FROM openjdk:17-jdk-alpine

LABEL author="Filip Polner"

EXPOSE 8080

ARG JAR_FILE=out/artifacts/user_service_jar/user-service.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]