FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src/ ./src/

RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]