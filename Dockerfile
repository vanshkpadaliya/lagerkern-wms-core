# --- Build Stage ---
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace

# Copy Maven wrapper and project descriptor
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy source code and package application
COPY src src
RUN ./mvnw package -DskipTests

# --- Run Stage ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-root user for security best practices
RUN useradd -u 1000 spring && chown -R spring:spring /app
USER spring

# Copy built jar from the build stage (matching your artifactId 'lagerkern')
COPY --from=build /workspace/target/lagerkern-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]