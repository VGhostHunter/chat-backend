FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /app

ADD pom.xml pom.xml
ADD ./src src/

RUN mvn package -Dmaven.test.skip=true

FROM openjdk:11-jre
COPY --from=build /app/target/chat.jar /app/chat.jar

EXPOSE 62519 62520
ENTRYPOINT [ "sh", "-c", "java -jar /app/chat.jar"]
