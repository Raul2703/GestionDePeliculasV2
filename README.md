Gestión de Películas

API REST hecha con Spring Boot para gestionar películas, directores y el reparto de actores.

Proyecto de Acceso a Datos y Programación de Servicios y Procesos (2º DAM, Salesianos Triana). Autor: Raúl Ramírez Domínguez

Tecnologías
Java 21
Spring Boot 4 (Web, Data JPA, Validation)
Base de datos H2
Lombok
Swagger (springdoc-openapi)
Cómo ejecutarlo

Hace falta tener instalado Java 21.

bash
git clone https://github.com/Raul2703/GestionDePeliculasV2.git
cd GestionDePeliculasV2
./mvnw spring-boot:run

También se puede abrir en IntelliJ y ejecutar la clase Application.

La API arranca en http://localhost:8080.

Modelo de datos
Director: id, nombre y año de nacimiento.
Película: id, título (único), género, fecha de estreno y su director.
Actor: id y nombre.

Un director dirige muchas películas, y una película tiene muchos actores (que a su vez pueden salir en muchas películas).

Datos de ejemplo

Al arrancar se cargan estos datos:

Directores: Raúl Ramírez (1970) y Mario Pérez (1949).
Actores: Alberto García, Manuel Sancho y Mario Gil.
Películas:
"2 DAM" (Informática, 2010), dirigida por Raúl Ramírez, con Alberto García y Manuel Sancho.
"Salesianos de Triana" (Religión, 2006), dirigida por Mario Pérez, con Mario Gil.
Endpoints

Directores:

GET /api/v1/directores: listar directores
GET /api/v1/directores/{id}: ver un director
POST /api/v1/directores: crear un director
PUT /api/v1/directores/{id}: editar un director
DELETE /api/v1/directores/{id}: borrar un director

Películas:

GET /api/v1/peliculas: listar películas
GET /api/v1/peliculas/{id}: ver una película con su director y su reparto
POST /api/v1/peliculas: crear una película (necesita un director existente)
PUT /api/v1/peliculas/{id}: editar una película
DELETE /api/v1/peliculas/{id}: borrar una película

Actores:

GET /api/v1/actores: listar actores
POST /api/v1/actores: crear un actor

Reparto:

POST /api/v1/peliculas/{peliculaId}/actores/{actorId}: añadir un actor al reparto de una película

Ejemplo para crear una película:

json
{
  "titulo": "Interstellar",
  "genero": "Ciencia ficción",
  "fechaEstreno": "2014-11-07",
  "directorId": 1
}
Validaciones

Los datos que se envían se comprueban antes de guardarlos: no se pueden dejar vacíos el nombre, el título, el género, la fecha de estreno ni el director.

También hay una validación propia, @DirectorMayorDeEdad, que comprueba que el director tenía al menos 18 años cuando se estrenó la película. Por ejemplo, Raúl Ramírez nació en 1970, así que no puede tener una película estrenada en 1980.

Errores

Los errores se devuelven en formato ProblemDetail:

400: los datos enviados no son válidos.
404: no existe el director, la película o el actor.
409: ya existe una película con ese título, o el actor ya está en el reparto.
Documentación y pruebas
Swagger: con la aplicación arrancada, en http://localhost:8080/swagger-ui.html
Postman: importar el fichero GestionDePeliculas.postman_collection.json que está en la raíz del repositorio.
