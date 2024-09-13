FROM gradle:7.6.4-jdk11 AS build
WORKDIR /app
COPY . /app
RUN ./gradlew build

FROM openjdk:11-jre-slim
COPY --from=build /app/build/libs/patient-app-0.1.war /app/app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.war"]