FROM openjdk:8-jre
MAINTAINER mireal <mirrorming@qq.com>

RUN jarname=`ls | grep blog | awk '{print $1}'`

RUN mkdir /app
COPY $jarname /app/blog.jar

ENTRYPOINT ["java", "-jar","-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8088", "/app/blog.jar", "--spring.profiles.active=test"]

EXPOSE 8080