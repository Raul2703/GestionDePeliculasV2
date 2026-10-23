package com.salesianos.dam.gestionpeliculas.error;

public class PeliculaYaExisteException extends RuntimeException {
    public PeliculaYaExisteException(String titulo) {
        super("Ya existe una película con el título " + titulo);
    }
}