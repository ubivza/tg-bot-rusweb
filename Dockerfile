FROM gradle:8.7-jdk17-jammy AS builder

WORKDIR /home/gradle/project

COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN gradle --no-daemon clean bootJar

FROM openjdk:17-ea-jdk-slim

WORKDIR /app

COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]