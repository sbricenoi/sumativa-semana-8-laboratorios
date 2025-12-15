# 🔧 Scripts CI/CD

Scripts de automatización para compilación, testing y análisis de calidad.

## 📋 Scripts Disponibles

### `run-all.sh`
Inicia todos los microservicios y el frontend en modo desarrollo.

```bash
./run-all.sh
```

**Servicios iniciados:**
- Microservicio Usuarios (8081)
- Microservicio Laboratorios (8082)
- Microservicio Resultados (8083)
- Frontend Angular (4200)

**Logs:** Se guardan en `/logs/*.log`

---

### `stop-all.sh`
Detiene todos los servicios en ejecución.

```bash
./stop-all.sh
```

---

### `build-backend.sh`
Compila todos los microservicios Spring Boot.

```bash
./build-backend.sh
```

**Output:** JARs en `*/target/*.jar`

---

### `build-frontend.sh`
Compila el frontend Angular para producción.

```bash
./build-frontend.sh
```

**Output:** Build en `frontend-laboratorios/dist/`

---

### `test-backend.sh`
Ejecuta tests unitarios de todos los microservicios con JaCoCo.

```bash
./test-backend.sh
```

**Reportes:** `*/target/site/jacoco/index.html`

---

### `test-frontend.sh`
Ejecuta tests unitarios del frontend con cobertura.

```bash
./test-frontend.sh
```

**Reporte:** `frontend-laboratorios/coverage/index.html`

---

### `sonar-run.sh`
Ejecuta análisis de SonarQube en todo el proyecto.

```bash
./sonar-run.sh
```

**Requisitos:**
- SonarQube corriendo en http://localhost:9000
- Token de autenticación configurado

**Iniciar SonarQube:**
```bash
docker run -d -p 9000:9000 sonarqube:latest
```

## 🔄 Flujo de Trabajo Recomendado

### Desarrollo Local
```bash
# 1. Compilar todo
./build-backend.sh
./build-frontend.sh

# 2. Ejecutar tests
./test-backend.sh
./test-frontend.sh

# 3. Iniciar servicios
./run-all.sh

# 4. Cuando termines
./stop-all.sh
```

### Análisis de Calidad
```bash
# 1. Ejecutar tests con cobertura
./test-backend.sh
./test-frontend.sh

# 2. Análisis SonarQube
./sonar-run.sh

# 3. Revisar resultados
open http://localhost:9000
```

## 📊 Métricas de Calidad

Los scripts están configurados para:
- ✅ Cobertura mínima: 80%
- ✅ Análisis estático de código
- ✅ Detección de code smells
- ✅ Verificación de vulnerabilidades

## 🐛 Troubleshooting

### Error: Puerto en uso
```bash
# Liberar puertos manualmente
lsof -ti:8081 | xargs kill -9
lsof -ti:8082 | xargs kill -9
lsof -ti:8083 | xargs kill -9
lsof -ti:4200 | xargs kill -9
```

### Error: Maven no encontrado
```bash
# Instalar Maven
brew install maven
```

### Error: npm no encontrado
```bash
# Instalar Node.js y npm
brew install node
```

## 📝 Notas

- Todos los scripts son compatibles con macOS y Linux
- Requieren permisos de ejecución (`chmod +x *.sh`)
- Los logs se guardan automáticamente en `/logs/`

