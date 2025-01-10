# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory into the container
COPY target/myprogram.jar /app/myprogram.jar

# Command to run the JAR file
CMD ["java", "-jar", "myprogram.jar"]
