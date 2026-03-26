# ControlGuardiasAPILeo

Mini proyecto de control de guardias para el instituto IES Alixar.

## Documentacion

Ver [MEMORIA.md](./MEMORIA.md) para documentacion tecnica completa del proyecto.

## Estructura

- frontend: interfaz web HTML/CSS/JavaScript
- backend-spring: API REST con Spring Boot 3.2 + Java 17

## Horario del centro

- Inicio jornada: 08:15
- Fin jornada: 14:45
- Horas lectivas: 6
- Recreo: 30 min (11:15 a 11:45)

## Ejecutar backend

Opcion 1 (desde la raiz del proyecto):

1. Ejecuta:

   mvn -f backend-spring/pom.xml clean install -DskipTests

2. Arranca la aplicacion:

   java -jar backend-spring/target/guardias-backend-1.0-SNAPSHOT.jar

Opcion 2 (entrando en backend-spring):

1. Cambia a la carpeta backend-spring
2. Ejecuta:

   mvn clean install -DskipTests

3. Arranca la aplicacion:

   java -jar target/guardias-backend-1.0-SNAPSHOT.jar

API disponible en:

- http://localhost:4000/api/health
- http://localhost:4000/api/config
- http://localhost:4000/api/partes
- http://localhost:4000/api/parte/2026-02-10
- http://localhost:4000/api/profesores?fecha=2026-02-10

Swagger/OpenAPI: http://localhost:4000/swagger-ui.html

Mas detalles en [backend-spring/README.md](./backend-spring/README.md).

## Ejecutar frontend

Abre frontend/index.html en el navegador.

Para evitar problemas de CORS en algunos navegadores, puedes servirlo con un servidor estatico:

python -m http.server 5500

Luego abre:

http://localhost:5500/frontend

## Metricas de rendimiento

La aplicacion mide automaticamente los tiempos de carga en consola y en el pie de pagina.

## Datos de ejemplo

El backend incluye partes de guardias para dos fechas:

- 2026-02-10 (martes)
- 2026-02-11 (miercoles)

## Caracteristicas

- Visualizacion de guardias por franja horaria
- Registro de profesores
- Control de ausencias y tareas
- Navegacion entre fechas
- Medicion de rendimiento
- Interfaz responsiva
- API REST completa


