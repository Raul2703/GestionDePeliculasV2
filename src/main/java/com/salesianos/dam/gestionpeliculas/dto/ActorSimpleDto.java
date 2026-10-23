package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Actor;

public record ActorSimpleDto(
        Long id,
        String nombre
) {

    public static ActorSimpleDto of(Actor a) {
        return new ActorSimpleDto(
                a.getId(),
                a.getNombre()
        );
    }

}