FROM java:8

LABEL maintainer = "vercility@gmail.com"
VOLUME /tmp
EXPOSE 8000
ARG JAR_FILE=target/server-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]