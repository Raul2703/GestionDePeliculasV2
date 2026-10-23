package com.salesianos.dam.gestionpeliculas.error;

import com.salesianos.dam.gestionpeliculas.error.notfound.EntidadNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PeliculaYaExisteException.class)
    public ProblemDetail handlePeliculaYaExiste(PeliculaYaExisteException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Película duplicada");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/pelicula-ya-existe"));

        return problem;
    }

    @ExceptionHandler(ActorYaEnRepartoException.class)
    public ProblemDetail handleActorYaEnReparto(ActorYaEnRepartoException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Actor ya asignado");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/actor-ya-en-reparto"));

        return problem;
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ProblemDetail handleEntidadNoEncontrada(EntidadNoEncontradaException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Recurso no encontrado");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/not-found"));

        return problem;
    }

}