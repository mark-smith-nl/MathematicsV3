FROM openjdk:9
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

