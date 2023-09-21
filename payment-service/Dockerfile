# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY ./build/libs/payment-service-0.0.1-SNAPSHOT.jar /app/payment-service.jar

# Command to run your application
CMD ["java", "-jar", "payment-service.jar"]