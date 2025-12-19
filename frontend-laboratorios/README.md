# 🎨 Frontend - Sistema de Gestión de Laboratorios Clínicos

**Framework**: Angular 17  
**Puerto**: 4200  
**Versión**: 1.0.0

---

## 📋 Descripción

Aplicación web frontend para el Sistema de Gestión de Laboratorios Clínicos, desarrollada con Angular 17, Bootstrap 5 y TypeScript.

---

## ✨ Características

- ✅ Autenticación con JWT
- ✅ Gestión de usuarios y perfiles
- ✅ Dashboard personalizado por rol
- ✅ Formularios reactivos con validaciones avanzadas
- ✅ Diseño responsive (Mobile, Tablet, Desktop)
- ✅ Interceptores HTTP (Auth, Error, Loading)
- ✅ Guards de protección de rutas
- ✅ Integración completa con microservicios
- ✅ Tests unitarios con Karma + Jasmine

---

## 🚀 Tecnologías

- **Framework**: Angular 17.3
- **UI**: Bootstrap 5.3.8
- **Icons**: Bootstrap Icons + Font Awesome
- **Lenguaje**: TypeScript 5.4
- **Testing**: Karma + Jasmine
- **HTTP**: HttpClient con Interceptors
- **Routing**: Angular Router con Guards

---

## 📁 Estructura del Proyecto

```
frontend-laboratorios/
├── src/
│   ├── app/
│   │   ├── core/                    # Servicios core y guards
│   │   │   ├── guards/             # Guards de autenticación
│   │   │   ├── interceptors/       # Interceptores HTTP
│   │   │   └── services/           # Servicios principales
│   │   ├── features/               # Módulos de funcionalidades
│   │   │   ├── auth/              # Login, registro, recuperar password
│   │   │   ├── dashboard/         # Panel principal
│   │   │   ├── perfil/            # Gestión de perfil
│   │   │   ├── laboratorios/      # Gestión de laboratorios
│   │   │   └── resultados/        # Gestión de resultados
│   │   ├── layouts/               # Layouts de la aplicación
│   │   ├── shared/                # Componentes y utilidades compartidas
│   │   │   ├── components/
│   │   │   ├── models/
│   │   │   └── validators/
│   │   ├── app.component.ts
│   │   ├── app.config.ts
│   │   └── app.routes.ts
│   ├── assets/                    # Recursos estáticos
│   ├── environments/              # Configuración de entornos
│   └── styles.scss               # Estilos globales
├── angular.json
├── package.json
└── README.md
```

---

## 🔧 Instalación

### Prerrequisitos

- Node.js 18+ y npm
- Angular CLI 17+

```bash
npm install -g @angular/cli@17
```

### 1. Instalar Dependencias

```bash
cd frontend-laboratorios
npm install
```

### 2. Configurar Variables de Entorno

Editar `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: {
    usuarios: 'http://localhost:8081/api',
    laboratorios: 'http://localhost:8082/api',
    resultados: 'http://localhost:8083/api'
  }
};
```

### 3. Ejecutar en Desarrollo

```bash
ng serve
# o
npm start
```

Aplicación disponible en: **http://localhost:4200**

---

## 🎯 Páginas Principales

### 1. **Login** (`/login`)
- Inicio de sesión con email y contraseña
- Generación de token JWT
- Redirección al dashboard

### 2. **Registro** (`/registro`)
- Registro de nuevos usuarios
- Validaciones avanzadas de formulario
- Selección de rol

### 3. **Recuperar Contraseña** (`/recuperar-password`)
- Solicitud de recuperación de contraseña
- Envío de email con instrucciones

### 4. **Dashboard** (`/dashboard`)
- Panel principal personalizado por rol
- Estadísticas y accesos rápidos
- Navegación a módulos

### 5. **Perfil** (`/perfil/editar`)
- Visualización y edición de datos personales
- Cambio de contraseña
- Gestión de cuenta

---

## 🔐 Validaciones de Contraseña

### **Requisitos Obligatorios**

El sistema implementa **5 validaciones** para contraseñas seguras:

| # | Validación | Descripción | Regex |
|---|------------|-------------|-------|
| 1️⃣ | **Longitud mínima** | Mínimo 8 caracteres | `.{8,}` |
| 2️⃣ | **Mayúscula** | Al menos 1 letra mayúscula | `(?=.*[A-Z])` |
| 3️⃣ | **Minúscula** | Al menos 1 letra minúscula | `(?=.*[a-z])` |
| 4️⃣ | **Número** | Al menos 1 dígito | `(?=.*\\d)` |
| 5️⃣ | **Carácter especial** | Al menos 1 carácter especial (@$!%*?&) | `(?=.*[@$!%*?&])` |

### **Patrón Completo**

```typescript
// src/app/shared/validators/password-validator.ts
const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
```

### **Ejemplos**

✅ **Contraseñas Válidas**:
- `Password123*`
- `Admin2025!`
- `Secure@Pass1`
- `MyP@ssw0rd`

❌ **Contraseñas Inválidas**:
- `password` (sin mayúscula, sin número, sin especial)
- `PASSWORD123` (sin minúscula, sin especial)
- `Pass123` (muy corta)
- `Password` (sin número, sin especial)

### **Mensajes de Error**

```
"La contraseña debe contener:
 • Mínimo 8 caracteres
 • Al menos 1 letra mayúscula
 • Al menos 1 letra minúscula
 • Al menos 1 número
 • Al menos 1 carácter especial (@$!%*?&)"
```

### **Implementación en Formularios**

```typescript
// Ejemplo en registro.component.ts
this.registroForm = this.fb.group({
  password: ['', [
    Validators.required,
    Validators.minLength(8),
    Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)
  ]]
});
```

---

## 📱 Diseño Responsive

### **Grid de 12 Columnas (Bootstrap)**

El proyecto utiliza el sistema de grid de Bootstrap 5 con 12 columnas:

```html
<!-- Ejemplo de layout responsive -->
<div class="container">
  <div class="row">
    <div class="col-12 col-md-6 col-lg-4">
      <!-- Contenido -->
    </div>
  </div>
</div>
```

### **Breakpoints**

| Dispositivo | Breakpoint | Columnas |
|-------------|------------|----------|
| **Mobile** | < 768px | col-12 (full width) |
| **Tablet** | ≥ 768px | col-md-6 (2 columnas) |
| **Desktop** | ≥ 992px | col-lg-4 (3 columnas) |
| **Large Desktop** | ≥ 1200px | col-xl-3 (4 columnas) |

### **Verificación Responsive**

```bash
# Probar en diferentes tamaños:
- Mobile: 375px × 667px (iPhone)
- Tablet: 768px × 1024px (iPad)
- Desktop: 1920px × 1080px
```

---

## 🛡️ Interceptores HTTP

### 1. **Auth Interceptor**
Agrega automáticamente el token JWT a todas las peticiones:

```typescript
// Agrega: Authorization: Bearer {token}
```

### 2. **Error Interceptor**
Manejo centralizado de errores HTTP:

```typescript
// Captura errores 401, 403, 404, 500
// Muestra mensajes apropiados
// Redirecciona a login si es necesario
```

### 3. **Loading Interceptor**
Indicador de carga global:

```typescript
// Muestra spinner durante peticiones HTTP
```

---

## 🔒 Guards de Protección

### **authGuard**
Protege rutas que requieren autenticación:

```typescript
// Solo usuarios autenticados pueden acceder
// Redirecciona a /login si no está autenticado
```

### **roleGuard**
Protege rutas por rol específico:

```typescript
// Ejemplo: solo ADMINISTRADOR puede acceder a /admin
// Redirecciona a /dashboard si no tiene el rol
```

---

## 🧪 Testing

### Ejecutar Tests

```bash
# Tests en modo watch
npm test

# Tests con cobertura
npm test -- --code-coverage --watch=false --browsers=ChromeHeadless

# Ver reporte de cobertura
open coverage/index.html
```

### Tests Implementados

- ✅ `auth.service.spec.ts` - Servicio de autenticación
- ✅ `auth.guard.spec.ts` - Guards de protección
- ✅ `auth.interceptor.spec.ts` - Interceptor de autenticación
- ✅ `loading.service.spec.ts` - Servicio de carga
- ✅ `app.component.spec.ts` - Componente principal

### Objetivo de Cobertura

**≥ 80%** en:
- Statements
- Branches
- Functions
- Lines

---

## 🏗️ Build

### Development
```bash
ng build
```

### Production
```bash
ng build --configuration production
```

Build generado en: `dist/frontend-laboratorios/`

---

## 🔄 Integración con Backend

### Microservicios Consumidos

| Microservicio | Puerto | Endpoints |
|---------------|--------|-----------|
| **Usuarios** | 8081 | `/api/usuarios/*` |
| **Laboratorios** | 8082 | `/api/laboratorios/*` |
| **Resultados** | 8083 | `/api/resultados/*` |

### Configuración de APIs

```typescript
// src/environments/environment.ts
export const environment = {
  apiUrl: {
    usuarios: 'http://localhost:8081/api',
    laboratorios: 'http://localhost:8082/api',
    resultados: 'http://localhost:8083/api'
  }
};
```

---

## 🎨 Personalización de Estilos

```scss
// src/styles.scss
$primary-color: #0d6efd;
$secondary-color: #6c757d;
$success-color: #198754;
$danger-color: #dc3545;
```

---

## 🐛 Troubleshooting

### Puerto 4200 en uso
```bash
lsof -ti:4200 | xargs kill -9
```

### Errores de compilación
```bash
rm -rf node_modules package-lock.json
npm install
```

### Problemas con tests
```bash
npm run test -- --browsers=ChromeHeadless
```

---

## 📞 Soporte

Ver [README principal](../README.md) o [CONTRIBUTING.md](../CONTRIBUTING.md)

---

**Desarrollado por el Equipo de Desarrollo - DUOC UC**
