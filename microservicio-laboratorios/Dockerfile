# ========================================
# Dockerfile - Microservicio de Laboratorios
# Sistema de Gestión de Laboratorios
# ========================================

# Etapa 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY src ./src

# Compilar la aplicación
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear directorio para el wallet de Oracle
RUN mkdir -p /app/wallet

# Copiar el JAR compilado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto del microservicio
EXPOSE 8082

# Variables de entorno
ENV SPRING_PROFILES_ACTIVE=cloud
ENV ORACLE_PASSWORD=Oracle123#Lab

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8082/api/laboratorios/health || exit 1

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]

