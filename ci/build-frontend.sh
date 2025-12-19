#!/bin/bash

###############################################################################
# Script: build-frontend.sh
# Descripción: Compila el frontend Angular para producción
# Uso: ./build-frontend.sh
###############################################################################

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  Compilando Frontend Angular                             ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""

# Directorio raíz del proyecto
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT/frontend-laboratorios"

echo -e "${YELLOW}📦 Instalando dependencias...${NC}"
if npm install; then
    echo -e "${GREEN}   ✅ Dependencias instaladas${NC}"
    echo ""
else
    echo -e "${RED}   ❌ Error al instalar dependencias${NC}"
    exit 1
fi

echo -e "${YELLOW}🏗️  Compilando para producción...${NC}"
if npm run build; then
    echo -e "${GREEN}   ✅ Frontend compilado exitosamente${NC}"
    echo ""
else
    echo -e "${RED}   ❌ Error al compilar frontend${NC}"
    exit 1
fi

echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  ✅ Frontend compilado exitosamente                      ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${YELLOW}📌 Build generado en: frontend-laboratorios/dist/${NC}"
echo ""


