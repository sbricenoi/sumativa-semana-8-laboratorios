#!/bin/bash

###############################################################################
# Script: run-all.sh
# Descripción: Ejecuta todos los microservicios y el frontend
# Uso: ./run-all.sh
###############################################################################

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  Sistema de Gestión de Laboratorios Clínicos            ║${NC}"
echo -e "${GREEN}║  Iniciando todos los servicios...                        ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""

# Directorio raíz del proyecto
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

# Función para verificar si un puerto está en uso
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        echo -e "${YELLOW}⚠️  Puerto $port ya está en uso${NC}"
        return 1
    fi
    return 0
}

# Verificar puertos
echo -e "${YELLOW}📡 Verificando puertos disponibles...${NC}"
check_port 8081 || echo "   Microservicio Usuarios puede estar corriendo"
check_port 8082 || echo "   Microservicio Laboratorios puede estar corriendo"
check_port 8083 || echo "   Microservicio Resultados puede estar corriendo"
check_port 4200 || echo "   Frontend puede estar corriendo"
echo ""

# Iniciar Microservicio de Usuarios
echo -e "${GREEN}🚀 Iniciando Microservicio de Usuarios (Puerto 8081)...${NC}"
cd "$PROJECT_ROOT/microservicio-usuarios"
mvn spring-boot:run > ../logs/usuarios.log 2>&1 &
USUARIOS_PID=$!
echo "   PID: $USUARIOS_PID"
sleep 5

# Iniciar Microservicio de Laboratorios
echo -e "${GREEN}🚀 Iniciando Microservicio de Laboratorios (Puerto 8082)...${NC}"
cd "$PROJECT_ROOT/microservicio-laboratorios"
mvn spring-boot:run > ../logs/laboratorios.log 2>&1 &
LABORATORIOS_PID=$!
echo "   PID: $LABORATORIOS_PID"
sleep 5

# Iniciar Microservicio de Resultados
echo -e "${GREEN}🚀 Iniciando Microservicio de Resultados (Puerto 8083)...${NC}"
cd "$PROJECT_ROOT/microservicio-resultados"
mvn spring-boot:run > ../logs/resultados.log 2>&1 &
RESULTADOS_PID=$!
echo "   PID: $RESULTADOS_PID"
sleep 5

# Iniciar Frontend
echo -e "${GREEN}🚀 Iniciando Frontend Angular (Puerto 4200)...${NC}"
cd "$PROJECT_ROOT/frontend-laboratorios"
npm start > ../logs/frontend.log 2>&1 &
FRONTEND_PID=$!
echo "   PID: $FRONTEND_PID"
echo ""

# Guardar PIDs
mkdir -p "$PROJECT_ROOT/logs"
echo "$USUARIOS_PID" > "$PROJECT_ROOT/logs/usuarios.pid"
echo "$LABORATORIOS_PID" > "$PROJECT_ROOT/logs/laboratorios.pid"
echo "$RESULTADOS_PID" > "$PROJECT_ROOT/logs/resultados.pid"
echo "$FRONTEND_PID" > "$PROJECT_ROOT/logs/frontend.pid"

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  ✅ Todos los servicios han sido iniciados               ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${YELLOW}📌 URLs de acceso:${NC}"
echo "   • Frontend:              http://localhost:4200"
echo "   • API Usuarios:          http://localhost:8081/api/usuarios"
echo "   • API Laboratorios:      http://localhost:8082/api/laboratorios"
echo "   • API Resultados:        http://localhost:8083/api/resultados"
echo "   • Swagger Usuarios:      http://localhost:8081/swagger-ui.html"
echo "   • Swagger Laboratorios:  http://localhost:8082/swagger-ui.html"
echo "   • Swagger Resultados:    http://localhost:8083/swagger-ui.html"
echo ""
echo -e "${YELLOW}📝 Logs disponibles en: $PROJECT_ROOT/logs/${NC}"
echo ""
echo -e "${RED}⚠️  Para detener todos los servicios, ejecuta: ./ci/stop-all.sh${NC}"
echo ""

