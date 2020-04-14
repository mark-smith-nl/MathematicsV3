FROM openjdk:9
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081 8000

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000","-jar","/app.jar"]
