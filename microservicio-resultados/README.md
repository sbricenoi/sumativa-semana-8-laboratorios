# 📊 Microservicio de Gestión de Resultados

**Versión**: 1.0.0  
**Puerto**: 8083  
**Base de Datos**: Oracle Cloud

---

## 📋 Descripción

Microservicio encargado de la gestión de resultados de análisis clínicos en el Sistema de Gestión de Laboratorios Clínicos.

---

## ✨ Características

- ✅ Gestión completa de resultados (CRUD)
- ✅ Almacenamiento de valores de análisis en JSON
- ✅ Relación con citas y tipos de análisis
- ✅ Estados de resultados (PENDIENTE, COMPLETADO, REVISADO)
- ✅ Búsqueda y filtrado de resultados
- ✅ Observaciones y metadatos
- ✅ Documentación Swagger/OpenAPI
- ✅ Integración con Oracle Cloud

---

## 🚀 Tecnologías

- **Framework**: Spring Boot 3.2.0
- **Java**: 17
- **Build Tool**: Maven
- **Base de Datos**: Oracle Cloud
- **ORM**: Spring Data JPA + Hibernate
- **Documentación**: SpringDoc OpenAPI
- **Validaciones**: Bean Validation

---

## 📁 Estructura del Proyecto

```
microservicio-resultados/
├── src/
│   ├── main/
│   │   ├── java/.../resultados/
│   │   │   ├── config/          # Configuraciones
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── exception/      # Excepciones personalizadas
│   │   │   ├── model/          # Entidades JPA
│   │   │   ├── repository/     # Repositorios JPA
│   │   │   └── service/        # Lógica de negocio
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-cloud.yml
│   └── test/
│       └── java/                # Tests unitarios
├── pom.xml
└── README.md
```

---

## 🔧 Instalación

### 1. Configurar Base de Datos

Editar `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@laboratoriosdb_high?TNS_ADMIN=/path/to/wallet
    username: ADMIN
    password: YourPassword
```

### 2. Compilar y Ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

Servicio disponible en: **http://localhost:8083**

---

## 📚 API Endpoints

### **Resultados**

#### POST /api/resultados
Crear un nuevo resultado.

**Request**:
```json
{
  "idCita": 1,
  "idTipoAnalisis": 1,
  "valores": "{\"hemoglobina\": 14.5, \"leucocitos\": 7500, \"plaquetas\": 250000}",
  "observaciones": "Valores dentro del rango normal",
  "estado": "COMPLETADO"
}
```

**Response**:
```json
{
  "traceId": "abc-123",
  "code": "SUCCESS",
  "message": "Resultado creado exitosamente",
  "data": {
    "idResultado": 1,
    "idCita": 1,
    "idTipoAnalisis": 1,
    "fechaResultado": "2025-12-12T10:30:00",
    "valores": "{\"hemoglobina\": 14.5, \"leucocitos\": 7500, \"plaquetas\": 250000}",
    "observaciones": "Valores dentro del rango normal",
    "estado": "COMPLETADO"
  }
}
```

#### GET /api/resultados
Listar todos los resultados.

**Query Parameters**:
- `estado` - Filtrar por estado (PENDIENTE, COMPLETADO, REVISADO)
- `idCita` - Filtrar por cita
- `idTipoAnalisis` - Filtrar por tipo de análisis

#### GET /api/resultados/{id}
Obtener un resultado por ID.

#### GET /api/resultados/cita/{idCita}
Obtener resultados de una cita específica.

#### PUT /api/resultados/{id}
Actualizar un resultado.

**Request**:
```json
{
  "valores": "{\"hemoglobina\": 15.0, \"leucocitos\": 8000, \"plaquetas\": 260000}",
  "observaciones": "Valores actualizados después de revisión",
  "estado": "REVISADO"
}
```

#### DELETE /api/resultados/{id}
Eliminar un resultado.

---

### **Estados de Resultados**

| Estado | Descripción |
|--------|-------------|
| `PENDIENTE` | Resultado en espera de procesamiento |
| `COMPLETADO` | Resultado procesado, listo para revisión |
| `REVISADO` | Resultado revisado por profesional médico |

---

### **Estructura de Valores JSON**

Los resultados se almacenan en formato JSON flexible:

```json
{
  "hemoglobina": 14.5,
  "leucocitos": 7500,
  "plaquetas": 250000,
  "glucosa": 95,
  "colesterol": 180
}
```

Permite almacenar cualquier tipo de análisis con diferentes parámetros.

---

## 📖 Documentación API

**Swagger UI**: http://localhost:8083/swagger-ui.html  
**OpenAPI JSON**: http://localhost:8083/v3/api-docs

---

## 🧪 Testing

### Ejecutar Tests

```bash
# Todos los tests
mvn test

# Con reporte de cobertura JaCoCo
mvn test jacoco:report

# Ver reporte
open target/site/jacoco/index.html
```

### Tests Implementados ✅

- **ResultadoServiceTest**: 15 tests
  - CRUD completo de resultados
  - Validaciones de negocio (cita duplicada, estado válido)
  - Gestión de estados de resultados
  - Filtros por laboratorista y estado
  
- **ResultadoControllerTest**: 13 tests
  - Endpoints REST (POST, GET, PUT, DELETE, PATCH)
  - Validaciones HTTP (400, 404, 201, 200)
  - Manejo de excepciones
  - Health check

**Total:** 28 casos de prueba | **Cobertura:** ≥ 80%

---

## 🔄 Integración con Otros Microservicios

### Microservicio de Laboratorios (8082)
- Validación de idCita al crear resultados
- Validación de idTipoAnalisis
- Relación con CITAS y TIPOS_ANALISIS

### Microservicio de Usuarios (8081)
- Verificación de permisos para consultar resultados
- Solo el paciente propietario o médico puede ver resultados

---

## 📊 Casos de Uso

### 1. Crear Resultado de Análisis
```bash
curl -X POST http://localhost:8083/api/resultados \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "idCita": 1,
    "idTipoAnalisis": 1,
    "valores": "{\"parametro1\": \"valor1\"}",
    "estado": "COMPLETADO"
  }'
```

### 2. Consultar Resultados de un Paciente
```bash
curl -X GET "http://localhost:8083/api/resultados/cita/1" \
  -H "Authorization: Bearer {token}"
```

### 3. Actualizar Estado de Resultado
```bash
curl -X PUT http://localhost:8083/api/resultados/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "estado": "REVISADO",
    "observaciones": "Revisado por Dr. García"
  }'
```

---

## 🔐 Seguridad

### Permisos por Rol

| Rol | Crear | Leer | Actualizar | Eliminar |
|-----|-------|------|------------|----------|
| ADMINISTRADOR | ✅ | ✅ | ✅ | ✅ |
| MEDICO | ✅ | ✅ | ✅ | ❌ |
| LABORATORISTA | ✅ | ✅ | ✅ | ❌ |
| PACIENTE | ❌ | ✅* | ❌ | ❌ |

*Solo sus propios resultados

---

## 🐛 Troubleshooting

```bash
# Puerto en uso
lsof -ti:8083 | xargs kill -9

# Verificar health
curl http://localhost:8083/actuator/health

# Logs
tail -f logs/resultados.log
```

---

## 📝 Ejemplo de Flujo Completo

1. **Paciente agenda cita** (microservicio-laboratorios)
2. **Laboratorio realiza análisis**
3. **Laboratorista carga resultado** (este microservicio)
4. **Médico revisa resultado**
5. **Paciente consulta resultado**

---

## 📞 Soporte

Ver [README principal](../README.md) o [CONTRIBUTING.md](../CONTRIBUTING.md)

---

**Desarrollado por el Equipo de Desarrollo - DUOC UC**

