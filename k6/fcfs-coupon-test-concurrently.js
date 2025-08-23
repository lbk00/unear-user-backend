import http from 'k6/http';
import { check, sleep } from 'k6';

// ========= ENV =========
const BASE_URL   = __ENV.BASE_URL   || 'http://localhost:8080';
const COUPON_ID  = __ENV.COUPON_ID  || '326';
const METHOD     = (__ENV.METHOD || 'POST').toUpperCase();
const AUTH_TOKEN = __ENV.AUTH_TOKEN || '';

// 유저 ID 범위 (1000 ~ 3999) - 3000명으로 확장
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
        // 진짜 동시성 테스트 - 3000명이 동시에 요청
        fcfs_concurrent: {
            executor: 'shared-iterations',
            vus: 1000,              // 3000명 동시 실행
            iterations: 1000,       // 3000번 요청 (1인당 1번)
            maxDuration: '30s',     // 최대 30초 안에 완료
        },
    },

    thresholds: {
        http_req_failed: ['rate<0.20'],         // 20% 미만 실패율 (더 관대하게)
        http_req_duration: ['p(95)<5000'],      // 95% 요청이 5초 이내 (여유 확대)
        http_req_connecting: ['p(95)<2000'],    // 연결 시간 여유 확대
        http_req_waiting: ['p(95)<4000'],       // 서버 응답 대기 시간 여유 확대
    },

    // 연결 최적화
    discardResponseBodies: true,
    noConnectionReuse: false,
    insecureSkipTLSVerify: true,
    batch: 10,
    batchPerHost: 5,
};

export const summaryTrendStats = ['avg','min','med','max','p(90)','p(95)','p(99)'];

// ========= Helpers =========
function params(uid) {
    const headers = {
        'Content-Type': 'application/json',
        'Connection': 'keep-alive',
        'Accept-Encoding': 'gzip',
    };

    if (AUTH_TOKEN) headers['Authorization'] = `Bearer ${AUTH_TOKEN}`;
    headers['X-Req-Id'] = `${Date.now()}-${uid}-${__VU}`;

    return {
        headers,
        tags: {
            coupon: COUPON_ID,
            user: String(uid),
            scenario: 'fcfs_concurrent'
        },
        timeout: '20s', // 타임아웃 확대
        compression: 'gzip',
    };
}

// ========= VU logic =========
export default function () {
    // 동시성 테스트를 위해 지연 제거
    // if (__ITER === 0) {
    //     sleep(Math.random() * 0.5); // 지연 없음!
    // }

    const uid = userIds[(__VU - 1) % TOTAL_USERS];
    const url = `${BASE_URL}/coupons/${COUPON_ID}/fcfs_test?testUserId=${uid}`;
    const p = params(uid);

    try {
        const startTime = Date.now();

        const res = METHOD === 'POST'
            ? http.post(url, null, p)
            : http.get(url, p);

        const endTime = Date.now();
        const responseTime = endTime - startTime;

        // 간소화된 체크
        const checks = check(res, {
            'status success': (r) => [200, 201, 202].includes(r.status),
            'status business error': (r) => [409, 410, 429].includes(r.status),
            'response time < 10s': (r) => responseTime < 10000, // 동시성 테스트라 여유 확대
        });

        // 중요한 로그만 (더 높은 샘플링)
        if (res.status === 200 && Math.random() < 0.02) { // 2% 샘플링
            console.log(`User ${uid} - SUCCESS: ${responseTime}ms`);
        }
        if ([409, 410, 429].includes(res.status) && Math.random() < 0.02) { // 2% 샘플링
            console.log(`User ${uid} - SOLD OUT: Status ${res.status}, ${responseTime}ms`);
        }
        if (res.status >= 500 && Math.random() < 0.1) { // 서버 에러는 10% 샘플링
            console.error(`User ${uid} - SERVER ERROR: Status ${res.status}, ${responseTime}ms`);
        }

    } catch (error) {
        if (Math.random() < 0.1) {
            console.error(`User ${uid} - Exception: ${error.message}`);
        }
        check(null, { 'no exceptions': false });
    }

    // 동시성 테스트를 위해 sleep 제거!
    // sleep(0.02);
}