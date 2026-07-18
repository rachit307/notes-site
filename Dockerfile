# --- Build stage ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first so dependency layer is cached separately from source changes
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
COPY notes ./notes
RUN mvn -B clean package -DskipTests

# --- Runtime stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/notes ./notes

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
