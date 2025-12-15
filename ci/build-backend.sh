#!/bin/bash

###############################################################################
# Script: build-backend.sh
# Descripción: Compila todos los microservicios Spring Boot
# Uso: ./build-backend.sh
###############################################################################

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  Compilando Microservicios Backend                       ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""

# Directorio raíz del proyecto
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

# Función para compilar un microservicio
build_microservice() {
    local service_name=$1
    local service_dir=$2
    
    echo -e "${YELLOW}📦 Compilando $service_name...${NC}"
    cd "$PROJECT_ROOT/$service_dir"
    
    if mvn clean package -DskipTests; then
        echo -e "${GREEN}   ✅ $service_name compilado exitosamente${NC}"
        echo ""
        return 0
    else
        echo -e "${RED}   ❌ Error al compilar $service_name${NC}"
        echo ""
        return 1
    fi
}

# Compilar microservicios
build_microservice "Microservicio de Usuarios" "microservicio-usuarios" || exit 1
build_microservice "Microservicio de Laboratorios" "microservicio-laboratorios" || exit 1
build_microservice "Microservicio de Resultados" "microservicio-resultados" || exit 1

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  ✅ Todos los microservicios compilados exitosamente     ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${YELLOW}📌 JARs generados en:${NC}"
echo "   • microservicio-usuarios/target/"
echo "   • microservicio-laboratorios/target/"
echo "   • microservicio-resultados/target/"
echo ""

