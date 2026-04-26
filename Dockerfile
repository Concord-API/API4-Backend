FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY trivio/pom.xml .
COPY trivio/.mvn .mvn
COPY trivio/mvnw .
RUN mvn dependency:go-offline -q
COPY trivio/src src
RUN mvn package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
