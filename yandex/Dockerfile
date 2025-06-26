FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/yandex-0.0.1-SNAPSHOT.jar app.jar
COPY storage/images /var/lib/storage/images

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]