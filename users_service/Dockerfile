FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY . .

RUN mvn -pl users_service -am clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/users_service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
