FROM maven:3.9.3-eclipse-temurin-8 AS build
COPY src /app/src
COPY pom.xml /app
COPY config.yml /app
RUN mvn -f /app/pom.xml package -DskipTests

FROM eclipse-temurin:8-jre-focal
COPY --from=build /app/target/*.jar /app.jar
COPY --from=build /app/config.yml /config.yml
ENTRYPOINT ["java","-jar","/app.jar","server","config.yml"]
