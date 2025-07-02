Resumen del Proceso de Despliegue de API Spring Boot en Google Cloud
Este documento resume los pasos clave que realicé para desplegar mi API de Spring Boot en Google Cloud Run y conectarla a mi base de datos MySQL en Google Cloud SQL, permitiendo que mi aplicación JavaFX se comunique con ella.

1. Punto de Partida
Mi aplicación API de Spring Boot ya funcionaba correctamente en local. Esto significa que la lógica de negocio, los controladores, los servicios, los repositorios y la seguridad (JWT) estaban operativos y no necesité modificaciones a nivel de código de la API.

Mi base de datos MySQL en Google Cloud SQL ya estaba creada y accesible con sus credenciales.

2. Adaptación de la Configuración del Backend
application.properties: Actualicé el archivo application.properties en mi proyecto Spring Boot para apuntar a la dirección IP pública de mi instancia de Google Cloud SQL, así como al puerto, nombre de la base de datos y credenciales.

spring.datasource.url=jdbc:mysql://130.211.202.189:3306/agrotera
spring.datasource.username=root
spring.datasource.password=XXX.XXX.XXX
# ... otras propiedades



(Nota: Las credenciales y secretos los pasé como variables de entorno durante el despliegue para mayor seguridad, aunque los mantuve en el archivo para la fase de construcción local).

3. Preparación para el Despliegue en Cloud Run (Contenerización)
Creación del Dockerfile: Añadí un Dockerfile en la raíz de mi proyecto Spring Boot. Este archivo es una "receta" para Docker que le indica cómo construir una imagen de contenedor de mi aplicación. Define la imagen base de Java, copia mi JAR compilado y especifica cómo ejecutar la aplicación.

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/AgroTERABackEnd-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]



4. Construcción y Subida de la Imagen Docker
Generación del JAR: Ejecuté mvn clean package para compilar mi aplicación Spring Boot y generar el archivo .jar ejecutable en la carpeta target/.

Autenticación Docker con GCP: Usé gcloud auth configure-docker para permitir que mi Docker local interactúe con Google Container Registry (GCR).

Construcción de la Imagen: Ejecuté docker build -t gcr.io/YOUR_PROJECT_ID/agrotera-backend:1.0.0 . para crear la imagen Docker de mi aplicación localmente.

Subida de la Imagen: Usé docker push gcr.io/YOUR_PROJECT_ID/agrotera-backend:1.0.0 para subir la imagen construida a Google Container Registry, haciéndola accesible para Cloud Run.

5. Despliegue de la API en Google Cloud Run
Comando gcloud run deploy: Utilicé el comando gcloud run deploy para desplegar la imagen de mi API en Cloud Run.

Especificé el nombre del servicio (agrotera-api).

Indiqué la imagen de GCR.

Configuró para usar la plataforma gestionada (--platform managed).

Definí la región de despliegue (--region europe-west1).

Permití el acceso no autenticado (--allow-unauthenticated) para que la API fuera pública.

¡Clave! Paso de Variables de Entorno: Pasé las credenciales de la base de datos (SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD) y los secretos de JWT (JWT_SECRET, JWT_EXPIRATION) como variables de entorno. Esto es más seguro que tenerlos en el JAR.

6. Configuración de Acceso a la Base de Datos en Cloud SQL
Autorización de IP Pública: Para permitir que Cloud Run se conectara a Cloud SQL, añadí temporalmente 0.0.0.0/0 a las "Redes autorizadas" de mi instancia de Cloud SQL en la consola de Google Cloud. Esto resolvió el error de "Communications link failure".

7. Adaptación del Cliente JavaFX
Cambio de BASE_URL: La única modificación necesaria en mi aplicación JavaFX fue actualizar la constante BASE_URL en HelloController.java para que apuntara a la URL pública de mi servicio Cloud Run.

private static final String BASE_URL = "https://agrotera-api-263057861964.europe-west1.run.app/";



8. Depuración y Resolución de Errores
Durante el proceso, identifiqué y resolví los siguientes errores comunes:

Errores de sintaxis de comandos en CMD/PowerShell: Ajusté la forma de escribir comandos largos para que fueran compatibles con mi terminal de Windows.

Fallo de inicio del contenedor en Cloud Run (failed to start and listen on port): Diagnostiqué que la causa raíz era la falta de conexión a la base de datos.

Fallo de conexión JDBC (Communications link failure): Lo resolví autorizando la IP de Cloud Run en Cloud SQL (usando 0.0.0.0/0 como solución temporal).

Error de validación en el login (username: rejected value [null]): Identifiqué un desajuste en el nombre del campo (email vs username) en la clase LoginRequest entre el cliente JavaFX y el backend de Spring Boot, y lo corregí para que coincidieran.

9. Actualización de la API y Despliegue de Cambios
Cuando necesito actualizar mi API con nuevos cambios (ya sea código, configuración o credenciales), el proceso de despliegue sigue una secuencia similar, pero es crucial asegurar que todos los pasos se repitan para que los cambios se reflejen en la nube.

Por ejemplo, si he cambiado la contraseña del usuario SQL en mi base de datos a XXX.XXX.XXX y he actualizado spring.datasource.password en mi application.properties local:

Actualizar la Base de Datos en Cloud SQL: Primero, me aseguro de que la contraseña del usuario root (o el usuario que esté utilizando) en la instancia de Google Cloud SQL se haya cambiado a 26sZlS1.E*f}R;r[. Esto se hace a través de la consola de Google Cloud en la sección de "Usuarios" de la instancia de Cloud SQL.

Actualizar la Configuración Local: Verifico que mi application.properties local refleje la nueva contraseña:

spring.datasource.password=XXX.XXX.XXX

Si he movido las credenciales a variables de entorno (como se recomienda para producción), me aseguro de que mi código lea de esas variables y que yo tenga la nueva contraseña lista para el siguiente paso.

Reconstruir el JAR de la API: Cualquier cambio en el código fuente o en application.properties requiere una nueva compilación.

mvn clean package

Esto generará un nuevo JAR con los cambios.

Reconstruir la Imagen Docker: Con el nuevo JAR, necesito construir una nueva imagen Docker. Es una buena práctica usar una nueva etiqueta (tag) para la imagen (ej. 1.0.1 o 2.0.0) para mantener un historial de versiones.

docker build -t gcr.io/t-operative-460413-j6/agrotera-backend:1.0.1 .

(Aquí he usado 1.0.1 como nueva etiqueta).

Subir la Nueva Imagen Docker: Subo esta nueva imagen a Google Container Registry.

docker push gcr.io/t-operative-460413-j6/agrotera-backend:1.0.1

Redesplegar la API en Cloud Run: Finalmente, despliego la nueva imagen en Cloud Run. Es crucial actualizar las variables de entorno (--set-env-vars) con los nuevos valores si estos han cambiado (como la contraseña en este caso).

gcloud run deploy agrotera-api \
  --image gcr.io/t-operative-460413-j6/agrotera-backend:1.0.1 \
  --platform managed \
  --region europe-west1 \
  --allow-unauthenticated \
  --set-env-vars SPRING_DATASOURCE_URL="jdbc:mysql://130.211.202.189:3306/agrotera",SPRING_DATASOURCE_USERNAME="root",SPRING_DATASOURCE_PASSWORD="XXX.XXX.XXX",JWT_SECRET="miClaveSuperSecretaParaAgroTERAconMasDe256BitsDeLongitud",JWT_EXPIRATION="3600000"

(Nota: He actualizado SPRING_DATASOURCE_PASSWORD con la nueva contraseña en el comando set-env-vars).

Cloud Run gestionará la creación de una nueva revisión de mi servicio y dirigirá el tráfico a esta nueva versión una vez que esté lista y saludable, permitiendo despliegues sin interrupciones.

10. Resultado Final
Mi API de Spring Boot está ahora completamente funcional y desplegada en Google Cloud Run.

Se conecta exitosamente a mi base de datos MySQL en Google Cloud SQL.

Mi aplicación cliente JavaFX puede comunicarse sin problemas con la API desplegada para realizar operaciones de registro y login.

Listo para el Commit
Dado que el proyecto ahora funciona de extremo a extremo, puedo hacer un commit con confianza. Los cambios clave para el commit en mi repositorio de backend incluirían:

El Dockerfile añadido.

Cualquier ajuste en mi application.properties (aunque las credenciales sensibles se manejan por entorno).

Las correcciones en la clase LoginRequest (si aplica).

(Opcional) La adición de logging para depuración que quiera mantener.
