FROM openjdk:21
WORKDIR /app
COPY target/payment-service-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]