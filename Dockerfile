# 1단계: 빌드용 이미지
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# 2단계: 실행용 이미지 (Scouter 에이전트 추가)
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY ./scouter /scouter

# 기존 jar 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# --- ENTRYPOINT에 -javaagent 옵션 추가 ---
ENTRYPOINT ["java", "-javaagent:/scouter/scouter.agent.jar", "-Xms512m", "-Xmx4g", "-jar", "app.jar"]