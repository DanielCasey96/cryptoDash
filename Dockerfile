
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Run `.\gradlew.bat bootJar` before building the image
COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]