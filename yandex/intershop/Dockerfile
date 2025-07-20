FROM openjdk:21
WORKDIR /app
COPY target/intershop-0.0.1-SNAPSHOT.jar app.jar
COPY ../storage/images /var/lib/storage/images
ENTRYPOINT ["java", "-jar", "app.jar"]