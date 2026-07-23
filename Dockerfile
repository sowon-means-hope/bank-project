# Build Stage
#FROM gradle:9.5.1-jdk25 AS builder
#WORKDIR /app
#COPY . .
#RUN gradle clean bootJar -x test --no-daemon

# Run Stage
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]