# ========================================
# Dockerfile - Frontend Angular
# Sistema de Gestión de Laboratorios
# ========================================

# Etapa 1: Build
FROM node:18-alpine AS build

WORKDIR /app

# Copiar archivos de configuración
COPY package*.json ./

# Instalar dependencias
RUN npm ci --legacy-peer-deps

# Copiar el código fuente
COPY . .

# Construir la aplicación para producción
RUN npm run build

# Etapa 2: Production con Nginx
FROM nginx:alpine

# Copiar archivos compilados desde la etapa de build
COPY --from=build /app/dist/frontend-laboratorios/browser /usr/share/nginx/html

# Copiar configuración personalizada de nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Exponer puerto 80
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:80 || exit 1

# Comando de inicio
CMD ["nginx", "-g", "daemon off;"]

