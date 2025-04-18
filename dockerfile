# Etapa 1: Construcción con Maven
FROM maven:3.9.4-eclipse-temurin-21 AS backend-build

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el código fuente al contenedor
COPY ./ /app

# Construir el proyecto con Maven
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con el JDK y el archivo JAR
FROM eclipse-temurin:21-jdk

# Copiar el archivo JAR desde la etapa anterior
COPY --from=backend-build /app/target/inventory-valu-0.0.1-SNAPSHOT.jar /app/app.jar

# Exponer el puerto en el que corre Spring Boot (8080 por defecto)
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
