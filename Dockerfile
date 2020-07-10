FROM maven:3.5.0-jdk-8-alpine AS build
WORKDIR /app

ADD pom.xml pom.xml
ADD ./src src/

RUN mvn package -Dmaven.test.skip=true

FROM openjdk:8-jre-alpine
COPY --from=build /app/target/chat.jar /app/chat.jar

EXPOSE 62519 62520
ENTRYPOINT [ "sh", "-c", "java -jar /app/realtime.jar"]