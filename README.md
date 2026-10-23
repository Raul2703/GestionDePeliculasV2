🎬 Gestión de Películas — API REST

API REST desarrollada con Spring Boot para gestionar un catálogo de películas, sus directores y el reparto de actores.

Proyecto de las asignaturas Acceso a Datos y Programación de Servicios y Procesos — 2º DAM, Salesianos Triana (curso 2026-27).

Autor: Raúl Ramírez Domínguez

📑 Índice
Tecnologías
Cómo ejecutar el proyecto
Modelo de datos
Estructura del proyecto
Endpoints
Ejemplos de peticiones y respuestas
Validaciones
Gestión de errores
Documentación OpenAPI / Swagger
Colección de Postman
Datos de ejemplo
Metodología de trabajo
🛠 Tecnologías
Tecnología	Uso
Java 21	Lenguaje
Spring Boot 4	Framework principal
Spring Web MVC	API REST
Spring Data JPA + Hibernate	Persistencia
H2	Base de datos (en fichero)
Bean Validation	Validación de datos de entrada
Lombok	Reducción de código repetitivo
springdoc-openapi	Documentación OpenAPI 3 + Swagger UI
Maven	Gestión de dependencias y construcción
🚀 Cómo ejecutar el proyecto
Requisitos
JDK 21
No hace falta instalar Maven: el proyecto incluye el Maven Wrapper.
Pasos
bash
git clone https://github.com/Raul2703/GestionDePeliculasV2.git
cd GestionDePeliculasV2
./mvnw spring-boot:run        # Linux / macOS / Git Bash
mvnw.cmd spring-boot:run      # Windows (CMD)

También se puede abrir con IntelliJ IDEA como proyecto Maven y ejecutar la clase Application.

La aplicación arranca en http://localhost:8080.

Recurso	URL
API	http://localhost:8080/api/v1
Swagger UI	http://localhost:8080/swagger-ui.html
Especificación OpenAPI (JSON)	http://localhost:8080/v3/api-docs
Consola H2	http://localhost:8080/h2-console

Datos de conexión a la consola H2:

JDBC URL: jdbc:h2:./db/peliculas
Usuario: sa
Contraseña: (vacía)

La base de datos se crea de nuevo en cada arranque (ddl-auto=create-drop) y se rellena con datos de ejemplo.

🗂 Modelo de datos

El modelo tiene tres entidades:

┌──────────────┐ 1        N ┌──────────────────┐ N        M ┌──────────┐
│   Director   │────────────│     Pelicula     │────────────│  Actor   │
├──────────────┤            ├──────────────────┤            ├──────────┤
│ id           │            │ id               │            │ id       │
│ nombre       │            │ titulo (único)   │            │ nombre   │
│ anioNacimiento│           │ genero           │            └──────────┘
└──────────────┘            │ fechaEstreno     │
                            │ director         │
                            │ actores          │
                            └──────────────────┘
Entidad	Atributos	Asociaciones
Director	id (Long), nombre (String), anioNacimiento (Integer)	Un director dirige muchas películas
Pelicula	id (Long), titulo (String, único), genero (String), fechaEstreno (LocalDate)	@ManyToOne con Director · @ManyToMany con Actor
Actor	id (Long), nombre (String)	Participa en muchas películas
Director → Película (1:N): se modela con @ManyToOne en Pelicula.
Película ↔ Actor (M:N): se modela con @ManyToMany en Pelicula (lado propietario), que genera la tabla intermedia pelicula_actores.
📁 Estructura del proyecto

El proyecto sigue una arquitectura por capas:

src/main/java/com/salesianos/dam/gestionpeliculas
├── Application.java
├── config          → Configuración (mensajes de validación)
├── controller      → Controladores REST (reciben las peticiones HTTP)
├── dto             → Objetos de transferencia de datos (entrada y salida)
├── error           → Excepciones y manejador global de errores
│   └── notfound    → Excepciones 404
├── model           → Entidades JPA
├── repo            → Repositorios Spring Data JPA
├── service         → Lógica de negocio
├── utils           → Carga de datos de ejemplo (DataSeed)
└── validation      → Validación personalizada @DirectorMayorDeEdad

src/main/resources
├── application.properties
└── messages/errors.properties   → Mensajes de error de validación

Flujo de una petición: Controller → recibe un DTO → Service aplica la lógica de negocio → Repository accede a la BD → la entidad se convierte de nuevo a DTO para la respuesta.

DTOs

Las entidades nunca se exponen directamente. Se usan record de Java:

DTO	Uso
DirectorRequestDto	Entrada para crear/editar directores
DirectorResponseDto	Salida con los datos del director
DirectorSimpleDto	Versión reducida, anidada en la película
PeliculaRequestDto	Entrada para crear/editar películas (incluye directorId)
PeliculaResponseDto	Salida de la película con DTOs anidados: un DirectorSimpleDto y una lista de ActorSimpleDto
ActorRequestDto	Entrada para crear actores
ActorSimpleDto	Salida de actores y elemento del reparto
🔗 Endpoints
Directores — /api/v1/directores
Método	Ruta	Descripción	Respuestas
GET	/api/v1/directores	Listar todos los directores	200, 404
GET	/api/v1/directores/{id}	Obtener un director	200, 404
POST	/api/v1/directores	Crear un director	201, 400
PUT	/api/v1/directores/{id}	Editar un director	200, 400, 404
DELETE	/api/v1/directores/{id}	Borrar un director	204

Al borrar un director, sus películas no se borran: se quedan sin director asignado.

Películas — /api/v1/peliculas
Método	Ruta	Descripción	Respuestas
GET	/api/v1/peliculas	Listar todas las películas	200, 404
GET	/api/v1/peliculas/{id}	Obtener una película con su director y su reparto	200, 404
POST	/api/v1/peliculas	Crear una película (requiere un director existente)	201, 400, 404, 409
PUT	/api/v1/peliculas/{id}	Editar una película	200, 400, 404, 409
DELETE	/api/v1/peliculas/{id}	Borrar una película	204
Actores — /api/v1/actores
Método	Ruta	Descripción	Respuestas
GET	/api/v1/actores	Listar todos los actores	200, 404
POST	/api/v1/actores	Crear un actor	201, 400
Reparto (relación M:N)
Método	Ruta	Descripción	Respuestas
POST	/api/v1/peliculas/{peliculaId}/actores/{actorId}	Asignar un actor al reparto de una película	201, 404, 409
📨 Ejemplos de peticiones y respuestas
Crear una película

POST /api/v1/peliculas

json
{
  "titulo": "Interstellar",
  "genero": "Ciencia ficción",
  "fechaEstreno": "2014-11-07",
  "directorId": 1
}

Respuesta 201 Created:

json
{
  "id": 3,
  "titulo": "Interstellar",
  "genero": "Ciencia ficción",
  "fechaEstreno": "2014-11-07",
  "director": {
    "id": 1,
    "nombre": "Christopher Nolan"
  },
  "actores": []
}
Asignar un actor al reparto

POST /api/v1/peliculas/1/actores/3

Respuesta 201 Created:

json
{
  "id": 1,
  "titulo": "Origen",
  "genero": "Ciencia ficción",
  "fechaEstreno": "2010-07-16",
  "director": {
    "id": 1,
    "nombre": "Christopher Nolan"
  },
  "actores": [
    { "id": 1, "nombre": "Leonardo DiCaprio" },
    { "id": 2, "nombre": "Cillian Murphy" },
    { "id": 3, "nombre": "Penélope Cruz" }
  ]
}
Crear un director

POST /api/v1/directores

json
{
  "nombre": "Greta Gerwig",
  "anioNacimiento": 1983
}

Respuesta 201 Created:

json
{
  "id": 3,
  "nombre": "Greta Gerwig",
  "anioNacimiento": 1983
}
✅ Validaciones

Los datos de entrada se validan con Bean Validation (@Valid en los controladores). Los mensajes se definen en messages/errors.properties.

DTO	Campo	Reglas
DirectorRequestDto	nombre	@NotBlank
	anioNacimiento	@NotNull, @Min(1880)
PeliculaRequestDto	titulo	@NotBlank
	genero	@NotBlank
	fechaEstreno	@NotNull
	directorId	@NotNull
	(toda la clase)	@DirectorMayorDeEdad
ActorRequestDto	nombre	@NotBlank
Validación personalizada: @DirectorMayorDeEdad

Regla de negocio: un director debe tener al menos 18 años en el año de estreno de la película.

Es una anotación a nivel de clase sobre PeliculaRequestDto, porque necesita dos campos a la vez (directorId y fechaEstreno).
Su validador (DirectorMayorDeEdadValidator) busca al director en la base de datos y compara su año de nacimiento con el año de estreno.

Ejemplo: Christopher Nolan nació en 1970, así que no puede tener una película estrenada en 1980.

⚠️ Gestión de errores

Todos los errores se devuelven con el formato estándar ProblemDetail (RFC 7807), gestionados en GlobalExceptionHandler.

Excepción	Código	Cuándo se produce
DirectorNoEncontradoException	404 Not Found	No existe el director
PeliculaNoEncontradaException	404 Not Found	No existe la película
ActorNoEncontradoException	404 Not Found	No existe el actor
PeliculaYaExisteException	409 Conflict	Se crea o edita una película con un título que ya existe
ActorYaEnRepartoException	409 Conflict	Se asigna un actor que ya está en el reparto de la película
Error de validación	400 Bad Request	Los datos de entrada no cumplen las validaciones

Las tres excepciones 404 heredan de EntidadNoEncontradaException, así que un único método del manejador las gestiona todas.

Se ha elegido 409 Conflict para los duplicados porque la petición es correcta en su forma, pero choca con el estado actual de los datos.

Ejemplo: recurso no encontrado (404)
json
{
  "type": "https://example.com/errors/not-found",
  "title": "Recurso no encontrado",
  "status": 404,
  "detail": "No se ha encontrado la película con id 999",
  "instance": "/api/v1/peliculas/999"
}
Ejemplo: actor ya en el reparto (409)
json
{
  "type": "https://example.com/errors/actor-ya-en-reparto",
  "title": "Actor ya asignado",
  "status": 409,
  "detail": "El actor con id 3 ya está en el reparto de la película con id 1",
  "instance": "/api/v1/peliculas/1/actores/3"
}
Ejemplo: error de validación (400)
json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Error de validación",
  "instance": "/api/v1/directores",
  "invalid-params": [
    {
      "object": "directorRequestDto",
      "message": "El nombre del director es obligatorio",
      "field": "nombre",
      "rejectedValue": ""
    },
    {
      "object": "directorRequestDto",
      "message": "El año de nacimiento del director es obligatorio",
      "field": "anioNacimiento"
    }
  ]
}
📘 Documentación OpenAPI / Swagger

La API está documentada con OpenAPI 3 mediante springdoc-openapi. Con la aplicación arrancada:

👉 http://localhost:8080/swagger-ui.html

Desde Swagger UI se pueden ver todos los endpoints (incluidos los de la relación M:N), sus parámetros, los esquemas de los DTOs y los códigos de error de cada operación, y también probarlos directamente.

📮 Colección de Postman

En la raíz del repositorio está el fichero GestionDePeliculas.postman_collection.json.

Para usarlo: en Postman, Import → selecciona el fichero.

La colección usa la variable {{baseUrl}} (por defecto http://localhost:8080) y está organizada en carpetas:

Directores: listar, obtener, crear, editar, borrar y casos de error (404, 400).
Películas: CRUD completo y casos de error (404, 409 por título duplicado, 400 por director menor de edad).
Actores: listar, crear y error de validación.
Reparto: asignar actor, actor ya asignado (409) y actor inexistente (404).

Los ids de la colección corresponden a los datos de ejemplo cargados al arrancar.

🌱 Datos de ejemplo

Al arrancar, la clase DataSeed carga estos datos:

Directores

id	Nombre	Año de nacimiento
1	Christopher Nolan	1970
2	Pedro Almodóvar	1949

Actores

id	Nombre
1	Leonardo DiCaprio
2	Cillian Murphy
3	Penélope Cruz

Películas

id	Título	Género	Estreno	Director	Reparto
1	Origen	Ciencia ficción	2010-07-16	Christopher Nolan	DiCaprio, Murphy
2	Volver	Drama	2006-03-17	Pedro Almodóvar	Penélope Cruz
🔄 Metodología de trabajo

El proyecto se ha desarrollado con una versión express de metodología ágil:

Tablero Kanban en GitHub Projects con una tarea por funcionalidad (Todo → In Progress → Done).
Una rama por funcionalidad (feature/...), que se integra en main al terminarla. Por ejemplo:
feature/dtos-director, feature/servicio-director, feature/controlador-director
feature/servicio-pelicula, feature/controlador-pelicula
feature/actor, feature/asignar-reparto, feature/detalle-pelicula-reparto
feature/errores-conflicto, feature/validaciones, feature/validacion-director-mayor-edad
feature/swagger, feature/postman, feature/readme
Commits pequeños con mensajes descriptivos de lo que se ha hecho.
