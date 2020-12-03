FROM openjdk:8-alpine

WORKDIR /opt/app

ADD target/*.jar profectus-ui-framework-1.0.0.RELEASE.jar

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait wait

RUN chmod +x wait

CMD ./wait &&  java -jar -Dspring.profiles.active=prod profectus-ui-framework-1.0.0.RELEASE.jar
