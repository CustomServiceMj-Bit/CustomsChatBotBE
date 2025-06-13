# 베이스 이미지
FROM openjdk:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# JAR 복사
COPY build/libs/*.jar app.jar

# 앱 실행
ENTRYPOINT ["java", "-jar", "app.jar"]