# Use an official OpenJDK runtime as a parent image
FROM openjdk:8-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file into the container at /app
COPY target/test_bmc_java.jar test_bmc_java.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "test_bmc_java.jar"]

# docker build -t test_bmc_java
# docker run -p 8080:8080 test_bmc_java