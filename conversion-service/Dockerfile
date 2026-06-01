# Stage 1: Build the application
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and package the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from the first stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Conversion Service port
EXPOSE 8081

# Start the application with memory limit for Render's free tier
ENTRYPOINT ["java", "-Xmx300m", "-jar", "app.jar"]
