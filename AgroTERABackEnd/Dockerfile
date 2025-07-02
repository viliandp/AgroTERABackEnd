# Usa una imagen base de Java que sea ligera y adecuada para Spring Boot
# Eclipse Temurin es una buena opción con OpenJDK.
# Puedes elegir una versión específica, por ejemplo, `eclipse-temurin:21-jre-jammy` si usas Java 21.
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/AgroTERABackEnd-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
# ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]