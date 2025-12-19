#!/bin/bash

###############################################################################
# Script: test-backend.sh
# Descripción: Ejecuta tests unitarios de todos los microservicios
# Uso: ./test-backend.sh
###############################################################################

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  Ejecutando Tests Backend (JUnit + JaCoCo)               ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""

# Directorio raíz del proyecto
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

# Función para ejecutar tests de un microservicio
test_microservice() {
    local service_name=$1
    local service_dir=$2
    
    echo -e "${YELLOW}🧪 Ejecutando tests de $service_name...${NC}"
    cd "$PROJECT_ROOT/$service_dir"
    
    if mvn clean test jacoco:report; then
        echo -e "${GREEN}   ✅ Tests de $service_name exitosos${NC}"
        echo -e "${YELLOW}   📊 Reporte JaCoCo: $service_dir/target/site/jacoco/index.html${NC}"
        echo ""
        return 0
    else
        echo -e "${RED}   ❌ Tests de $service_name fallaron${NC}"
        echo ""
        return 1
    fi
}

# Ejecutar tests
test_microservice "Microservicio de Usuarios" "microservicio-usuarios" || exit 1
test_microservice "Microservicio de Laboratorios" "microservicio-laboratorios" || exit 1
test_microservice "Microservicio de Resultados" "microservicio-resultados" || exit 1

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  ✅ Todos los tests ejecutados exitosamente              ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${YELLOW}📊 Reportes de cobertura JaCoCo:${NC}"
echo "   • microservicio-usuarios/target/site/jacoco/index.html"
echo "   • microservicio-laboratorios/target/site/jacoco/index.html"
echo "   • microservicio-resultados/target/site/jacoco/index.html"
echo ""


