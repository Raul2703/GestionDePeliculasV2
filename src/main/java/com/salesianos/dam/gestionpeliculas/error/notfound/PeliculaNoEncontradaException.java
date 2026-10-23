package com.salesianos.dam.gestionpeliculas.error.notfound;

public class PeliculaNoEncontradaException extends EntidadNoEncontradaException {

    public PeliculaNoEncontradaException() {
        super("No se han encontrado películas");
    }

    public PeliculaNoEncontradaException(Long id) {
        super("No se ha encontrado la película con id " + id);
    }
}