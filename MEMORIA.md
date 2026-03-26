# MEMORIA DE LA APLICACIÓN
## Control de Guardias - IES Alixar

---

## 1. INTRODUCCIÓN

### Propósito del Proyecto

La aplicación **Control de Guardias** es un sistema de gestión digital para el **Instituto de Educación Secundaria (IES) Alixar** diseñado para optimizar la asignación, visualización y seguimiento de guardias docentes durante la jornada escolar.

### Carácter Innovador y Creativo

**Innovación:**
- **Digitalización de procesos**: Reemplaza los registros en papel por una interfaz web intuitiva y accesible desde cualquier dispositivo.
- **Cálculo de rendimiento en tiempo real**: Medición automática de tiempos de carga para analizar la eficiencia de la aplicación.
- **API RESTful moderna**: Arquitectura escalable que permite futuras integraciones con sistemas de gestión académica.
- **Interfaz responsiva**: Diseño adaptable a diferentes tipos de pantalla (desktop, tablet, móvil).

**Creatividad:**
- Diseño visual atractivo con branding del instituto (logo de IES Alixar).
- Sistema de navegación intuitivo: botones para día siguiente, búsqueda de fechas específicas.
- Visualización clara de guardias por franja horaria con ubicaciones específicas.
- Apartado de ausencias y tareas asignadas con información estructurada (profesor, grupo, aula, asignatura, tarea).

---

## 2. ESTRUCTURA DEL PROYECTO

```
ControlGuadiasAPILeo/
├── backend/                     (Servidor Node.js + Express - ORIGINAL)
│   ├── package.json              (Dependencias npm)
│   └── src/
│       ├── index.js              (Servidor Express, rutas API)
│       └── data/
│           └── guardiasData.js   (Modelos de datos, funciones helpers)
│
├── backend-spring/              (Servidor Java + Spring Boot - ALTERNATIVA)
│   ├── pom.xml                   (Dependencias Maven)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/leo/guardias/
│   │   │   │   ├── GuardiasApplication.java
│   │   │   │   ├── controller/GuardiasController.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── Centro.java
│   │   │   │   │   ├── Franja.java
│   │   │   │   │   ├── Parte.java
│   │   │   │   │   └── ...
│   │   │   │   └── service/GuardiasService.java
│   │   │   └── resources/application.properties
│   │   └── test/
│   └── README.md
│
├── frontend/                     (Interfaz HTML/CSS/JavaScript vanilla)
│   ├── index.html                (Estructura HTML)
│   ├── img/
│   │   └── logoalxia.png         (Logo del instituto)
│   └── assets/
│       ├── css/
│       │   └── styles.css        (Estilos CSS)
│       └── js/
│           └── app.js            (Lógica de la aplicación)
│
├── MEMORIA.md                    (Este documento)
└── README.md                     (Instrucciones generales)
```

### Backend - Node.js + Express (Original)
- **Tecnología**: Node.js + Express.js
- **Puerto**: 4000
- **Ventajas**: Rápido de desarrollar, ligero, ideal para prototipado
- **Datos**: En memoria (JavaScript objects)

### Backend - Spring Boot (Alternativa Enterprise)
- **Tecnología**: Java 17 + Spring Boot 3.2
- **Puerto**: 4000
- **Ventajas**: Tipado fuerte, escalable, production-ready, Swagger incluido
- **Datos**: En memoria (modelos Java con getters/setters)
- **Migración**: Ambos backends exponen los mismos endpoints y devuelven la misma estructura JSON

### Diferenciación de Capas

#### **Origen de Datos**
- **Ubicación**: `backend/src/data/guardiasData.js`
- **Tipo**: Almacenamiento en memoria (constantes JavaScript)
- **Contenido**: Partes de guardias por fecha, franjas horarias, profesores de guardia, ausencias y tareas
- **Formato**: Objetos JavaScript estructurados en JSON

#### **Entorno Servidor (Backend)**
- **Tecnología**: Node.js + Express.js
- **Puerto**: 4000
- **Protocolo**: HTTP REST
- **Seguridad**: CORS habilitado para comunicación con frontend
- **Rutas disponibles**:
  - `GET /api/health` - Verificar disponibilidad del servidor
  - `GET /api/config` - Obtener configuración (centro, franjas, fechas)
  - `GET /api/partes` - Listar todos los partes disponibles
  - `GET /api/parte/:fecha` - Obtener parte completo de una fecha específica
  - `GET /api/profesores` - Listar profesores de guardia

#### **Entorno Cliente (Frontend)**
- **Tecnología**: HTML5 + CSS3 + JavaScript vanilla (sin frameworks)
- **Características**:
  - Carga dinámica de datos via Fetch API
  - Renderización en tiempo real de componentes
  - Medición automática de tiempos de carga
  - Navegación entre fechas
  - Búsqueda de fechas específicas

#### **Comunicación Cliente-Servidor**
```
┌──────────────┐                           ┌──────────────┐
│   Frontend   │                           │   Backend    │
│   (HTML/JS)  │──── HTTP/JSON ────→        │  (Express)   │
│              │←──── Respuesta JSON ────   │              │
└──────────────┘                           └──────────────┘
      ↓                                             ↓
   API_BASE:                                Datos en memoria
   http://localhost:4000/api              (guardiasData.js)
```

**Flujo de datos:**
1. Frontend realiza `fetch()` a endpoint del backend
2. Backend procesa la solicitud
3. Backend devuelve JSON con datos solicitados
4. Frontend renderiza datos en el DOM
5. JavaScript mide tiempos y los registra en consola

---

## 3. MODELOS DE DATOS

### Estructura de Almacenamiento

#### **Centro Educativo**
```javascript
{
  nombre: "IES Alixar",
  jornada: {
    inicio: "08:15",
    fin: "14:45",
    horasLectivas: 6,
    recreo: {
      inicio: "11:15",
      fin: "11:45",
      duracionMinutos: 30
    }
  }
}
```

#### **Franjas Horarias**
```javascript
[
  { id: "h1", etiqueta: "Hora 1", inicio: "08:15", fin: "09:15", orden: 1 },
  { id: "h2", etiqueta: "Hora 2", inicio: "09:15", fin: "10:15", orden: 2 },
  // ... más franjas ...
  { id: "recreo", etiqueta: "Recreo", inicio: "11:15", fin: "11:45", orden: 4, tipo: "descanso" },
  // ...
]
```

#### **Parte de Guardias (por fecha)**
```javascript
{
  fecha: "2026-02-10",
  diaSemana: "martes",
  usuarioConectado: "Navarro Romero, Pablo",
  bloques: [
    {
      franjaId: "h1",
      profesoresGuardia: [
        { nombre: "Dominguez Molina, Beatriz", ubicacion: "Edif 2, Planta Baja" },
        // ... más profesores ...
      ],
      ausenciasTareas: [
        {
          profesorAusente: "Abad Diaz, Joaquin",
          grupo: "1B",
          aula: "Apoyo-04",
          asignatura: "ALCT",
          tarea: "Lectura libre y actividades de la página 22"
        },
        // ... más ausencias ...
      ]
    },
    // ... más bloques por franja ...
  ]
}
```

#### **Profesor (Agregado)**
```javascript
{
  id: "dominguez-molina-beatriz",
  nombre: "Dominguez Molina, Beatriz",
  guardias: [
    {
      franjaId: "h1",
      franja: "Hora 1",
      ubicacion: "Edif 2, Planta Baja"
    }
  ],
  ausenciasCubiertas: [
    {
      franjaId: "h1",
      profesorAusente: "García Rufino, Carmen María",
      grupo: "2LCB"
    }
  ]
}
```

### Fuentes de Datos Disponibles

La aplicación utiliza **almacenamiento en memoria** (en lugar de base de datos, CSV o hojas de cálculo) para simplificar el despliegue. Alternativamente, se podría adaptar para:
- **Base de datos**: PostgreSQL, MongoDB
- **CSV/Excel**: Importar archivos desde el servidor con librerías como `papaparse` (JS) o `csv` (Node.js)
- **Google Sheets API**: Conectar directamente a hojas de cálculo en Google Drive
- **Ficheros JSON**: Cargar desde archivos estáticos en el servidor

---

## 4. DOCUMENTACIÓN TÉCNICA

### Backend - Node.js + Express (Original)

#### **`obtenerParte(fecha)`**
```javascript
export function obtenerParte(fecha) {
  if (!fecha) {
    return partesPorFecha[fechasDisponibles[0]]; // Retorna primer parte
  }
  return partesPorFecha[fecha] || null; // Retorna parte o null
}
```
**Propósito**: Obtener un parte de guardias completo para una fecha específica. Si no se proporciona fecha, devuelve el primer disponible.

#### **`obtenerProfesores(fecha)`**
```javascript
export function obtenerProfesores(fecha) {
  // Mapea profesores únicos y agrega sus guardias + ausencias cubiertas
  const mapaProfes = new Map();
  
  for (const bloque of parte.bloques) {
    for (const guardia of bloque.profesoresGuardia) {
      // Agregar guardia al mapa
      mapaProfes.get(guardia.nombre).guardias.push({
        franjaId: bloque.franjaId,
        franja: franja.etiqueta,
        ubicacion: guardia.ubicacion
      });
    }
  }
  
  return [...mapaProfes.values()].sort((a, b) => a.nombre.localeCompare(b.nombre));
}
```
**Propósito**: Generar un listado agregado de todos los profesores de guardia para una fecha, con sus ubicaciones y guardias asignadas.

#### **`listarPartes()`**
```javascript
export function listarPartes() {
  return fechasDisponibles.map((fecha) => {
    const parte = partesPorFecha[fecha];
    return {
      fecha: parte.fecha,
      diaSemana: parte.diaSemana
    };
  });
}
```
**Propósito**: Devolver un listado simple de todas las fechas con partes disponibles para mostrar en el selector de fechas del frontend.

### Frontend - Funciones Clave

#### **`leerJSON(url)`**
```javascript
async function leerJSON(url) {
  const inicio = performance.now();
  const response = await fetch(url);
  const tiempoFetch = performance.now() - inicio;
  
  if (!response.ok) {
    throw new Error(`Error ${response.status}: ${url}`);
  }
  
  const data = await response.json();
  console.log(`📡 Fetch ${url}: ${tiempoFetch.toFixed(2)}ms`);
  
  return { data, tiempo: tiempoFetch };
}
```
**Propósito**: Wrapper sobre Fetch API que mide automáticamente el tiempo de cada solicitud HTTP y lo registra en consola.

#### **`pintarParte()`**
```javascript
function pintarParte() {
  // 1. Actualiza encabezados (fecha, usuario)
  cabeceraParteEl.textContent = `Parte de: ${formatoFecha(...)} (${diaSemana})`;
  
  // 2. Para cada bloque (franja horaria):
  //    - Obtiene profesores de guardia
  //    - Obtiene ausencias/tareas
  //    - Renderiza HTML con estructura: col-left (horas+guardias) + col (ausencias)
  
  // 3. Renderiza franja de recreo
  
  // 4. Inserta HTML en el DOM
  rowsEl.innerHTML = htmlFilas + recreoHtml;
}
```
**Propósito**: Renderizar dinámicamente la tabla de guardias con profesores, ausencias y tareas en la página.

#### **`cargarParte(indice)`**
```javascript
async function cargarParte(indice) {
  // Validar índice
  if (indice < 0 || indice >= state.partes.length) return;
  
  state.indiceActual = indice;
  const fecha = state.partes[indice].fecha;
  
  // Obtener datos del parte completo desde la API
  const { data, tiempo } = await leerJSON(`${API_BASE}/parte/${fecha}`);
  tiempos.parte = tiempo; // Guardar tiempo de este fetch
  
  // Actualizar estado global
  state.centro = data.centro;
  state.franjas = data.franjas;
  state.parteActual = data.parte;
  
  // Renderizar en pantalla
  pintarParte();
}
```
**Propósito**: Cargar un nuevo parte completo desde la API, actualizar el estado de la aplicación y re-renderizar la interfaz.

#### **`init()`**
```javascript
async function init() {
  try {
    // 1. Health check (validar servidor)
    const healthResp = await leerJSON(`${API_BASE}/health`);
    tiempos.health = healthResp.tiempo;
    
    // 2. Obtener lista de partes
    const partesResp = await leerJSON(`${API_BASE}/partes`);
    tiempos.partes = partesResp.tiempo;
    state.partes = partesResp.data.partes;
    
    // 3. Cargar primer parte
    await cargarParte(0);
    
    // 4. Calcular tiempo total y mostrar métricas
    tiempos.totalCarga = performance.now() - tiempos.inicioApp;
    console.log(`Tiempo total de carga: ${tiempos.totalCarga.toFixed(2)}ms`);
  } catch (error) {
    rowsEl.innerHTML = "<p>Error cargando datos...</p>";
  }
}
```
**Propósito**: Inicializar la aplicación: obtener datos desde la API, renderizar el primer parte y mostrar métricas de rendimiento.

### Medición de Rendimiento

La aplicación implementa medición automática de tiempos:

```javascript
const tiempos = {
  inicioApp: performance.now(),        // Inicio de la ejecución
  health: null,                         // Tiempo del health check
  partes: null,                         // Tiempo de obtener lista de partes
  parte: null,                          // Tiempo de cargar un parte completo
  totalCarga: null                      // Tiempo total hasta renderizado
};
```

**Salida en consola** (cuando abre DevTools):
```
=== MÉTRICAS DE RENDIMIENTO ===
⏱️  Health Check: 45.32ms
⏱️  Obtener Partes: 52.18ms
⏱️  Cargar Parte Inicial: 38.91ms
⏱️  TIEMPO TOTAL DE CARGA: 145.67ms
```

**Salida en UI** (pie de página):
```
⏱️ Tiempo de carga total: 145.67ms
```

---

## Backend - Spring Boot (Alternativa Enterprise)

Este backend alternativo utiliza **Java 17 + Spring Boot 3.2**, con la misma funcionalidad que Express pero con arquitectura enterprise-ready, tipado fuerte y Swagger incluido.

### Estructura de Layered Architecture

```
GuardiasController (@RestController)
        ↓ (inyecta)
GuardiasService (@Service)
        ↓ (usa)
Modelos (Centro, Franja, Parte, Bloque, etc.)
```

### Clases Clave

#### **GuardiasApplication.java**
Punto de entrada que habilita autoconfiguration de Spring.

#### **GuardiasService.java**
Métodos públicos para lógica de negocio:
- `getCentro()` - Retorna configuración del centro
- `getFranjas()` - Lista franjas horarias
- `listarPartes()` - Devuelve fechas disponibles
- `obtenerParte(fecha)` - Obtiene parte completo
- `obtenerProfesores(fecha)` - Listado de profesores con guardias

#### **GuardiasController.java**
Mapea endpoints HTTP a métodos del servicio:
- `GET /api/health` → health()
- `GET /api/config` → config()
- `GET /api/partes` → partes()
- `GET /api/parte/{fecha}` → partePathVariable()
- `GET /api/profesores` → profesores()

### Ventajas Spring Boot vs Express

| Característica | Express | Spring Boot |
|---|---|---|
| Lenguaje | JavaScript | Java 17 |
| Tipado | Dinámico | Estático |
| Validación | Manual | @Valid automática |
| Documentación API | Manual | Swagger incluido |
| Performance | Bueno | Excelente |
| Escalabilidad | Media | Enterprise |

### Ejecutar

```bash
cd backend-spring
mvnw clean package
mvnw spring-boot:run
```

**Swagger**: http://localhost:4000/swagger-ui.html

---

## 5. INSTRUCCIONES DE EJECUCIÓN

### Requisitos Previos

#### Frontend (ambos backends)
- Navegador moderno (Chrome, Firefox, Safari, Edge, Edge)
- Python 3.x o Node.js (para servidor estático)

#### Backend Node.js + Express
- **Node.js** v14 o superior
- **npm** v6 o superior

#### Backend Spring Boot (Alternativa)
- **Java 17** o superior
- **Maven 3.6+** (o usar Maven Wrapper incluido)

### Instalación y Ejecución

#### Opción 1: Backend Node.js + Express

```bash
# Terminal 1: Backend
cd backend
npm install
npm run dev
# Inicia en http://localhost:4000

# Terminal 2: Frontend
cd frontend
python -m http.server 8000
# Accede a http://localhost:8000
```

#### Opción 2: Backend Spring Boot

```bash
# Terminal 1: Backend
cd backend-spring
mvnw clean package
mvnw spring-boot:run
# Inicia en http://localhost:4000
# Swagger: http://localhost:4000/swagger-ui.html

# Terminal 2: Frontend
cd frontend
python -m http.server 8000
# Accede a http://localhost:8000
```

**Nota**: El frontend y backend deben ejecutarse simultáneamente. Ambos backends exponen los mismos endpoints y devuelven la misma estructura JSON.

### Endpoints de la API (Idénticos en ambos backends)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/health` | Verificar estado del servidor |
| GET | `/api/config` | Obtener configuración (centro, franjas, fechas) |
| GET | `/api/partes` | Listar fechas disponibles con partes |
| GET | `/api/parte/:fecha` | Obtener parte completo de una fecha (ej: `/api/parte/2026-02-10`) |
| GET | `/api/profesores?fecha=:fecha` | Listar profesores de guardia de una fecha |

**Ejemplo de respuesta** (`GET /api/parte/2026-02-10`):
```json
{
  "centro": {
    "nombre": "IES Alixar",
    "jornada": { ... }
  },
  "franjas": [ ... ],
  "parte": {
    "fecha": "2026-02-10",
    "diaSemana": "martes",
    "bloques": [ ... ]
  }
}
```

---

## 6. CARACTERÍSTICAS Y FUNCIONALIDADES

### Implementadas ✅

1. **Visualización de guardias por franja horaria**
   - Listado de profesores asignados a cada hora
   - Ubicación exacta (edificio, planta, aula)

2. **Gestión de ausencias y tareas**
   - Registro de profesores ausentes
   - Asignación de tareas a otros profesores
   - Información de grupo y aula

3. **Navegación entre fechas**
   - Botón "Mañana" (siguiente día disponible)
   - Botón "Otro día" (búsqueda manual por fecha)

4. **Medición de rendimiento**
   - Tiempos de carga de cada fetch
   - Tiempo total de carga
   - Registro en consola y UI

5. **Interfaz responsiva**
   - Diseño adaptable (desktop y mobile)
   - Logo del instituto
   - Estilos profesionales

6. **API RESTful**
   - Endpoints para acceso a datos
   - Manejo de errores
   - Soporte CORS

### Futuras Mejoras (No Implementadas)

- [ ] Autenticación de usuarios (login/logout)
- [ ] Almacenamiento en base de datos
- [ ] Importación de datos desde CSV/Excel
- [ ] Generación de informes y estadísticas
- [ ] Notificaciones en tiempo real (WebSockets)
- [ ] Validación y edición de guardias
- [ ] Exportación a PDF
- [ ] Integración con calendario (Google Calendar, Outlook)

---

## 7. BIBLIOGRAFÍA Y REFERENCIAS

### Tecnologías Utilizadas

1. **Backend**
   - [Express.js Official Documentation](https://expressjs.com/)
   - [Node.js Documentation](https://nodejs.org/docs/)
   - [CORS Middleware](https://www.npmjs.com/package/cors)

2. **Frontend**
   - [MDN Web Docs - Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
   - [MDN Web Docs - DOM Manipulation](https://developer.mozilla.org/en-US/docs/Web/API/Document)
   - [CSS Grid Layout](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Grid_Layout)
   - [Performance API](https://developer.mozilla.org/en-US/docs/Web/API/Performance)

3. **Diseño**
   - [Google Fonts - Barlow & Fraunces](https://fonts.google.com/)
   - [Modern CSS Patterns](https://web.dev/patterns/)

### Estándares y Buenas Prácticas

- **REST API Design**: https://restfulapi.net/
- **ES6+ JavaScript**: https://es6-features.org/
- **Web Accessibility (WCAG)**: https://www.w3.org/WAI/

### Fuentes Educativas

- Ejercicio grupal realizado en clase de Desarrollo Web
- Normativa de centros educativos españoles
- Guías de UX/UI para aplicaciones educativas

---

## 8. CONCLUSIONES

La aplicación **Control de Guardias** demuestra la integración exitosa de tecnologías web modernas (Node.js + Express + Vanilla JavaScript) para resolver un problema educativo real. Su arquitectura escalable, medición de rendimiento integrada, e interfaz intuitiva la convierten en una solución práctica y reproducible.

El proyecto valida conceptos clave como:
- Comunicación REST entre cliente y servidor
- Manipulación del DOM en tiempo real
- Medición de rendimiento web
- Diseño responsivo y accesibilidad

---

**Autor**: Estudiante de Desarrollo Web  
**Centro**: IES Alixar  
**Fecha**: Marzo 2026  
**Versión**: 1.0

