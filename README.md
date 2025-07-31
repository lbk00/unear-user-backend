![CodeRabbit Pull Request Reviews](https://img.shields.io/coderabbit/prs/github/ureca-poject-unear/unear-admin-backend?utm_source=oss&utm_medium=github&utm_campaign=ureca-poject-unear%2Funear-admin-backend&labelColor=171717&color=FF570A&link=https%3A%2F%2Fcoderabbit.ai&label=CodeRabbit+Reviews)

## 프로젝트 이름

> 당신 근처에, U:Near 시스템 백엔드

---

## 프로젝트 개요

이 프로젝트는 사용자 행동 로그를 수집하여 분석하고,  
추천 시스템 및 관리자 대시보드에 활용 가능한 백엔드 서비스를 제공합니다.

- 사용자 요청 API 처리 (`/api/app`)
- 사용자 행동 로그 Redis Stream 전송
- 로그 컨슈머를 통한 DB 적재
- Airflow 기반 요약 데이터 배치 처리
- 관리자용 시각화 API (`/api/admin`)

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

