FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY target/bmc-test-java-service.jar bmc-test-java-service.jar

EXPOSE 8080

CMD ["java", "-jar", "bmc-test-java-service.jar"]

# docker build -t test_bmc_java
# docker run -p 8080:8080 test_bmc_java