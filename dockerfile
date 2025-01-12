# Base image with Java 11
FROM openjdk:11-jdk

# Set working directory


# Copy the JAR file to the container

# WORKDIR /app

# Copy the JAR file to the container
COPY target/CICD_Demo-1.0-SNAPSHOT.jar /CICD_Demo-1.0-SNAPSHOT.jar

# Command to run the JAR
CMD ["java", "-jar", "CICD_Demo-1.0-SNAPSHOT.jar"]
