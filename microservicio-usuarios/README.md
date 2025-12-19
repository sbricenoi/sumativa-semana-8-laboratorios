# 🔐 Microservicio de Gestión de Usuarios

**Versión**: 1.0.0  
**Puerto**: 8081  
**Base de Datos**: Oracle Cloud

---

## 📋 Descripción

Microservicio encargado de la gestión de usuarios, autenticación JWT y control de acceso por roles en el Sistema de Gestión de Laboratorios Clínicos.

---

## ✨ Características

- ✅ Autenticación con JWT (JSON Web Tokens)
- ✅ Gestión completa de usuarios (CRUD)
- ✅ Sistema de roles (ADMINISTRADOR, PACIENTE, MEDICO, LABORATORISTA)
- ✅ Encriptación de contraseñas con BCrypt
- ✅ Recuperación y cambio de contraseña
- ✅ Activación/desactivación de usuarios
- ✅ Búsqueda por nombre, apellido y rol
- ✅ Documentación Swagger/OpenAPI
- ✅ Tests unitarios con JUnit + Mockito
- ✅ Cobertura de código con JaCoCo

---

## 🚀 Tecnologías

- **Framework**: Spring Boot 3.2.0
- **Java**: 21
- **Build Tool**: Maven
- **Base de Datos**: Oracle Cloud
- **Seguridad**: Spring Security + JWT
- **Documentación**: SpringDoc OpenAPI
- **Testing**: JUnit 5, Mockito, JaCoCo
- **ORM**: Spring Data JPA + Hibernate
- **Validaciones**: Bean Validation

---

## 📁 Estructura del Proyecto

```
microservicio-usuarios/
├── src/
│   ├── main/
│   │   ├── java/.../usuarios/
│   │   │   ├── config/          # Configuraciones (Security, CORS, Oracle)
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── exception/      # Excepciones personalizadas
│   │   │   ├── model/          # Entidades JPA
│   │   │   ├── repository/     # Repositorios JPA
│   │   │   ├── security/       # JWT y autenticación
│   │   │   └── service/        # Lógica de negocio
│   │   └── resources/
│   │       ├── application.yml              # Configuración principal
│   │       └── application-cloud.yml        # Configuración Oracle Cloud
│   └── test/
│       └── java/.../usuarios/
│           ├── controller/      # Tests de controladores
│           ├── service/        # Tests de servicios
│           └── security/       # Tests de seguridad
├── pom.xml                     # Dependencias Maven
└── README.md                   # Este archivo
```

---

## 🔧 Instalación y Configuración

### Prerrequisitos

- Java 21 (JDK)
- Maven 3.8+
- Oracle Cloud Database (con Wallet configurado)
- Git

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd "Sumativa 2 Semana 5/microservicio-usuarios"
```

### 2. Configurar Base de Datos

Editar `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@laboratoriosdb_high?TNS_ADMIN=/path/to/wallet
    username: ADMIN
    password: YourPassword
    driver-class-name: oracle.jdbc.OracleDriver
```

### 3. Compilar el Proyecto

```bash
mvn clean install
```

### 4. Ejecutar Tests

```bash
mvn test
```

### 5. Ejecutar el Microservicio

```bash
mvn spring-boot:run
```

El servicio estará disponible en: **http://localhost:8081**

---

## 📚 API Endpoints

### **Autenticación**

#### POST /api/usuarios/login
Iniciar sesión y obtener token JWT.

**Request**:
```json
{
  "email": "admin@lab.cl",
  "password": "Admin123*"
}
```

**Response**:
```json
{
  "traceId": "abc-123",
  "code": "SUCCESS",
  "message": "Inicio de sesión exitoso",
  "data": {
    "idUsuario": 1,
    "nombre": "Admin",
    "apellido": "Sistema",
    "email": "admin@lab.cl",
    "rol": "ADMINISTRADOR",
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "mensaje": "Login exitoso"
  }
}
```

#### POST /api/usuarios/registro
Registrar un nuevo usuario.

**Request**:
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "password": "Password123*",
  "rol": "PACIENTE"
}
```

---

### **Gestión de Usuarios** (Requiere JWT)

#### GET /api/usuarios
Listar todos los usuarios.

**Headers**: `Authorization: Bearer {token}`

#### GET /api/usuarios/{id}
Obtener un usuario por ID.

#### GET /api/usuarios/rol/{rol}
Listar usuarios por rol (ADMINISTRADOR, PACIENTE, MEDICO, LABORATORISTA).

#### GET /api/usuarios/buscar?texto={texto}
Buscar usuarios por nombre o apellido.

#### PUT /api/usuarios/{id}
Actualizar un usuario.

**Request**:
```json
{
  "nombre": "Juan Carlos",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "rol": "MEDICO",
  "activo": 1
}
```

#### DELETE /api/usuarios/{id}
Desactivar un usuario (soft delete).

#### DELETE /api/usuarios/{id}/permanente
Eliminar un usuario permanentemente (solo ADMINISTRADOR).

---

### **Gestión de Contraseñas**

#### POST /api/usuarios/recuperar-password
Solicitar recuperación de contraseña.

**Request**:
```json
{
  "email": "usuario@example.com"
}
```

#### PUT /api/usuarios/{id}/cambiar-password
Cambiar contraseña del usuario.

**Request**:
```json
{
  "passwordActual": "OldPassword123*",
  "passwordNueva": "NewPassword123*",
  "passwordConfirmacion": "NewPassword123*"
}
```

---

### **Administración** (Solo ADMINISTRADOR)

#### PATCH /api/usuarios/{id}/activar
Activar un usuario inactivo.

#### PATCH /api/usuarios/{id}/desactivar
Desactivar un usuario.

---

### **Health Check**

#### GET /api/usuarios/health
Verificar estado del microservicio.

**Response**:
```json
{
  "code": "SUCCESS",
  "message": "Microservicio de Usuarios funcionando correctamente",
  "data": "OK"
}
```

---

## 🔐 Seguridad

### JWT (JSON Web Tokens)

- **Algoritmo**: HS512
- **Expiración**: 24 horas
- **Claims**: email, rol, idUsuario
- **Header**: `Authorization: Bearer {token}`

### Roles Disponibles

| Rol | Descripción |
|-----|-------------|
| `ADMINISTRADOR` | Acceso completo al sistema |
| `MEDICO` | Gestión de análisis y resultados |
| `LABORATORISTA` | Gestión de laboratorios y análisis |
| `PACIENTE` | Consulta de resultados propios |

### Endpoints Públicos
- POST /api/usuarios/login
- POST /api/usuarios/registro
- POST /api/usuarios/recuperar-password
- GET /api/usuarios/health

### Endpoints Protegidos
Todos los demás requieren token JWT válido en el header `Authorization`.

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

### Tests Implementados

- **UsuarioServiceTest**: 20 tests
  - Registro de usuarios
  - Login y autenticación
  - Gestión CRUD
  - Validaciones de negocio

- **UsuarioControllerTest**: 10 tests
  - Endpoints REST
  - Validaciones de entrada
  - Manejo de errores

- **JwtUtilTest**: 10 tests
  - Generación de tokens
  - Validación de tokens
  - Extracción de claims

### Cobertura de Código

**Objetivo**: ≥ 80%

```bash
# Generar reporte
mvn clean test jacoco:report

# Ubicación del reporte
target/site/jacoco/index.html
```

---

## 📖 Documentación API

### Swagger UI

Acceder a la documentación interactiva:

**URL**: http://localhost:8081/swagger-ui.html

### OpenAPI JSON

**URL**: http://localhost:8081/v3/api-docs

---

## 🐛 Troubleshooting

### Error: No se puede conectar a Oracle

```bash
# Verificar wallet configurado
ls ~/oracle_wallet

# Verificar variable TNS_ADMIN en application.yml
```

### Error: Puerto 8081 en uso

```bash
# Liberar puerto
lsof -ti:8081 | xargs kill -9

# O cambiar puerto en application.yml
server:
  port: 8082
```

### Error: Tests fallan

```bash
# Limpiar y recompilar
mvn clean install -DskipTests

# Ejecutar tests específicos
mvn test -Dtest=UsuarioServiceTest
```

---

## 📊 Configuración de SonarQube

```bash
# Ejecutar análisis
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=laboratorios-usuarios \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=YOUR_TOKEN
```

---

## 🔄 Actualización de Dependencias

```bash
# Ver dependencias desactualizadas
mvn versions:display-dependency-updates

# Actualizar versiones
mvn versions:use-latest-releases
```

---

## 📝 Variables de Entorno

| Variable | Descripción | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Puerto del servicio | 8081 |
| `DB_URL` | URL de Oracle | jdbc:oracle:thin:@... |
| `DB_USERNAME` | Usuario de BD | ADMIN |
| `DB_PASSWORD` | Contraseña de BD | - |
| `JWT_SECRET` | Secret para JWT | (ver application.yml) |
| `JWT_EXPIRATION` | Tiempo expiración JWT (ms) | 86400000 (24h) |

---

## 🤝 Contribuir

Ver [CONTRIBUTING.md](../CONTRIBUTING.md) para guías de contribución.

---

## 📞 Soporte

Para problemas o preguntas:
- Abrir un issue en el repositorio
- Contactar al equipo de desarrollo
- Revisar la documentación en `/docs`

---

## 📄 Licencia

Apache License 2.0

---

**Desarrollado por el Equipo de Desarrollo - DUOC UC**  
*Full Stack III - Sumativa 2 Semana 5*


