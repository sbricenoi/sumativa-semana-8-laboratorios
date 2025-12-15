#!/bin/bash

###############################################################################
# Script: test-frontend.sh
# Descripción: Ejecuta tests unitarios del frontend Angular con cobertura
# Uso: ./test-frontend.sh
###############################################################################

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  Ejecutando Tests Frontend (Karma + Jasmine)             ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""

# Directorio raíz del proyecto
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT/frontend-laboratorios"

echo -e "${YELLOW}🧪 Ejecutando tests con cobertura...${NC}"
if npm run test -- --code-coverage --watch=false --browsers=ChromeHeadless; then
    echo -e "${GREEN}   ✅ Tests ejecutados exitosamente${NC}"
    echo ""
else
    echo -e "${RED}   ❌ Tests fallaron${NC}"
    exit 1
fi

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  ✅ Tests de frontend completados                        ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${YELLOW}📊 Reporte de cobertura: frontend-laboratorios/coverage/index.html${NC}"
echo ""

