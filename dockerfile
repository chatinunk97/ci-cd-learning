# Base image with Java 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from the 'target' directory (relative to the build context)
COPY target/CICD_Demo-1.0-SNAPSHOT.jar /app/

# Command to run the JAR
CMD ["java", "-jar", "CICD_Demo-1.0-SNAPSHOT.jar"]
