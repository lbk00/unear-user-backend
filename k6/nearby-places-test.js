import http from 'k6/http';
import { check, sleep } from 'k6';

// --- 1. 테스트 옵션 설정 ---
// 이 부분에서 부하의 강도와 테스트 목표를 설정합니다.
export const options = {
    stages: [
        { duration: '30s', target: 100 }, // 30초 동안 사용자를 100명까지 서서히 늘립니다. (Ramp-up)
        { duration: '1m', target: 100 }, // 1분 동안 사용자 100명을 유지합니다. (Sustained Load)
        { duration: '10s', target: 0 },   // 10초 동안 사용자를 0명으로 줄입니다. (Ramp-down)
    ],
    thresholds: {
        // 테스트 성공/실패 기준을 정의합니다.
        'http_req_failed': ['rate<0.01'],   // HTTP 에러율이 1% 미만이어야 합니다.
        'http_req_duration': ['p(95)<500'], // 전체 요청의 95%가 500ms 안에 응답해야 합니다.
    },
};

// --- 2. 테스트 데이터 준비 ---
// 캐시를 피하고 실제 사용 환경과 유사하게 만들기 위해 여러 좌표를 준비합니다.
const locations = [
    { lat: 37.5665, lon: 126.9780 }, // 서울 시청
    { lat: 37.5124, lon: 127.0589 }, // 강남역
    { lat: 37.5512, lon: 126.9226 }, // 홍대입구역
    { lat: 37.5326, lon: 127.0246 }, // 여의도
    { lat: 37.4979, lon: 127.0276 }, // 서초역
];


// --- 3. 가상 사용자가 수행할 행동 정의 ---
export default function () {
    // 1. 테스트 데이터 중 하나를 무작위로 선택합니다.
    const randomLocation = locations[Math.floor(Math.random() * locations.length)];
    const randomUserId = Math.floor(Math.random() * 1000) + 1; // 1 ~ 1000 사이의 랜덤 유저 ID

    // 2. API 엔드포인트 URL을 만듭니다.
    const url = `http://localhost:8080/places/nearby-with-coupons-test?latitude=${randomLocation.lat}&longitude=${randomLocation.lon}&testUserId=${randomUserId}`;

    // 3. GET 요청을 보냅니다.
    const res = http.get(url);

    // 4. 응답이 성공했는지 확인합니다. (HTTP Status Code가 200인지)
    check(res, {
        'is status 200': (r) => r.status === 200,
    });

    // 5. 실제 사용자처럼 1초간 대기합니다.
    sleep(1);
}