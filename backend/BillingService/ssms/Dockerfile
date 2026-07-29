# Stage 1: Build common-events library & BillingService microservice
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Step 1: Install shared dependency common-events
COPY backend/common-events/common-events /app/common-events
RUN cd /app/common-events && mvn clean install -DskipTests

# Step 2: Build BillingService
WORKDIR /app/billingservice
COPY backend/BillingService/ssms/pom.xml .
COPY backend/BillingService/ssms/src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime environment
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/billingservice/target/*.jar app.jar
EXPOSE 8073
ENTRYPOINT ["java", "-jar", "app.jar"]
