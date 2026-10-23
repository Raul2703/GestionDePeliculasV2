package com.salesianos.dam.gestionpeliculas.error.notfound;

public class ActorNoEncontradoException extends EntidadNoEncontradaException {

    public ActorNoEncontradoException() {
        super("No se han encontrado actores");
    }

    public ActorNoEncontradoException(Long id) {
        super("No se ha encontrado el actor con id " + id);
    }
}