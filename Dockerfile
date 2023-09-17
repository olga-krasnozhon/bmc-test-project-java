FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY target/bmc-test-java-service-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "bmc-test-java-service-0.0.1-SNAPSHOT.jar"]