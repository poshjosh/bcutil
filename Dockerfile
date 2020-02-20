# github.com/poshjosh/bcutil
# FROM openjdk:8-jdk-alpine
FROM maven:3-alpine
RUN addgroup -S looseboxes && adduser -S poshjosh -G looseboxes
USER looseboxes:poshjosh
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]