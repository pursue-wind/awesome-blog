FROM openjdk:8-jre
MAINTAINER mireal <mirrorming@qq.com>

RUN mkdir /app

COPY blog-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8080