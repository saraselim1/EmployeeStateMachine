FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/app.jar"]


