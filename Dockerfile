# --- 1단계: Build 단계 ---
FROM gradle:8.5-jdk17-alpine AS builder

WORKDIR /app
COPY . /app
RUN gradle build -x test

# --- 2단계: 실행용 이미지 ---
FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]