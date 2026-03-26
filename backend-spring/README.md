# Control Guardias API - Spring Boot

Backend en Spring Boot 3.2 para el sistema de Control de Guardias de IES Alixar.

## Requisitos

- **Java 17+**
- **Maven 3.6+** (o usar el Maven Wrapper incluido: `mvnw`)

## Estructura del Proyecto

```
backend-spring/
├── pom.xml                    (Dependencias Maven)
├── mvnw / mvnw.cmd          (Maven Wrapper)
├── src/
│   ├── main/
│   │   ├── java/com/leo/guardias/
│   │   │   ├── GuardiasApplication.java    (@SpringBootApplication)
│   │   │   ├── controller/
│   │   │   │   └── GuardiasController.java (@RestController)
│   │   │   ├── model/
│   │   │   │   ├── Centro.java
│   │   │   │   ├── Franja.java
│   │   │   │   ├── Parte.java
│   │   │   │   ├── Bloque.java
│   │   │   │   ├── ProfesorGuardia.java
│   │   │   │   └── AusenciaTarea.java
│   │   │   └── service/
│   │   │       └── GuardiasService.java    (Lógica de negocio)
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── README.md
```

## Instalación y Ejecución

### Compilar
```bash
cd backend-spring

# Opción 1: Con Maven Wrapper (recomendado)
mvnw clean package

# Opción 2: Con Maven instalado en el sistema
mvn clean package
```

### Ejecutar
```bash
# Con Maven
mvnw spring-boot:run

# O ejecutar el JAR compilado
java -jar target/guardias-backend-1.0-SNAPSHOT.jar
```

El servidor inicia en **http://localhost:4000**

## Endpoints API

### Health Check
```
GET /api/health
```
Respuesta:
```json
{
  "ok": "true",
  "servicio": "Control Guardias API",
  "version": "1.0.0"
}
```

### Configuración del Centro
```
GET /api/config
```
Devuelve:
- Centro (IES Alixar)
- Franjas horarias (h1-h6 + recreo)
- Fechas disponibles

### Listar Partes
```
GET /api/partes
```
Devuelve todas las fechas con partes disponibles.

### Obtener Parte Completo
```
GET /api/parte/{fecha}
GET /api/parte?fecha={fecha}

Ejemplo:
GET /api/parte/2026-02-10
```
Devuelve el parte completo con:
- Información del centro
- Franjas horarias
- Profesores de guardia por franja
- Ausencias y tareas

### Listar Profesores
```
GET /api/profesores?fecha={fecha}

Ejemplo:
GET /api/profesores?fecha=2026-02-10
```
Devuelve lista de profesores con:
- Guardias asignadas
- Ubicaciones
- Ausencias cubiertas

## Documentación Swagger

Accede a: **http://localhost:4000/swagger-ui.html**

Aquí puedes ver y probar todos los endpoints interactivamente.

## Datos de Ejemplo

El backend incluye partes de guardias para:
- **2026-02-10** (martes)
- **2026-02-11** (miércoles)

Datos reales de profesores y clases de IES Alixar.

## Diferencias con el Backend Node.js

| Aspecto | Node.js (Express) | Spring Boot |
|--------|------------------|------------|
| Tipado | Dinámico (JavaScript) | Estático (Java 17) |
| Compilación | No necesaria | Compilación previa |
| Validación | Manual | Automática (@Valid) |
| Swagger | Instalación manual | Incluido por defecto |
| Mantenibilidad | Escalable | Enterprise-ready |
| Curva de aprendizaje | Menor | Mayor |
| Performance | Bueno | Excelente |

## Integración con Frontend

El frontend sigue siendo el mismo (HTML/CSS/JS vanilla). Solo cambiar la URL base si es necesario:

```javascript
const API_BASE = "http://localhost:4000/api";
```

## Logs

Los logs se muestran en consola. Para cambiar el nivel de logging, editar `application.properties`:

```properties
logging.level.com.leo.guardias=DEBUG  # Detallado
logging.level.com.leo.guardias=INFO   # Normal
logging.level.com.leo.guardias=WARN   # Solo advertencias
```

## Próximas Mejoras

- [ ] Autenticación y autorización (JWT)
- [ ] Base de datos (JPA/Hibernate)
- [ ] Tests unitarios e integración
- [ ] Caché (Redis)
- [ ] Metricas con Actuator
- [ ] Docker/Containerización
