# Base image with Java 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file to the container
COPY target/CICD_Demo-1.0-SNAPSHOT.jar target/CICD_Demo-1.0-SNAPSHOT.jar

# Command to run the JAR
CMD ["java", "-jar", "myprogram.jar"]
