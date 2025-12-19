#!/bin/bash

###############################################################################
# Script: stop-all.sh
# Descripción: Detiene todos los microservicios y el frontend
# Uso: ./stop-all.sh
###############################################################################

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${RED}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${RED}║  Deteniendo todos los servicios...                       ║${NC}"
echo -e "${RED}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""

# Directorio raíz del proyecto
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
LOGS_DIR="$PROJECT_ROOT/logs"

# Función para detener un servicio
stop_service() {
    local service_name=$1
    local pid_file="$LOGS_DIR/${service_name}.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p $pid > /dev/null 2>&1; then
            echo -e "${YELLOW}🛑 Deteniendo $service_name (PID: $pid)...${NC}"
            kill $pid 2>/dev/null || true
            sleep 2
            # Forzar si aún está corriendo
            if ps -p $pid > /dev/null 2>&1; then
                kill -9 $pid 2>/dev/null || true
            fi
            echo -e "${GREEN}   ✅ $service_name detenido${NC}"
        else
            echo -e "${YELLOW}   ⚠️  $service_name no estaba corriendo${NC}"
        fi
        rm -f "$pid_file"
    else
        echo -e "${YELLOW}   ⚠️  No se encontró PID para $service_name${NC}"
    fi
}

# Detener servicios
stop_service "frontend"
stop_service "usuarios"
stop_service "laboratorios"
stop_service "resultados"

# Detener procesos por puerto como respaldo
echo ""
echo -e "${YELLOW}🔍 Verificando puertos...${NC}"
lsof -ti:4200 | xargs kill -9 2>/dev/null && echo -e "${GREEN}   ✅ Puerto 4200 liberado${NC}" || echo "   Puerto 4200 ya libre"
lsof -ti:8081 | xargs kill -9 2>/dev/null && echo -e "${GREEN}   ✅ Puerto 8081 liberado${NC}" || echo "   Puerto 8081 ya libre"
lsof -ti:8082 | xargs kill -9 2>/dev/null && echo -e "${GREEN}   ✅ Puerto 8082 liberado${NC}" || echo "   Puerto 8082 ya libre"
lsof -ti:8083 | xargs kill -9 2>/dev/null && echo -e "${GREEN}   ✅ Puerto 8083 liberado${NC}" || echo "   Puerto 8083 ya libre"

echo ""
echo -e "${GREEN}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  ✅ Todos los servicios han sido detenidos               ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""


