FROM openjdk:8-jre
MAINTAINER mireal <mirrorming@qq.com>

RUN jarname=`ls -t | grep blog | grep -v 'sources'| awk '{print $1}'|head -n 1`

RUN echo $PWD
RUN echo $jarname

RUN mkdir /app
COPY $jarname /app/blog.jar
RUN echo $PWD
RUN echo $jarname

ENTRYPOINT ["java", "-jar","-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8899", "/app/blog.jar"]

EXPOSE 8880
EXPOSE 8899