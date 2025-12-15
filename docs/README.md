# 🏥 Sistema de Gestión de Laboratorios Clínicos

**Proyecto:** Sumativa 2 - Semana 5  
**Asignatura:** Desarrollo Full Stack III (DSY2205)  
**Fecha:** Noviembre 2025  
**Versión:** 2.0.0

---

## 📋 Descripción del Proyecto

Sistema completo de gestión de laboratorios clínicos desarrollado con arquitectura de microservicios (Backend) y Angular (Frontend). Permite la gestión de usuarios, laboratorios, citas y resultados de análisis clínicos.

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│               FRONTEND ANGULAR (Puerto 4200)            │
│                                                          │
│  • Login y Registro con validaciones                   │
│  • Dashboard por rol                                    │
│  • Gestión de perfil                                    │
│  • Responsive Design (Mobile, Tablet, Desktop)         │
└─────────────────────────────────────────────────────────┘
                              │
                              │ (Independiente - No conectado)
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│Microservicio │    │Microservicio │    │Microservicio │
│  Usuarios    │    │ Laboratorios │    │ Resultados   │
│ (Puerto 8081)│    │ (Puerto 8082)│    │ (Puerto 8083)│
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                   │
       └───────────────────┼───────────────────┘
                           │
                           ▼
                  ┌────────────────┐
                  │  Oracle DB     │
                  │ (Puerto 1521)  │
                  └────────────────┘
```

---

## ✅ Componentes Completados

### 🟢 Backend (100%)

#### Microservicio de Usuarios (Puerto 8081)
- ✅ CRUD completo de usuarios
- ✅ Login y registro
- ✅ Gestión de roles (ADMINISTRADOR, PACIENTE, LABORATORISTA, MEDICO)
- ✅ Contraseñas hasheadas con BCrypt
- ✅ 8 endpoints REST

#### Microservicio de Laboratorios (Puerto 8082)
- ✅ CRUD de laboratorios
- ✅ CRUD de tipos de análisis
- ✅ Gestión de citas
- ✅ Asignación análisis-laboratorio
- ✅ 15+ endpoints REST

#### Microservicio de Resultados (Puerto 8083) ⭐ NUEVO
- ✅ CRUD de resultados de análisis
- ✅ Gestión de estados (PENDIENTE, EN_PROCESO, COMPLETADO, REVISADO)
- ✅ Filtros por cita, paciente, laboratorista
- ✅ 10 endpoints REST

#### Base de Datos Oracle
- ✅ 7 tablas con relaciones
- ✅ Secuencias e índices optimizados
- ✅ Datos de prueba (5 usuarios, 4 laboratorios, 6 análisis, 3 citas)
- ✅ Constraints y validaciones

### 🟢 Frontend Angular (100%) ⭐ NUEVO

#### Estructura
- ✅ Angular 17 con Standalone Components
- ✅ Bootstrap 5 + Bootstrap Icons
- ✅ Arquitectura MVC/MVVM
- ✅ Responsive (Mobile, Tablet, Desktop)

#### Servicios
- ✅ `AuthService` - Autenticación y autorización
- ✅ `MockDataService` - Datos simulados en LocalStorage
- ✅ Guards (authGuard, roleGuard)
- ✅ Validators personalizados para contraseñas

#### Componentes (5 Páginas Obligatorias)
1. ✅ **Login** - Formulario con validaciones
2. ✅ **Registro** - Con 6 validaciones de contraseña:
   - Longitud mínima (8 caracteres)
   - Longitud máxima (20 caracteres)
   - Al menos 1 número
   - Al menos 1 carácter especial
   - Al menos 1 mayúscula
   - Al menos 1 minúscula
   - ✅ Indicador visual de fortaleza
3. ✅ **Recuperar Contraseña** - Flujo de 3 pasos
4. ✅ **Editar Perfil** - Con tabs (Info Personal / Cambiar Password)
5. ✅ **Main Layout** - Navbar + Footer responsive

#### Páginas Internas
- ✅ **Dashboard** - Diferenciado por rol (PACIENTE, MEDICO, LABORATORISTA, ADMIN)
- ✅ **Estadísticas** - Cards con información relevante

#### Características
- ✅ Datos simulados con LocalStorage
- ✅ Routing configurado con guards
- ✅ Validaciones en tiempo real
- ✅ Mensajes de error/éxito amigables
- ✅ Tema médico profesional

### 🟢 Docker (100%)

#### Contenedorización
- ✅ 3 Dockerfiles para microservicios Java
- ✅ 1 Dockerfile para Frontend Angular
- ✅ Multi-stage builds optimizados
- ✅ Health checks configurados

#### Docker Compose
- ✅ 5 servicios orquestados:
  - oracle-db
  - microservicio-usuarios
  - microservicio-laboratorios
  - microservicio-resultados
  - frontend-angular
- ✅ Red compartida
- ✅ Dependencias configuradas
- ✅ Variables de entorno

### 🟢 Arquetipo Maven (100%)

- ✅ Plantilla completa para nuevos microservicios
- ✅ 9 archivos Java template
- ✅ Configuraciones (application.yml, Dockerfile)
- ✅ README documentado con ejemplos

### 🟢 Documentación (100%)

- ✅ `docs/arquitectura.md` - Arquitectura detallada del backend
- ✅ `docs/plan-de-trabajo.md` - Plan completo de desarrollo
- ✅ `frontend-laboratorios/ESTRUCTURA_FRONTEND.md` - Guía del frontend
- ✅ `arquetipo-microservicio/README.md` - Uso del arquetipo
- ✅ `PROGRESO.md` - Estado actual del proyecto
- ✅ `README.md` - Este archivo

### 🟢 Postman (100%)

- ✅ Colección completa con 30+ requests
- ✅ Variables configuradas
- ✅ Tests para los 3 microservicios

---

## 🚀 Instalación y Ejecución

### Requisitos Previos

- **Java:** 17 o superior
- **Maven:** 3.6+
- **Node.js:** 18+
- **Docker:** 20+
- **Docker Compose:** 1.29+

### Opción 1: Con Docker (Recomendado)

```bash
# 1. Clonar el repositorio
git clone <tu-repositorio>
cd "Sumativa 2 Semana 5"

# 2. Construir las imágenes
docker-compose build

# 3. Levantar todos los servicios
docker-compose up -d

# 4. Verificar que todo esté corriendo
docker-compose ps

# 5. Ver logs
docker-compose logs -f
```

**URLs de acceso:**
- Frontend: http://localhost:4200
- Microservicio Usuarios: http://localhost:8081
- Microservicio Laboratorios: http://localhost:8082
- Microservicio Resultados: http://localhost:8083
- Oracle DB: localhost:1521 (SID: XE)

### Opción 2: Sin Docker (Desarrollo)

#### Backend

```bash
# Base de datos (requiere Docker)
docker-compose up oracle-db -d

# Microservicio Usuarios
cd microservicio-usuarios
mvn spring-boot:run

# En otra terminal - Microservicio Laboratorios
cd microservicio-laboratorios
mvn spring-boot:run

# En otra terminal - Microservicio Resultados
cd microservicio-resultados
mvn spring-boot:run
```

#### Frontend

```bash
cd frontend-laboratorios
npm install
npm start
# Abre http://localhost:4200
```

---

## 👥 Usuarios de Prueba

| Email | Password | Rol |
|-------|----------|-----|
| admin@lab.cl | cualquiera | ADMINISTRADOR |
| maria@email.cl | cualquiera | PACIENTE |
| carlos@lab.cl | cualquiera | LABORATORISTA |
| ana@hospital.cl | cualquiera | MEDICO |
| pedro@email.cl | cualquiera | PACIENTE |

**Nota:** En modo demo, cualquier contraseña es válida para facilitar las pruebas.

---

## 📊 Estadísticas del Proyecto

### Archivos Creados/Modificados

| Categoría | Cantidad | Detalle |
|-----------|----------|---------|
| **Backend Java** | 39 archivos | 13 x 3 microservicios |
| **Frontend Angular** | 25+ archivos | Components, Services, Models |
| **Docker** | 8 archivos | Dockerfiles, Compose, nginx |
| **Arquetipo** | 15 archivos | Templates completos |
| **SQL** | 1 archivo | 300+ líneas |
| **Documentación** | 6 archivos | Guías y READMEs |
| **Postman** | 1 colección | 30+ requests |

**Total:** ~95 archivos creados/modificados

### Líneas de Código (aproximado)

- **Backend:** ~3,500 líneas Java
- **Frontend:** ~2,000 líneas TypeScript/HTML/CSS
- **Configuración:** ~500 líneas YAML/JSON
- **SQL:** ~300 líneas
- **Documentación:** ~2,000 líneas Markdown

**Total:** ~8,300 líneas de código

---

## 🧪 Pruebas

### Con Postman

```bash
# 1. Importar colección desde /postman/Laboratorios-API.postman_collection.json

# 2. Verificar variables:
BASE_URL_USUARIOS=http://localhost:8081
BASE_URL_LABORATORIOS=http://localhost:8082
BASE_URL_RESULTADOS=http://localhost:8083

# 3. Ejecutar tests en orden:
- Health checks
- Registro de usuario
- Login
- CRUD de laboratorios
- CRUD de análisis
- Crear citas
- Crear resultados
```

### Frontend

1. Abrir http://localhost:4200
2. Probar login con usuarios de prueba
3. Navegar por el dashboard
4. Probar registro con validaciones de contraseña
5. Probar recuperar contraseña (código: 123456)
6. Editar perfil

---

## 📱 Responsive Design

El Frontend está optimizado para 3 tamaños:

- **📱 Mobile:** < 576px
- **📱 Tablet:** 576px - 992px
- **💻 Desktop:** > 992px

Usa el Grid de 12 columnas de Bootstrap para adaptarse automáticamente.

---

## 🎨 Paleta de Colores

- **Primary:** #0066cc (Azul médico)
- **Secondary:** #00a86b (Verde salud)
- **Accent:** #ff6b35 (Naranja alertas)
- **Dark:** #1a1a2e
- **Light:** #f8f9fa

---

## 📦 Estructura del Proyecto

```
Sumativa 2 Semana 5/
├── docs/                           # Documentación
│   ├── arquitectura.md
│   └── plan-de-trabajo.md
├── scripts/                        # Scripts SQL
│   └── init.sql
├── microservicio-usuarios/         # MS Usuarios
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── microservicio-laboratorios/     # MS Laboratorios
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── microservicio-resultados/       # MS Resultados ⭐
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── frontend-laboratorios/          # Frontend Angular ⭐
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── package.json
│   └── src/
│       ├── app/
│       │   ├── core/              (services, guards)
│       │   ├── shared/            (models, validators)
│       │   ├── features/          (componentes por feature)
│       │   └── layouts/           (layout principal)
│       └── styles.scss
├── arquetipo-microservicio/        # Arquetipo Maven ⭐
│   ├── README.md
│   └── src/main/resources/
├── postman/                        # Colección Postman
│   └── Laboratorios-API.postman_collection.json
├── docker-compose.yml              # Orquestación completa
├── PROGRESO.md                     # Estado del proyecto
└── README.md                       # Este archivo
```

---

## ⚙️ Comandos Útiles

### Docker

```bash
# Levantar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f [servicio]

# Detener servicios
docker-compose down

# Reconstruir imágenes
docker-compose build --no-cache

# Eliminar todo (incluyendo volúmenes)
docker-compose down -v
```

### Frontend

```bash
# Desarrollo
npm start

# Build producción
npm run build

# Linter
npm run lint

# Tests
npm test
```

### Backend

```bash
# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run

# Package
mvn clean package

# Tests
mvn test
```

---

## 🐛 Troubleshooting

### Error: Puerto en uso

```bash
# Verificar puertos ocupados
lsof -i :8081
lsof -i :8082
lsof -i :8083
lsof -i :4200

# Cambiar puerto en docker-compose.yml o application.yml
```

### Error: No se conecta a Oracle

```bash
# Verificar que Oracle esté corriendo
docker ps | grep oracle

# Ver logs de Oracle
docker logs oracle-laboratorios

# Esperar a que termine de inicializar (primera vez: 5-10 min)
```

### Error de compilación Java

```bash
# Verificar versión de Java
java -version

# Debe ser Java 17 o superior
# Si tienes Java 21, cambiar a 17:
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

---

## 📚 Tecnologías Utilizadas

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Oracle Database 21c
- Maven 3.9+
- Lombok
- Docker

### Frontend
- Angular 17
- TypeScript 5.2+
- Bootstrap 5.3
- Bootstrap Icons
- RxJS
- SCSS

### DevOps
- Docker & Docker Compose
- Nginx
- Maven Archetypes

---

## 📄 Licencia

Este proyecto es parte de la evaluación académica de DUOC UC - 2025

---

## 👨‍💻 Autor

Desarrollado para la asignatura Desarrollo Full Stack III (DSY2205)  
**Fecha:** Noviembre 2025

---

## 🎯 Próximos Pasos (Futuras Mejoras)

- [ ] Conectar Frontend con Backend (opcional)
- [ ] Implementar Spring Security con JWT
- [ ] Agregar tests unitarios e integración
- [ ] Implementar API Gateway
- [ ] Service Discovery con Eureka
- [ ] Documentación con Swagger/OpenAPI
- [ ] CI/CD con GitHub Actions

---

**¡Proyecto Completado! 🎉**

Para cualquier consulta, revisar la documentación en `/docs/` o el plan de trabajo en `/docs/plan-de-trabajo.md`

