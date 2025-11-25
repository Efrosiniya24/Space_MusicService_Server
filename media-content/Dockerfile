FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY . .

RUN mvn -pl media-content -am clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/media-content/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
