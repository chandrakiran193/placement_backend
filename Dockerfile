# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
 
# Cache dependencies first (faster rebuilds)
COPY pom.xml .
RUN mvn -B dependency:go-offline
 
COPY src ./src
RUN mvn -B clean package -DskipTests
 
# ---------- Stage 2: run ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
 
# Run as non-root
RUN useradd -r -u 1001 spring && mkdir -p /app/uploads/resumes && chown -R spring /app
USER spring
 
COPY --from=build /app/target/*.jar app.jar
 
EXPOSE 8080
 
# Uses $PORT if the host provides one (Render, Railway, etc.), otherwise 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]