![CodeRabbit Pull Request Reviews](https://img.shields.io/coderabbit/prs/github/ureca-poject-unear/unear-admin-backend?utm_source=oss&utm_medium=github&utm_campaign=ureca-poject-unear%2Funear-admin-backend&labelColor=171717&color=FF570A&link=https%3A%2F%2Fcoderabbit.ai&label=CodeRabbit+Reviews)

## 프로젝트 이름

> 당신 근처에, U:Near 시스템 백엔드

---

## 프로젝트 개요


---

## 시스템 아키텍쳐


---

## 기술 스택

| 구성 요소 | 기술 |
|-----------|------|
| 백엔드 프레임워크 | Spring Boot  |
| 데이터베이스 | PostgreSQL (RDS) |
| 로그 브로커 | Redis Stream |
| 로그 소비자 | Spring + Redis Consumer Group |
| 배치 처리 | Apache Airflow |
| 배포 인프라 | AWS EC2, Docker, Nginx |
| API 문서화 | Swagger/OpenAPI |
| 기타 | JPA, Spring Security |

---

## 프로젝트 구조

<pre>
unear-infra/
├── unear-user-backend/          # 사용자 서비스 API 서버
├── unear-pos-backend/           # 가맹점 POS 연동 서버
├── unear-admin-backend/         # 관리자 웹 대시보드 백엔드
├── unear-log-consumer/          # Redis Stream 로그 컨슈머 (로그 적재)
└── unear-airflow-analysis/      # 사용자 행동 로그 요약 및 통계 분석 (Airflow DAG)
</pre>

