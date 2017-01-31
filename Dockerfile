FROM openjdk:jre-alpine
MAINTAINER Martin Dilger <martin@effectivetrainings.de>

RUN apk update && apk add curl

ADD target/counter-app-0.0.1-SNAPSHOT.jar  /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
