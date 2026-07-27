# Stage 1: Build common-events library & ShipmentService microservice
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Step 1: Install shared dependency common-events
COPY backend/common-events/common-events /app/common-events
RUN cd /app/common-events && mvn clean install -DskipTests

# Step 2: Build ShipmentService
WORKDIR /app/shipmentservice
COPY backend/ShipmentService/ssms/pom.xml .
RUN mvn dependency:go-offline -B
COPY backend/ShipmentService/ssms/src ./src
RUN mvn package -DskipTests

# Stage 2: Runtime environment
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/shipmentservice/target/*.jar app.jar
EXPOSE 8072
ENTRYPOINT ["java", "-jar", "app.jar"]
