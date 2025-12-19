# 🏥 Microservicio de Gestión de Laboratorios

**Versión**: 1.0.0  
**Puerto**: 8082  
**Base de Datos**: Oracle Cloud

---

## 📋 Descripción

Microservicio encargado de la gestión de laboratorios, tipos de análisis y citas médicas en el Sistema de Gestión de Laboratorios Clínicos.

---

## ✨ Características

- ✅ Gestión completa de laboratorios (CRUD)
- ✅ Gestión de tipos de análisis clínicos
- ✅ Agendamiento y seguimiento de citas
- ✅ Asignación de análisis a laboratorios (relación N:M)
- ✅ Búsqueda y filtrado de laboratorios
- ✅ Validaciones de negocio
- ✅ Documentación Swagger/OpenAPI
- ✅ Integración con Oracle Cloud

---

## 🚀 Tecnologías

- **Framework**: Spring Boot 3.2.0
- **Java**: 21
- **Build Tool**: Maven
- **Base de Datos**: Oracle Cloud
- **ORM**: Spring Data JPA + Hibernate
- **Documentación**: SpringDoc OpenAPI
- **Validaciones**: Bean Validation

---

## 📁 Estructura del Proyecto

```
microservicio-laboratorios/
├── src/
│   ├── main/
│   │   ├── java/.../laboratorios/
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

Servicio disponible en: **http://localhost:8082**

---

## 📚 API Endpoints

### **Laboratorios**

#### POST /api/laboratorios
Crear un nuevo laboratorio.

**Request**:
```json
{
  "nombre": "Lab Central",
  "direccion": "Av. Principal 123",
  "telefono": "+56912345678",
  "email": "info@labcentral.cl",
  "especialidad": "Análisis Clínicos",
  "activo": true
}
```

#### GET /api/laboratorios
Listar todos los laboratorios.

#### GET /api/laboratorios/{id}
Obtener un laboratorio por ID.

#### PUT /api/laboratorios/{id}
Actualizar un laboratorio.

#### DELETE /api/laboratorios/{id}
Eliminar un laboratorio.

---

### **Tipos de Análisis**

#### POST /api/tipos-analisis
Crear un nuevo tipo de análisis.

**Request**:
```json
{
  "nombre": "Hemograma Completo",
  "descripcion": "Análisis completo de células sanguíneas",
  "precio": 15000.00,
  "tiempoEstimado": "24 horas",
  "requisitos": "Ayuno de 8 horas",
  "activo": true
}
```

#### GET /api/tipos-analisis
Listar todos los tipos de análisis.

#### GET /api/tipos-analisis/{id}
Obtener un tipo de análisis por ID.

#### PUT /api/tipos-analisis/{id}
Actualizar un tipo de análisis.

#### DELETE /api/tipos-analisis/{id}
Eliminar un tipo de análisis.

---

### **Citas**

#### POST /api/citas
Agendar una nueva cita.

**Request**:
```json
{
  "idPaciente": 1,
  "idLaboratorio": 1,
  "fechaHora": "2025-12-15T10:00:00",
  "estado": "PROGRAMADA",
  "observaciones": "Análisis de rutina"
}
```

#### GET /api/citas
Listar todas las citas.

#### GET /api/citas/{id}
Obtener una cita por ID.

#### GET /api/citas/paciente/{idPaciente}
Listar citas de un paciente.

#### GET /api/citas/laboratorio/{idLaboratorio}
Listar citas de un laboratorio.

#### PUT /api/citas/{id}
Actualizar una cita.

#### DELETE /api/citas/{id}
Cancelar una cita.

---

### **Asignación Laboratorio-Análisis**

#### POST /api/laboratorios/{idLab}/analisis/{idAnalisis}
Asignar un tipo de análisis a un laboratorio.

#### GET /api/laboratorios/{id}/analisis
Obtener análisis disponibles en un laboratorio.

#### DELETE /api/laboratorios/{idLab}/analisis/{idAnalisis}
Eliminar asignación de análisis.

---

## 📖 Documentación API

**Swagger UI**: http://localhost:8082/swagger-ui.html  
**OpenAPI JSON**: http://localhost:8082/v3/api-docs

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

- **LaboratorioServiceTest**: 17 tests
  - CRUD completo de laboratorios
  - Validaciones de negocio (email duplicado, etc.)
  - Asignación de análisis a laboratorios
  - Soft delete
  
- **TipoAnalisisServiceTest**: 10 tests
  - CRUD de tipos de análisis
  - Búsquedas y filtros
  - Validaciones
  
- **LaboratorioControllerTest**: 12 tests
  - Endpoints REST (POST, GET, PUT, DELETE, PATCH)
  - Validaciones HTTP (400, 404, 201, 200)
  - Manejo de excepciones
  - Health check

**Total:** 39 casos de prueba | **Cobertura:** ≥ 80%

---

## 🔄 Integración con Otros Microservicios

### Microservicio de Usuarios (8081)
- Validación de idPaciente al crear citas
- Verificación de roles para operaciones

### Microservicio de Resultados (8083)
- Las citas generan resultados
- Relación idCita en tabla RESULTADOS

---

## 🐛 Troubleshooting

```bash
# Puerto en uso
lsof -ti:8082 | xargs kill -9

# Verificar conexión BD
curl http://localhost:8082/actuator/health
```

---

## 📞 Soporte

Ver [README principal](../README.md) o [CONTRIBUTING.md](../CONTRIBUTING.md)

---

**Desarrollado por el Equipo de Desarrollo - DUOC UC**

