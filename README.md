# 🏥 Sistema de Gestión de Laboratorios Clínicos

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17-red)](https://angular.io/)
[![Oracle](https://img.shields.io/badge/Oracle-Cloud-red)](https://www.oracle.com/cloud/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## 📋 Descripción

Sistema integral de gestión de laboratorios clínicos desarrollado con arquitectura de microservicios. Permite la gestión de usuarios, laboratorios, análisis y resultados médicos con autenticación JWT y roles diferenciados.

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend Angular 17                       │
│                   (Puerto 4200)                              │
└─────────────┬───────────────┬──────────────┬────────────────┘
              │               │              │
              ▼               ▼              ▼
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│  Microservicio   │ │  Microservicio   │ │  Microservicio   │
│    Usuarios      │ │  Laboratorios    │ │   Resultados     │
│  (Puerto 8081)   │ │  (Puerto 8082)   │ │  (Puerto 8083)   │
│                  │ │                  │ │                  │
│  • Autenticación │ │  • Gestión Labs  │ │  • Resultados    │
│  • JWT Tokens    │ │  • Tipos Análisis│ │  • Análisis      │
│  • Roles         │ │  • Citas         │ │  • Reportes      │
└────────┬─────────┘ └────────┬─────────┘ └────────┬─────────┘
         │                    │                     │
         └────────────────────┼─────────────────────┘
                              ▼
                   ┌──────────────────────┐
                   │   Oracle Database    │
                   │   (Oracle Cloud)     │
                   └──────────────────────┘
```

## ✨ Características Principales

### 🔐 Seguridad
- ✅ Autenticación JWT
- ✅ Roles diferenciados (Admin, Paciente, Laboratorista, Médico)
- ✅ Guards de protección de rutas
- ✅ Interceptores HTTP

### 🎯 Funcionalidades
- ✅ Gestión completa de usuarios
- ✅ CRUD de laboratorios
- ✅ Gestión de tipos de análisis
- ✅ Registro y seguimiento de citas
- ✅ Carga y consulta de resultados
- ✅ Dashboard personalizado por rol

### 📊 Calidad de Código
- ✅ Swagger/OpenAPI en todos los microservicios
- ✅ Tests unitarios (JUnit + Mockito)
- ✅ Cobertura de código con JaCoCo
- ✅ Análisis con SonarQube
- ✅ Scripts de CI/CD

## 🚀 Inicio Rápido

### Prerrequisitos

- **Java 21** (o superior)
- **Node.js 18+** y npm
- **Maven 3.8+**
- **Oracle Database** (Cloud o local)
- **Git**

### Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd "Sumativa 2 Semana 5"
   ```

2. **Configurar Base de Datos**
   - Crear usuario en Oracle Cloud
   - Descargar Wallet de conexión
   - Actualizar `application.yml` en cada microservicio
   - Ejecutar scripts SQL en `/db/init.sql`

3. **Compilar Backend**
   ```bash
   ./ci/build-backend.sh
   ```

4. **Compilar Frontend**
   ```bash
   ./ci/build-frontend.sh
   ```

5. **Iniciar todos los servicios**
   ```bash
   ./ci/run-all.sh
   ```

6. **Acceder a la aplicación**
   - Frontend: http://localhost:4200
   - Swagger Usuarios: http://localhost:8081/swagger-ui.html
   - Swagger Laboratorios: http://localhost:8082/swagger-ui.html
   - Swagger Resultados: http://localhost:8083/swagger-ui.html

## 📁 Estructura del Proyecto

```
/
├── microservicio-usuarios/      # Microservicio de autenticación y usuarios
├── microservicio-laboratorios/  # Microservicio de laboratorios y análisis
├── microservicio-resultados/    # Microservicio de resultados
├── frontend-laboratorios/       # Aplicación Angular
├── db/                          # Scripts SQL
├── ci/                          # Scripts de CI/CD
├── docs/                        # Documentación
├── postman/                     # Colección Postman
├── docker-compose.yml           # Orquestación Docker
├── sonar-project.properties     # Configuración SonarQube
└── README.md                    # Este archivo
```

## 🧪 Testing

### Backend
```bash
# Ejecutar todos los tests
./ci/test-backend.sh

# Ver reportes de cobertura
open microservicio-usuarios/target/site/jacoco/index.html
```

### Frontend
```bash
# Ejecutar tests con cobertura
./ci/test-frontend.sh

# Ver reporte de cobertura
open frontend-laboratorios/coverage/index.html
```

### SonarQube
```bash
# Iniciar SonarQube (Docker)
docker run -d -p 9000:9000 sonarqube:latest

# Ejecutar análisis
./ci/sonar-run.sh

# Ver resultados
open http://localhost:9000
```

## 📖 Documentación

- [Arquitectura del Sistema](docs/arquitectura.md)
- [Guía de Instalación](docs/INSTRUCCIONES.txt)
- [Credenciales de Prueba](docs/CREDENCIALES.txt)
- [Checklist Final](docs/CHECKLIST_FINAL.md)
- [Auditoría y Plan](AUDITORIA_Y_PLAN.md)

### READMEs por Módulo
- [Microservicio Usuarios](microservicio-usuarios/README.md)
- [Microservicio Laboratorios](microservicio-laboratorios/README.md)
- [Microservicio Resultados](microservicio-resultados/README.md)
- [Frontend Angular](frontend-laboratorios/README.md)
- [Base de Datos](db/README.md)
- [CI/CD](ci/README.md)

## 🔧 Scripts Disponibles

| Script | Descripción |
|--------|-------------|
| `./ci/run-all.sh` | Inicia todos los servicios |
| `./ci/stop-all.sh` | Detiene todos los servicios |
| `./ci/build-backend.sh` | Compila microservicios |
| `./ci/build-frontend.sh` | Compila frontend |
| `./ci/test-backend.sh` | Ejecuta tests backend |
| `./ci/test-frontend.sh` | Ejecuta tests frontend |
| `./ci/sonar-run.sh` | Análisis SonarQube |

## 👥 Usuarios de Prueba

| Rol | Email | Password |
|-----|-------|----------|
| Administrador | admin@lab.cl | Admin123* |
| Médico | medico@lab.cl | Medico123* |
| Laboratorista | lab@lab.cl | Lab123* |
| Paciente | paciente@lab.cl | Paciente123* |

## 📊 Endpoints Principales

### Microservicio Usuarios (8081)
- `POST /api/usuarios/registro` - Registrar usuario
- `POST /api/usuarios/login` - Iniciar sesión (devuelve JWT)
- `GET /api/usuarios` - Listar usuarios (requiere auth)
- `PUT /api/usuarios/{id}` - Actualizar usuario

### Microservicio Laboratorios (8082)
- `GET /api/laboratorios` - Listar laboratorios
- `POST /api/laboratorios` - Crear laboratorio
- `GET /api/tipos-analisis` - Listar tipos de análisis
- `POST /api/citas` - Agendar cita

### Microservicio Resultados (8083)
- `GET /api/resultados` - Listar resultados
- `POST /api/resultados` - Crear resultado
- `GET /api/resultados/{id}` - Obtener resultado por ID
- `PUT /api/resultados/{id}` - Actualizar resultado

## 🛠️ Tecnologías Utilizadas

### Backend
- Spring Boot 3.2.0
- Spring Security + JWT
- Spring Data JPA
- Oracle JDBC Driver
- Lombok
- SpringDoc OpenAPI (Swagger)
- JUnit 5 + Mockito
- JaCoCo

### Frontend
- Angular 17
- TypeScript 5.4
- Bootstrap 5.3
- RxJS 7.8
- Karma + Jasmine

### Base de Datos
- Oracle Database (Cloud)
- SQL Scripts

### DevOps
- Maven 3.8+
- npm
- Docker
- SonarQube
- Git

## 🐛 Troubleshooting

### Error de conexión a Oracle
```bash
# Verificar wallet configurado
ls ~/oracle_wallet

# Verificar variables de entorno en application.yml
```

### Puerto en uso
```bash
# Liberar puerto
lsof -ti:8081 | xargs kill -9
```

### Frontend no compila
```bash
cd frontend-laboratorios
rm -rf node_modules package-lock.json
npm install
```

## 📝 Licencia

Este proyecto está licenciado bajo Apache License 2.0 - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

**Equipo de Desarrollo - DUOC UC**  
Asignatura: Full Stack III  
Evaluación: Sumativa 2 Semana 5

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📞 Soporte

Para soporte, por favor abre un issue en el repositorio o contacta al equipo de desarrollo.

---

**⭐ Si este proyecto te fue útil, considera darle una estrella!**

