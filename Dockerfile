FROM openjdk:17
COPY /build/libs/tg-bot-rusweb-0.0.1-SNAPSHOT-plain.jar app.jar
WORKDIR /app
ENTRYPOINT ["java","-jar","/app.jar"]