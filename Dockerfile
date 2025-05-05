FROM eclipse-temurin:17-alpine

WORKDIR /app

COPY target/*.jar .

EXPOSE 8080

# TODO: precisa configurar
#CMD ["java", "-jar", "desafio-0.0.1-SNAPSHOT.jar"]