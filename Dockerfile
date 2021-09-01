FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/VerySimpleATM.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9500/tcp
ENTRYPOINT ["java","-jar","/app.jar"]