# JVM mode build
FROM openjdk:21-slim as builder

WORKDIR /app

COPY . .

# 테스트 포함하여 빌드 (데몬 비활성화 + JVM 옵션)
RUN ./gradlew build --no-daemon -Dorg.gradle.jvmargs="-Xmx1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Final stage
FROM openjdk:21-slim

WORKDIR /app

# gradlew와 소스코드 복사 (테스트 실행을 위해)
COPY --from=builder /app/gradlew .
COPY --from=builder /app/gradle ./gradle
COPY --from=builder /app/build.gradle .
COPY --from=builder /app/settings.gradle .
COPY --from=builder /app/src ./src
COPY --from=builder /app/build/libs/fashion-coordination-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8090

# 기본 명령어는 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"] 