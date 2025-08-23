import http from 'k6/http';
import { check } from 'k6';

// ========= ENV =========
const BASE_URL   = __ENV.BASE_URL   || 'http://localhost:8080';
const COUPON_ID  = __ENV.COUPON_ID  || '326';
const METHOD     = (__ENV.METHOD || 'POST').toUpperCase();
const AUTH_TOKEN = __ENV.AUTH_TOKEN || '';

// 유저 ID 범위 (1000 ~ 4000)
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
        fcfs_spike: {
            executor: 'shared-iterations',
            vus: TOTAL_USERS,           // 3000
            iterations: TOTAL_USERS,    // 각자 1회
            maxDuration: '1m',
            startTime: '0s',
            gracefulStop: '0s',
        },
    },
    thresholds: {
        http_req_failed:    ['rate<0.05'],
        http_req_duration:  ['p(95)<2000'],
    },
    discardResponseBodies: true,
};
export const summaryTrendStats = ['avg','min','med','max','p(90)','p(95)'];


// ========= Helpers =========
function params(uid) {
    const headers = { 'Content-Type': 'application/json' };
    if (AUTH_TOKEN) headers['Authorization'] = `Bearer ${AUTH_TOKEN}`;
    headers['X-Req-Id'] = `${Date.now()}-${uid}`;
    return { headers, tags: { coupon: COUPON_ID, user: String(uid) } };
}

// ========= VU logic =========
export default function () {
    const uid = userIds[__VU - 1]; // VU 번호 → 유저ID 매핑
    const url = `${BASE_URL}/coupons/${COUPON_ID}/fcfs-test?testUserId=${uid}`;
    const p = params(uid);

    const res = METHOD === 'POST'
        ? http.post(url, null, p)
        : http.get(url, p);

    check(res, {
        'valid status': (r) =>
            r.status === 200 ||
            r.status === 201 ||
            r.status === 202 ||
            r.status === 409 || // 중복
            r.status === 410 || // 소진
            r.status === 429,   // 레이트 리밋
    });
}