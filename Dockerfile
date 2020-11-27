FROM openjdk:11-jdk-slim
COPY tea-service/target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Xmx2G", "-jar","app.jar"]
