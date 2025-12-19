# 🤝 CONTRIBUTING - Guía de Contribución

**Sistema de Gestión de Laboratorios Clínicos**

---

## 📋 Tabla de Contenidos

- [Flujo de Trabajo Git](#flujo-de-trabajo-git)
- [Estructura de Ramas](#estructura-de-ramas)
- [Convenciones de Commits](#convenciones-de-commits)
- [Proceso de Pull Requests](#proceso-de-pull-requests)
- [Estándares de Código](#estándares-de-código)
- [Testing](#testing)

---

## 🌳 Flujo de Trabajo Git

Este proyecto sigue **GitFlow** como estrategia de branching:

```
main (producción)
  └── develop (desarrollo)
       ├── feature/* (nuevas características)
       ├── bugfix/* (corrección de bugs)
       └── hotfix/* (correcciones urgentes en producción)
```

---

## 🌿 Estructura de Ramas

### **main**
- Rama de producción
- Solo recibe merges de `develop` o `hotfix/*`
- Cada commit debe estar etiquetado con versión semántica (v1.0.0)
- Protegida: requiere pull request y aprobación

### **develop**
- Rama de desarrollo principal
- Integración de todas las features
- Base para crear nuevas ramas `feature/*`
- Debe estar siempre en estado funcional

### **feature/***
- Para nuevas funcionalidades
- Nomenclatura: `feature/nombre-descriptivo`
- Se crea desde: `develop`
- Se mergea a: `develop`

**Ejemplos**:
```bash
feature/jwt-authentication
feature/user-profile
feature/lab-management
```

### **bugfix/***
- Para corrección de bugs en desarrollo
- Nomenclatura: `bugfix/descripcion-bug`
- Se crea desde: `develop`
- Se mergea a: `develop`

**Ejemplos**:
```bash
bugfix/login-error
bugfix/validation-password
```

### **hotfix/***
- Para correcciones urgentes en producción
- Nomenclatura: `hotfix/descripcion-urgente`
- Se crea desde: `main`
- Se mergea a: `main` y `develop`

**Ejemplos**:
```bash
hotfix/security-vulnerability
hotfix/database-connection
```

---

## 📝 Convenciones de Commits

### Formato de Commit
```
<tipo>(<alcance>): <descripción>

[cuerpo opcional]

[footer opcional]
```

### Tipos de Commit

| Tipo | Descripción | Ejemplo |
|------|-------------|---------|
| `feat` | Nueva funcionalidad | `feat(auth): agregar login con JWT` |
| `fix` | Corrección de bug | `fix(users): corregir validación de email` |
| `docs` | Documentación | `docs(readme): actualizar instrucciones` |
| `style` | Formato de código | `style(frontend): aplicar prettier` |
| `refactor` | Refactorización | `refactor(service): simplificar lógica` |
| `test` | Tests | `test(auth): agregar tests unitarios` |
| `chore` | Tareas de mantenimiento | `chore(deps): actualizar dependencias` |
| `perf` | Mejora de rendimiento | `perf(db): optimizar queries` |

### Ejemplos de Buenos Commits
```bash
feat(usuarios): implementar registro de usuarios
fix(laboratorios): corregir eliminación lógica
docs(api): documentar endpoints en Swagger
test(auth): agregar tests de JWT
refactor(models): separar DTOs de entidades
chore(config): actualizar configuración de Oracle
```

### Reglas de Commits
- ✅ Primera línea: máximo 72 caracteres
- ✅ Usar modo imperativo: "agregar" no "agregado"
- ✅ Primera letra en minúscula
- ✅ Sin punto final en la descripción
- ✅ Cuerpo opcional para explicar el "qué" y el "por qué"

---

## 🔄 Proceso de Pull Requests

### 1. Crear Pull Request

```bash
# 1. Asegurarse de estar en la rama correcta
git checkout develop

# 2. Actualizar develop
git pull origin develop

# 3. Crear nueva rama feature
git checkout -b feature/mi-nueva-funcionalidad

# 4. Hacer commits
git add .
git commit -m "feat(modulo): descripción"

# 5. Push de la rama
git push origin feature/mi-nueva-funcionalidad

# 6. Crear PR en GitHub/GitLab
```

### 2. Template de Pull Request

```markdown
## Descripción
Breve descripción de los cambios realizados.

## Tipo de cambio
- [ ] Nueva funcionalidad (feature)
- [ ] Corrección de bug (bugfix)
- [ ] Refactorización (refactor)
- [ ] Documentación (docs)
- [ ] Otro: _____

## Checklist
- [ ] Mi código sigue el estilo del proyecto
- [ ] He realizado una revisión de mi propio código
- [ ] He comentado el código en áreas complejas
- [ ] He actualizado la documentación correspondiente
- [ ] Mis cambios no generan nuevos warnings
- [ ] He agregado tests que prueban mi funcionalidad
- [ ] Los tests unitarios pasan localmente
- [ ] La cobertura de tests no ha disminuido

## Tests realizados
Describir cómo se probó la funcionalidad.

## Capturas de pantalla (si aplica)
Agregar capturas si hay cambios visuales.

## Issues relacionados
Cierra #issue_number
```

### 3. Revisión de Código

**Revisor debe verificar**:
- ✅ Código cumple estándares del proyecto
- ✅ Tests incluidos y pasando
- ✅ Documentación actualizada
- ✅ Sin conflictos con `develop`
- ✅ Funcionalidad probada localmente

**Proceso de Aprobación**:
1. Al menos 1 aprobación requerida
2. Todos los tests CI/CD deben pasar
3. Sin conflictos pendientes
4. Comentarios resueltos

### 4. Merge

```bash
# Después de aprobación, squash and merge
git checkout develop
git merge --squash feature/mi-nueva-funcionalidad
git commit -m "feat(modulo): agregar nueva funcionalidad"
git push origin develop

# Eliminar rama feature
git branch -d feature/mi-nueva-funcionalidad
git push origin --delete feature/mi-nueva-funcionalidad
```

---

## 💻 Estándares de Código

### Backend (Java/Spring Boot)

#### Convenciones de Nombres
```java
// Clases: PascalCase
public class UsuarioService { }

// Métodos: camelCase
public void registrarUsuario() { }

// Constantes: UPPER_SNAKE_CASE
public static final String API_VERSION = "v1";

// Variables: camelCase
private String nombreCompleto;
```

#### Estructura de Paquetes
```
com.duoc.laboratorio.[modulo]
  ├── config/          # Configuraciones
  ├── controller/      # Controladores REST
  ├── dto/            # Data Transfer Objects
  ├── exception/      # Excepciones personalizadas
  ├── model/          # Entidades JPA
  ├── repository/     # Repositorios
  ├── security/       # Seguridad (JWT, etc.)
  └── service/        # Lógica de negocio
```

#### Buenas Prácticas
- ✅ Usar Lombok para reducir boilerplate
- ✅ DTOs para transferencia de datos
- ✅ Validaciones con Bean Validation
- ✅ Manejo de excepciones centralizado
- ✅ Documentación con JavaDoc y Swagger
- ✅ Logs apropiados (info, warn, error)

---

### Frontend (Angular/TypeScript)

#### Convenciones de Nombres
```typescript
// Componentes: kebab-case
login.component.ts

// Clases: PascalCase
export class LoginComponent { }

// Interfaces: PascalCase con I (opcional)
export interface Usuario { }

// Métodos/Variables: camelCase
public iniciarSesion() { }
private usuarioActual: Usuario;

// Constantes: UPPER_SNAKE_CASE
const API_BASE_URL = 'http://localhost:8081';
```

#### Estructura de Módulos
```
/features/[modulo]/
  ├── [componente].component.ts
  ├── [componente].component.html
  ├── [componente].component.scss
  ├── [componente].component.spec.ts
  └── [servicio].service.ts
```

#### Buenas Prácticas
- ✅ Componentes pequeños y reutilizables
- ✅ Servicios para lógica de negocio
- ✅ Reactive Forms para formularios
- ✅ Interceptores para manejo de HTTP
- ✅ Guards para protección de rutas
- ✅ Observables y async pipe
- ✅ OnPush change detection donde sea posible

---

## 🧪 Testing

### Backend

#### Tests Unitarios
```java
@Test
@DisplayName("Debe registrar usuario correctamente")
void testRegistrarUsuario_Success() {
    // Arrange
    RegistroRequest request = new RegistroRequest();
    // ...
    
    // Act
    UsuarioDTO result = usuarioService.registrarUsuario(request);
    
    // Assert
    assertNotNull(result);
    assertEquals("test@example.com", result.getEmail());
}
```

**Ejecutar Tests**:
```bash
# Todos los tests
./ci/test-backend.sh

# Microservicio específico
cd microservicio-usuarios
mvn test

# Con cobertura
mvn test jacoco:report
```

**Objetivo**: Cobertura mínima del 80%

---

### Frontend

#### Tests Unitarios
```typescript
describe('AuthService', () => {
  it('should login successfully', (done) => {
    // Arrange
    const loginRequest = { email: 'test@test.com', password: 'pass' };
    
    // Act
    service.login(loginRequest).subscribe(response => {
      // Assert
      expect(response).toBeTruthy();
      done();
    });
  });
});
```

**Ejecutar Tests**:
```bash
# Todos los tests
./ci/test-frontend.sh

# Con watch
cd frontend-laboratorios
npm test

# Con cobertura
npm test -- --code-coverage --watch=false
```

**Objetivo**: Cobertura mínima del 80%

---

## 🔍 Code Review Checklist

### Para el Autor
- [ ] He probado mi código localmente
- [ ] He escrito tests unitarios
- [ ] La cobertura no ha disminuido
- [ ] He actualizado la documentación
- [ ] He seguido las convenciones de código
- [ ] No hay warnings ni errores
- [ ] He hecho self-review del código

### Para el Revisor
- [ ] El código es legible y mantenible
- [ ] Los tests cubren casos importantes
- [ ] No hay duplicación de código
- [ ] Las validaciones son apropiadas
- [ ] El manejo de errores es correcto
- [ ] La documentación es clara
- [ ] No introduce vulnerabilidades de seguridad

---

## 📚 Recursos

### Documentación del Proyecto
- [README Principal](README.md)
- [Arquitectura](docs/arquitectura.md)
- [API Documentation](http://localhost:8081/swagger-ui.html)

### Herramientas
- **IDE**: IntelliJ IDEA / VS Code
- **Git Client**: GitKraken / Sourcetree / CLI
- **API Testing**: Postman
- **Code Quality**: SonarQube

### Enlaces Útiles
- [GitFlow Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Spring Boot Best Practices](https://spring.io/guides)
- [Angular Style Guide](https://angular.io/guide/styleguide)

---

## ❓ Preguntas Frecuentes

**¿Puedo hacer commit directo a develop?**
No. Siempre crear una rama feature y hacer PR.

**¿Cuánto tiempo debe estar abierto un PR?**
Máximo 48 horas. PRs viejos deben cerrarse o actualizarse.

**¿Qué hacer si mi rama tiene conflictos?**
```bash
git checkout develop
git pull origin develop
git checkout feature/mi-rama
git merge develop
# Resolver conflictos
git push origin feature/mi-rama
```

**¿Debo eliminar ramas después del merge?**
Sí, tanto local como remotamente.

**¿Qué hacer si necesito ayuda?**
Abrir un issue en el repositorio o contactar al equipo.

---

## 📞 Contacto

Para dudas sobre este proceso:
- **Issues**: Abrir issue en el repositorio
- **Equipo**: Contactar al líder técnico
- **Documentación**: Revisar `/docs`

---

**¡Gracias por contribuir al proyecto!** 🎉

*Última actualización: Diciembre 2025*


