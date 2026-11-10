FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

COPY target/comunicacao_api-0.0.1-SNAPSHOT.jar /app/comunicacao_api.jar

EXPOSE 8080

CMD ["java", "-jar","/app/comunicacao_api.jar"]