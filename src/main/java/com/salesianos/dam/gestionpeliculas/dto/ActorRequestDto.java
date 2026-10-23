package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Actor;
import jakarta.validation.constraints.NotBlank;

public record ActorRequestDto(
        @NotBlank(message = "{actorRequestDto.nombre.notBlank}")
        String nombre
) {

    public Actor to() {
        return Actor.builder()
                .nombre(nombre)
                .build();
    }

}