FROM eclipse-temurin:17 as jre-build
#Change .jar name depends on which container you will run#########
WORKDIR /app
COPY /build/libs/tg-bot-rusweb-0.0.1-SNAPSHOT.jar spamCleaner.jar
COPY /src/main/resources/words/english_words /app/src/main/resources/words/english_words
COPY /src/main/resources/words/french_words /app/src/main/resources/words/french_words
COPY /src/main/resources/words/general_words /app/src/main/resources/words/general_words
COPY /src/main/resources/words/german_words /app/src/main/resources/words/german_words
COPY /src/main/resources/words/italian_words /app/src/main/resources/words/italian_words
COPY /src/main/resources/words/spanish_words /app/src/main/resources/words/spanish_words
ENTRYPOINT ["java","-jar","spamCleaner.jar"]