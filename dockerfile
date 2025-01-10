# Base image with Java 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file to the container
COPY myprogram.jar /app/myprogram.jar

# Command to run the JAR
CMD ["java", "-jar", "myprogram.jar"]
