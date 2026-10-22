package com.salesianos.dam.gestionpeliculas.error.notfound;

public class DirectorNoEncontradoException extends EntidadNoEncontradaException {

    public DirectorNoEncontradoException() {
        super("No se han encontrado directores");
    }

    public DirectorNoEncontradoException(Long id) {
        super("No se ha encontrado el director con id " + id);
    }
}