# Usar una imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim
 
# Establecer la variable de zona horaria
ENV TZ=America/Regina
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
 
# Establecer el directorio de trabajo
WORKDIR /app
 
# Agregar el archivo JAR de la aplicación
ADD /target/carga-orden-0.0.1-SNAPSHOT.jar app.jar
 
# Exponer el puerto que utilizará la aplicación
EXPOSE 8080
 
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]