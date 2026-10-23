Gestión de Películas

API REST para gestionar un catálogo de películas, sus directores y el reparto de actores, desarrollada con Spring Boot.

Proyecto de Acceso a Datos y Programación de Servicios y Procesos · 2º DAM · Salesianos Triana · Curso 2026-27 Autor: Raúl Ramírez Domínguez

Contenido
Descripción
Tecnologías
Puesta en marcha
Modelo de datos
Arquitectura
Endpoints
Ejemplos
Validaciones
Errores
Swagger
Postman
Datos iniciales
Organización del trabajo
Descripción

La aplicación permite:

Gestionar directores y películas (CRUD completo).
Crear y listar actores.
Asignar actores al reparto de una película (relación muchos a muchos).
Consultar una película con su director y su reparto en una sola respuesta.

Además incluye validación de los datos de entrada, una validación personalizada de negocio, gestión de errores con ProblemDetail, documentación OpenAPI/Swagger y una colección de Postman.

Tecnologías
Java 21
Spring Boot 4 (Web MVC, Data JPA, Validation)
H2 como base de datos en fichero
Lombok
springdoc-openapi (OpenAPI 3 + Swagger UI)
Maven (con Maven Wrapper incluido)
Puesta en marcha

Requisito: tener instalado el JDK 21.

bash
git clone https://github.com/Raul2703/GestionDePeliculasV2.git
cd GestionDePeliculasV2
./mvnw spring-boot:run

En Windows con CMD se usa mvnw.cmd spring-boot:run. También se puede abrir con IntelliJ IDEA y ejecutar la clase Application.

Una vez arrancada:

Qué	Dónde
API	http://localhost:8080/api/v1
Swagger UI	http://localhost:8080/swagger-ui.html
Consola H2	http://localhost:8080/h2-console

Para entrar en la consola H2: JDBC URL jdbc:h2:./db/peliculas, usuario sa y contraseña vacía.

La base de datos se regenera en cada arranque y se carga con datos de ejemplo.

Modelo de datos
dirige
tiene en su reparto
DIRECTOR
Long
id
PK
String
nombre
Integer
anioNacimiento
PELICULA
Long
id
PK
String
titulo
único
String
genero
LocalDate
fechaEstreno
ACTOR
Long
id
PK
String
nombre
Director 1 — N Película: un director dirige muchas películas. Se implementa con @ManyToOne en Pelicula.
Película N — M Actor: una película tiene muchos actores y un actor participa en muchas películas. Se implementa con @ManyToMany en Pelicula, que genera la tabla intermedia del reparto.
Arquitectura

El proyecto está organizado por capas:

com.salesianos.dam.gestionpeliculas
├── controller   Endpoints REST
├── service      Lógica de negocio
├── repo         Acceso a datos (Spring Data JPA)
├── model        Entidades JPA
├── dto          Datos de entrada y salida de la API
├── error        Excepciones y GlobalExceptionHandler
├── validation   Validación personalizada
├── config       Configuración de mensajes de validación
└── utils        DataSeed con los datos iniciales

Una petición recorre el camino Controller → Service → Repository, y la respuesta vuelve convertida en DTO. Las entidades nunca se devuelven directamente.

DTOs

Se han usado record de Java, con un método to() para convertir la entrada en entidad y un método of() para convertir la entidad en salida.

La respuesta de una película usa DTOs anidados:

PeliculaResponseDto
├── id, titulo, genero, fechaEstreno
├── director  → DirectorSimpleDto (id, nombre)
└── actores   → List<ActorSimpleDto> (id, nombre)
Endpoints

Todas las rutas empiezan por /api/v1.

Directores
Método	Ruta	Descripción	Códigos
GET	/directores	Listar directores	200 · 404
GET	/directores/{id}	Ver un director	200 · 404
POST	/directores	Crear director	201 · 400
PUT	/directores/{id}	Editar director	200 · 400 · 404
DELETE	/directores/{id}	Borrar director	204

Al borrar un director, sus películas se conservan y se quedan sin director asignado.

Películas
Método	Ruta	Descripción	Códigos
GET	/peliculas	Listar películas	200 · 404
GET	/peliculas/{id}	Ver una película con director y reparto	200 · 404
POST	/peliculas	Crear película (necesita un director existente)	201 · 400 · 404 · 409
PUT	/peliculas/{id}	Editar película	200 · 400 · 404 · 409
DELETE	/peliculas/{id}	Borrar película	204
Actores
Método	Ruta	Descripción	Códigos
GET	/actores	Listar actores	200 · 404
POST	/actores	Crear actor	201 · 400
Reparto
Método	Ruta	Descripción	Códigos
POST	/peliculas/{peliculaId}/actores/{actorId}	Añadir un actor al reparto	201 · 404 · 409
Ejemplos
Crear una película
http
POST /api/v1/peliculas
Content-Type: application/json

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
  "director": { "id": 1, "nombre": "Christopher Nolan" },
  "actores": []
}
Añadir un actor al reparto
http
POST /api/v1/peliculas/1/actores/3

Respuesta 201 Created:

json
{
  "id": 1,
  "titulo": "Origen",
  "genero": "Ciencia ficción",
  "fechaEstreno": "2010-07-16",
  "director": { "id": 1, "nombre": "Christopher Nolan" },
  "actores": [
    { "id": 1, "nombre": "Leonardo DiCaprio" },
    { "id": 2, "nombre": "Cillian Murphy" },
    { "id": 3, "nombre": "Penélope Cruz" }
  ]
}
Validaciones

Los DTOs de entrada se validan con Bean Validation y los mensajes están en src/main/resources/messages/errors.properties.

Campo	Regla
Director nombre	Obligatorio
Director anioNacimiento	Obligatorio y posterior a 1880
Película titulo	Obligatorio y único
Película genero	Obligatorio
Película fechaEstreno	Obligatoria
Película directorId	Obligatorio y debe existir
Actor nombre	Obligatorio
Validación personalizada @DirectorMayorDeEdad

Regla de negocio: el director debe tener al menos 18 años en el año de estreno de la película.

Se aplica sobre la clase PeliculaRequestDto porque necesita comparar dos datos: el año de nacimiento del director (que busca en la base de datos a partir de directorId) y el año de fechaEstreno.

Por ejemplo, Christopher Nolan nació en 1970, así que la API rechaza una película suya estrenada en 1980.

Errores

Todos los errores se devuelven en formato ProblemDetail (RFC 7807) desde un GlobalExceptionHandler.

Código	Excepción	Motivo
404	DirectorNoEncontradoException	El director no existe
404	PeliculaNoEncontradaException	La película no existe
404	ActorNoEncontradoException	El actor no existe
409	PeliculaYaExisteException	Ya hay una película con ese título
409	ActorYaEnRepartoException	El actor ya está en el reparto de esa película
400	Error de validación	Los datos enviados no son válidos

Las excepciones 404 heredan todas de EntidadNoEncontradaException, lo que permite gestionarlas con un único método.

Para los duplicados se ha elegido 409 Conflict: la petición está bien formada, pero entra en conflicto con los datos que ya existen.

Ejemplo de error 409:

json
{
  "type": "https://example.com/errors/actor-ya-en-reparto",
  "title": "Actor ya asignado",
  "status": 409,
  "detail": "El actor con id 3 ya está en el reparto de la película con id 1",
  "instance": "/api/v1/peliculas/1/actores/3"
}

Ejemplo de error de validación 400, con la lista de campos incorrectos:

json
{
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
    }
  ]
}
Swagger

Con la aplicación arrancada, la documentación interactiva está en:

http://localhost:8080/swagger-ui.html

Ahí aparecen todos los endpoints agrupados (Directores, Películas y Actores), incluido el de asignación de reparto, con los códigos de respuesta y de error de cada uno. Se pueden probar directamente desde el navegador.

Postman

El fichero GestionDePeliculas.postman_collection.json de la raíz del repositorio contiene la colección para probar la API.

Para usarla: en Postman, Import y seleccionar el fichero.

Incluye peticiones correctas y peticiones que provocan cada tipo de error (400, 404 y 409), organizadas en las carpetas Directores, Películas, Actores y Reparto. La URL base está en la variable {{baseUrl}}.

Datos iniciales

Al arrancar se cargan estos datos de ejemplo:

Película	Género	Estreno	Director	Reparto
Origen	Ciencia ficción	2010	Christopher Nolan (1970)	Leonardo DiCaprio, Cillian Murphy
Volver	Drama	2006	Pedro Almodóvar (1949)	Penélope Cruz
Organización del trabajo
Tareas gestionadas en un tablero Kanban (GitHub Projects).
Una rama por funcionalidad, integrada en main al terminarla: feature/dtos-director · feature/excepciones-not-found · feature/servicio-director · feature/controlador-director · feature/dtos-pelicula · feature/servicio-pelicula · feature/controlador-pelicula · feature/actor · feature/asignar-reparto · feature/detalle-pelicula-reparto · feature/errores-conflicto · feature/validaciones · feature/validacion-director-mayor-edad · feature/swagger · feature/postman · feature/readme
Commits pequeños y con mensajes que describen el cambio.
