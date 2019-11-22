FROM java:8

LABEL maintainer = "vercility@gmail.com"
VOLUME /tmp
EXPOSE 8000
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]