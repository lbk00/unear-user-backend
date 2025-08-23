# 1단계: 빌드용 이미지
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# 2단계: 실행용 이미지 (Scouter 관련 내용 모두 삭제)
FROM openjdk:17-jdk-slim
WORKDIR /app
# Scouter 관련 COPY 라인 삭제
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
# ENTRYPOINT를 원래대로 복구
ENTRYPOINT ["java", "-Xms512m", "-Xmx4g", "-jar", "app.jar"]