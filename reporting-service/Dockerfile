FROM openjdk:8-jre-slim

WORKDIR /opt/app

ADD target/*.jar reporting-service-0.0.1-SNAPSHOT.jar

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait wait

RUN chmod +x wait

CMD ./wait &&  java -jar reporting-service-0.0.1-SNAPSHOT.jar
