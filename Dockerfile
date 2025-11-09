# syntax=docker/dockerfile:1

FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy pre-built JAR from local target/ directory
# Build the JAR first: ./mvnw clean package -DskipTests
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Non-root user for security
RUN useradd -ms /bin/bash appuser && \
    chown appuser:appuser app.jar
USER appuser

# App listens on 8080
EXPOSE 8080

# JVM options can be tuned via JAVA_OPTS environment variable
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
