'''# 🏗️ Arquetipo Maven - Microservicios de Laboratorios

## 📋 Descripción

Este arquetipo Maven proporciona una plantilla estandarizada para crear nuevos microservicios Spring Boot con Oracle Database para el Sistema de Gestión de Laboratorios Clínicos.

## ✨ Características Incluidas

- **Spring Boot 3.2.0** con Java 17
- **Spring Data JPA** para persistencia
- **Oracle Database** (drivers JDBC + soporte para Oracle Cloud)
- **Lombok** para reducir boilerplate
- **Validaciones** con Bean Validation
- **Configuración Docker** lista para usar
- **Estructura de capas** (Controller, Service, Repository, Model, DTO, Exception)
- **Formato de respuesta API estándar** con traceId

## 📦 Instalación del Arquetipo

### 1. Instalar el arquetipo en el repositorio local de Maven

```bash
cd arquetipo-microservicio
mvn clean install
```

### 2. Verificar que se instaló correctamente

```bash
mvn archetype:crawl
```

## 🚀 Uso del Arquetipo

### Generar un nuevo microservicio

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.duoc.laboratorio \
  -DarchetypeArtifactId=arquetipo-microservicio \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.duoc.laboratorio \
  -DartifactId=microservicio-pagos \
  -Dversion=1.0.0 \
  -Dpackage=com.duoc.laboratorio.pagos \
  -Dpuerto=8084
```

### Parámetros

| Parámetro | Descripción | Ejemplo |
|-----------|-------------|---------|
| `groupId` | ID del grupo Maven | `com.duoc.laboratorio` |
| `artifactId` | Nombre del microservicio | `microservicio-pagos` |
| `version` | Versión inicial | `1.0.0` |
| `package` | Paquete Java base | `com.duoc.laboratorio.pagos` |
| `puerto` | Puerto del microservicio | `8084` |

## 📂 Estructura Generada

```
microservicio-nuevo/
├── pom.xml
├── Dockerfile
├── .dockerignore
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/duoc/laboratorio/nuevo/
│   │   │       ├── config/
│   │   │       │   └── OracleWalletConfig.java
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── dto/
│   │   │       │   └── ApiResponse.java
│   │   │       ├── exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── ResourceNotFoundException.java
│   │   │       │   └── BadRequestException.java
│   │   │       └── Application.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-cloud.yml
│   └── test/
│       └── java/
└── README.md
```

## 🔧 Configuración del Nuevo Microservicio

### 1. Actualizar `application.yml`

```yaml
server:
  port: ${puerto}

spring:
  application:
    name: ${artifactId}
  
  datasource:
    url: jdbc:oracle:thin:@laboratoriosdb_high?TNS_ADMIN=/path/to/wallet
    username: ADMIN
    password: ${ORACLE_PASSWORD}
```

### 2. Agregar al Docker Compose

```yaml
  ${artifactId}:
    build: ./${artifactId}
    container_name: ${artifactId}
    ports:
      - "${puerto}:${puerto}"
    depends_on:
      - oracle-db
    networks:
      - laboratorios-network
```

## 📝 Buenas Prácticas

1. **Nombres de Endpoints:** Usar `/api/{recurso}` como base
2. **Respuestas:** Usar siempre `ApiResponse<T>` con traceId
3. **Validaciones:** Usar anotaciones de Bean Validation
4. **Excepciones:** Manejadas globalmente con `GlobalExceptionHandler`
5. **DTOs:** Separar entidades JPA de objetos de transferencia
6. **Capas:** Mantener separación clara entre Controller → Service → Repository

## 🎯 Ejemplo de Uso Completo

```bash
# 1. Generar nuevo microservicio
mvn archetype:generate \
  -DarchetypeGroupId=com.duoc.laboratorio \
  -DarchetypeArtifactId=arquetipo-microservicio \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.duoc.laboratorio \
  -DartifactId=microservicio-reportes \
  -Dversion=1.0.0 \
  -Dpackage=com.duoc.laboratorio.reportes \
  -Dpuerto=8085

# 2. Entrar al directorio
cd microservicio-reportes

# 3. Compilar
mvn clean package

# 4. Ejecutar
mvn spring-boot:run

# 5. Probar
curl http://localhost:8085/api/reportes/health
```

## 📚 Componentes Pre-configurados

### ApiResponse (DTO Estándar)

```java
ApiResponse.success("Operación exitosa", data);
ApiResponse.error("Error en la operación");
ApiResponse.custom("CUSTOM_CODE", "Mensaje", data);
```

### Excepciones Personalizadas

```java
throw new ResourceNotFoundException("Recurso", id);
throw new BadRequestException("Datos inválidos");
```

### Configuración Oracle Cloud

```java
@Configuration
public class OracleWalletConfig {
    // Configura automáticamente el wallet de Oracle
}
```

## 🐳 Docker

El arquetipo genera automáticamente:

- **Dockerfile** multi-stage con build y runtime
- **.dockerignore** optimizado
- Health checks configurados

```bash
# Build de la imagen
docker build -t ${artifactId}:1.0 .

# Ejecutar contenedor
docker run -p ${puerto}:${puerto} ${artifactId}:1.0
```

## 🔍 Verificación

Después de generar un microservicio, verifica:

1. ✅ El puerto está disponible
2. ✅ Las dependencias se descargan correctamente
3. ✅ La aplicación compila sin errores
4. ✅ Los endpoints base funcionan
5. ✅ El health check responde

## 🆘 Troubleshooting

### Error: Arquetipo no encontrado

```bash
# Reinstalar el arquetipo
cd arquetipo-microservicio
mvn clean install
```

### Error: Puerto en uso

```bash
# Cambiar el puerto en application.yml
server.port: 8086
```

### Error: No encuentra Oracle Driver

```bash
# Agregar dependencia en pom.xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <version>21.9.0.0</version>
</dependency>
```

## 📄 Licencia

Este arquetipo es parte del Sistema de Gestión de Laboratorios - DUOC UC 2025

---

**Versión:** 1.0.0  
**Fecha:** Noviembre 2025  
**Autor:** Sistema de Gestión de Laboratorios

