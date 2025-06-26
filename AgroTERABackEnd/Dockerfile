# Usa una imagen base de OpenJDK para Java 21.
# Esta es una imagen ligera basada en Alpine Linux, ideal para contenedores.
FROM openjdk:21-jdk-slim-bullseye

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR ejecutable de tu aplicación Spring Boot al contenedor.
# Primero, asegúrate de haber construido tu JAR con 'mvn clean package'.
# Reemplaza 'AgroTERABackEnd-0.0.1-SNAPSHOT.jar' con el nombre real de tu archivo JAR.
COPY target/AgroTERABackEnd-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que tu aplicación Spring Boot escuchará.
# Por defecto, Spring Boot usa el puerto 8080.
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie.
# Usa 'java -jar' para ejecutar el JAR ejecutable.
ENTRYPOINT ["java", "-jar", "app.jar"]