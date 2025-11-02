# syntax=docker/dockerfile:1

# -------- Build stage --------
FROM maven:3.9-eclipse-temurin-24 AS build
WORKDIR /app

# Leverage Docker layer caching
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline

# Copy source and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests package

# -------- Run stage --------
FROM eclipse-temurin:24-jre
WORKDIR /app

# Non-root user for a bit more safety
RUN useradd -ms /bin/bash appuser
USER appuser

# Copy fat jar
COPY --from=build /app/target/*.jar app.jar

# App listens on 8080
EXPOSE 8080

# Recommended JVM options can be tuned via JAVA_OPTS
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
