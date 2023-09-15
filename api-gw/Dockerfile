# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY ./build/libs/api-gw-0.0.1-SNAPSHOT.jar /app/api-gw.jar

# Environment variables for PostgreSQL connection
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testdb
ENV SPRING_DATASOURCE_USERNAME=testuser
ENV SPRING_DATASOURCE_PASSWORD=testpass

# Expose the port that your Spring Boot app runs on (adjust as needed)
EXPOSE 8080

# Command to run your application
CMD ["java", "-jar", "api-gw.jar"]