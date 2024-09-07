FROM openjdk:17
COPY /build/libs/tg-bot-rusweb-0.0.1-SNAPSHOT.jar app.jar
WORKDIR /app
ENTRYPOINT ["java","-jar","/app.jar"]