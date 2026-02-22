FROM gradle:8.4-jdk11 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src/ src/

RUN gradle build --no-daemon -x test

FROM eclipse-temurin:11-jre

WORKDIR /app

COPY --from=builder /app/build/libs/sleep-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
