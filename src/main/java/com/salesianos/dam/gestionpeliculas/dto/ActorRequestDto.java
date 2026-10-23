package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Actor;

public record ActorRequestDto(
        String nombre
) {

    public Actor to() {
        return Actor.builder()
                .nombre(nombre)
                .build();
    }

}