import http from 'k6/http';
import { check, sleep } from 'k6';

// ========= ENV =========
const BASE_URL   = __ENV.BASE_URL   || 'http://localhost:8080';
const COUPON_ID  = __ENV.COUPON_ID  || '326';
const METHOD     = (__ENV.METHOD || 'POST').toUpperCase();
const AUTH_TOKEN = __ENV.AUTH_TOKEN || '';

// 유저 ID 범위 (1000 ~ 3999)
const USER_START = 1000;
const USER_END   = 3999;
const userIds = Array.from(
    { length: USER_END - USER_START + 1 },
    (_, i) => USER_START + i
);
const TOTAL_USERS = userIds.length; // 3000명

// ========= Options =========
export const options = {
    scenarios: {
        // 로컬 테스트용 - 점진적 증가
        fcfs_gradual: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '30s', target: 100 },   // 30초에 100명
                { duration: '30s', target: 300 },   // 30초에 300명
                { duration: '30s', target: 600 },   // 30초에 600명
                { duration: '30s', target: 1000 },  // 30초에 1000명
                { duration: '60s', target: 1000 },  // 60초간 1000명 유지
                { duration: '30s', target: 1500 },  // 30초에 1500명 (도전)
                { duration: '60s', target: 1500 },  // 60초간 1500명 유지
                { duration: '20s', target: 0 },     // 20초에 0명으로 감소
            ],
        },

        // 대안: 일정한 RPS로 테스트 (더 안정적)
        /*
        fcfs_constant_rps: {
            executor: 'constant-arrival-rate',
            rate: 500,          // 초당 500 요청 (점진적 증가 가능)
            timeUnit: '1s',
            duration: '3m',
            preAllocatedVUs: 50,
            maxVUs: 200,
        },
        */

        // 원본 스파이크 테스트 (3000명 동시 - 위험)
        /*
        fcfs_spike_original: {
            executor: 'shared-iterations',
            vus: 1000,              // 3000에서 1000으로 줄임
            iterations: 1000,       // 1000번 실행
            maxDuration: '2m',
            startTime: '0s',
            gracefulStop: '10s',    // 0s에서 10s로 변경
        },
        */
    },

    thresholds: {
        http_req_failed: ['rate<0.15'],         // 15% 미만 실패율 허용 (선착순 특성)
        http_req_duration: ['p(95)<5000'],      // 95% 요청이 5초 이내
        http_req_connecting: ['p(95)<2000'],    // 연결 시간 모니터링
        http_req_waiting: ['p(95)<3000'],       // 서버 응답 대기 시간
    },

    // 연결 최적화
    discardResponseBodies: true,
    noConnectionReuse: false,        // Keep-Alive 사용
    insecureSkipTLSVerify: true,

    // 배치 설정
    batch: 10,                       // 요청을 10개씩 배치로 처리
    batchPerHost: 5,                 // 호스트당 5개 배치
};

export const summaryTrendStats = ['avg','min','med','max','p(90)','p(95)','p(99)'];

// ========= Helpers =========
function params(uid) {
    const headers = {
        'Content-Type': 'application/json',
        'Connection': 'keep-alive',     // Keep-Alive 명시적 설정
        'Accept-Encoding': 'gzip',      // 압축 사용
    };

    if (AUTH_TOKEN) headers['Authorization'] = `Bearer ${AUTH_TOKEN}`;
    headers['X-Req-Id'] = `${Date.now()}-${uid}-${__VU}`;

    return {
        headers,
        tags: {
            coupon: COUPON_ID,
            user: String(uid),
            scenario: 'fcfs'
        },
        timeout: '30s',
        // 압축 응답 허용
        compression: 'gzip',
    };
}

// ========= VU logic =========
export default function () {
    // 첫 번째 요청 시에만 지연을 통한 연결 분산
    if (__ITER === 0) {
        sleep(Math.random() * 1); // 0-1초 랜덤 지연 (2초에서 1초로 줄임)
    }

    const uid = userIds[(__VU - 1) % TOTAL_USERS]; // 안전한 인덱스 계산
    const url = `${BASE_URL}/coupons/${COUPON_ID}/fcfs_test?testUserId=${uid}`;
    const p = params(uid);

    try {
        const startTime = Date.now();

        const res = METHOD === 'POST'
            ? http.post(url, null, p)
            : http.get(url, p);

        const endTime = Date.now();
        const responseTime = endTime - startTime;

        // 상세한 체크
        const checks = check(res, {
            'status is success (200-202)': (r) => [200, 201, 202].includes(r.status),
            'status is business error (409,410,429)': (r) => [409, 410, 429].includes(r.status),
            'no connection reset': (r) => !r.error || !r.error.includes('connection reset'),
            'no timeout': (r) => !r.error || !r.error.includes('timeout'),
            'response time < 10s': (r) => responseTime < 10000,
        });

        // 에러 상황 로깅 (샘플링)
        if (!checks['no connection reset'] || !checks['no timeout']) {
            if (Math.random() < 0.1) { // 10% 확률로만 로깅
                console.error(`User ${uid} - Error: ${res.error || 'Unknown'}, Status: ${res.status}, Time: ${responseTime}ms`);
            }
        }

        // 성공 케이스 로깅 (샘플링)
        if (res.status === 200 && Math.random() < 0.01) { // 1% 확률로만 로깅
            console.log(`User ${uid} - Success: Status ${res.status}, Time: ${responseTime}ms`);
        }

    } catch (error) {
        // 예외 상황 로깅 (샘플링)
        if (Math.random() < 0.1) { // 10% 확률로만 로깅
            console.error(`User ${uid} - Exception: ${error.message}`);
        }
        check(null, { 'no exceptions': false });
    }

    // 요청 간격 조절 (CPU 부하 분산)
    sleep(0.05); // 50ms 대기 (100ms에서 줄임)
}