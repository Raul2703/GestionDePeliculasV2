package com.salesianos.dam.gestionpeliculas.error;

public class ActorYaEnRepartoException extends RuntimeException {
    public ActorYaEnRepartoException(Long actorId, Long peliculaId) {
        super("El actor con id " + actorId + " ya está en el reparto de la película con id " + peliculaId);
    }
}