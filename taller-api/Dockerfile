# --- Etapa 1: build ---
# Compilamos con Maven dentro de un contenedor, asi no hace falta tener
# Maven instalado en la maquina que construye la imagen.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos primero solo el pom.xml para aprovechar la cache de capas de Docker:
# si no cambian las dependencias, no se vuelven a descargar en cada build.
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Etapa 2: runtime ---
# Imagen final mucho mas ligera: solo el JRE, sin Maven ni el codigo fuente.
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Usuario no root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
