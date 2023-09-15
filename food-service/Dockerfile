FROM eclipse-temurin:17-jre-alpine
COPY . /usr/src/ff-food-service
WORKDIR /usr/src/ff-food-service
RUN javac Main.java
CMD ["java", "Main"]

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]