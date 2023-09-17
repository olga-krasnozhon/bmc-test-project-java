FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/bmc-test-java-service-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-Djava.library.path=/usr/lib", "-jar", "bmc-test-java-service-0.0.1-SNAPSHOT.jar"]