FROM eclipse-temurin:21-jdk
LABEL authors="victor vianna"
WORKDIR /app
COPY target/classroomapi-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]