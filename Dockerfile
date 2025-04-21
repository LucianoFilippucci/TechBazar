FROM openjdk:17-jdk-slim
LABEL authors="Luciano Filippucci"

WORKDIR /app

COPY target/TechBazar-0.0.1-SNAPSHOT.jar app.jar

RUN apt-get update && apt-get install -y maven

EXPOSE 8081

CMD ["mvn", "spring-boot:run"]