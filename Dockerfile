FROM openjdk:16-jdk-alpine

WORKDIR /app

COPY build/libs/online-shop-0.0.1-SNAPSHOT.jar ./online-shop-app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "./online-shop-app.jar" ]