#!/bin/bash

###############################################################################
# Script: sonar-run.sh
# Descripción: Ejecuta análisis de SonarQube en todo el proyecto
# Requisitos: SonarQube Server corriendo en http://localhost:9000
# Uso: ./sonar-run.sh
###############################################################################

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║  Análisis de Calidad de Código con SonarQube             ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""

# Directorio raíz del proyecto
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

# Verificar que SonarQube esté corriendo
echo -e "${YELLOW}🔍 Verificando conexión con SonarQube...${NC}"
if curl -s http://localhost:9000/api/system/status > /dev/null 2>&1; then
    echo -e "${GREEN}   ✅ SonarQube está disponible${NC}"
    echo ""
else
    echo -e "${RED}   ❌ SonarQube no está corriendo en http://localhost:9000${NC}"
    echo -e "${YELLOW}   💡 Inicia SonarQube con: docker run -d -p 9000:9000 sonarqube:latest${NC}"
    echo ""
    exit 1
fi

# Función para analizar un microservicio
analyze_microservice() {
    local service_name=$1
    local service_dir=$2
    local project_key=$3
    
    echo -e "${YELLOW}📊 Analizando $service_name...${NC}"
    cd "$PROJECT_ROOT/$service_dir"
    
    if mvn clean verify sonar:sonar \
        -Dsonar.projectKey=$project_key \
        -Dsonar.projectName="$service_name" \
        -Dsonar.host.url=http://localhost:9000 \
        -Dsonar.token=squ_YOUR_TOKEN_HERE; then
        echo -e "${GREEN}   ✅ Análisis de $service_name completado${NC}"
        echo ""
        return 0
    else
        echo -e "${RED}   ❌ Error al analizar $service_name${NC}"
        echo ""
        return 1
    fi
}

# Analizar microservicios
analyze_microservice "Microservicio de Usuarios" "microservicio-usuarios" "laboratorios-usuarios" || exit 1
analyze_microservice "Microservicio de Laboratorios" "microservicio-laboratorios" "laboratorios-labs" || exit 1
analyze_microservice "Microservicio de Resultados" "microservicio-resultados" "laboratorios-resultados" || exit 1

# Analizar Frontend
echo -e "${YELLOW}📊 Analizando Frontend Angular...${NC}"
cd "$PROJECT_ROOT/frontend-laboratorios"

# Ejecutar tests con cobertura primero
npm run test -- --code-coverage --watch=false --browsers=ChromeHeadless || true

# Analizar con SonarScanner
if command -v sonar-scanner &> /dev/null; then
    sonar-scanner \
        -Dsonar.projectKey=laboratorios-frontend \
        -Dsonar.projectName="Frontend Angular" \
        -Dsonar.sources=src \
        -Dsonar.host.url=http://localhost:9000 \
        -Dsonar.token=squ_YOUR_TOKEN_HERE \
        -Dsonar.typescript.lcov.reportPaths=coverage/lcov.info
    echo -e "${GREEN}   ✅ Análisis de Frontend completado${NC}"
else
    echo -e "${YELLOW}   ⚠️  sonar-scanner no está instalado. Saltando análisis de frontend${NC}"
    echo -e "${YELLOW}   💡 Instala con: npm install -g sonarqube-scanner${NC}"
fi

echo ""
echo -e "${BLUE}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║  ✅ Análisis de SonarQube completado                     ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${YELLOW}📊 Resultados disponibles en: http://localhost:9000${NC}"
echo ""
echo -e "${YELLOW}📝 Proyectos analizados:${NC}"
echo "   • laboratorios-usuarios"
echo "   • laboratorios-labs"
echo "   • laboratorios-resultados"
echo "   • laboratorios-frontend"
echo ""
echo -e "${YELLOW}💡 Nota: Reemplaza 'squ_YOUR_TOKEN_HERE' con tu token real de SonarQube${NC}"
echo ""

