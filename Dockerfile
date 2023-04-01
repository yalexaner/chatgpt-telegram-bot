# just need image with java installed in it
FROM eclipse-temurin:11

WORKDIR /app

ADD ChatGptTelegramBot.jar /app/ChatGptTelegramBot.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/ChatGptTelegramBot.jar"]