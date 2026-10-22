package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Director;

public record DirectorRequestDto(
        String nombre,
        Integer anioNacimiento
) {

    public Director to() {
        return Director.builder()
                .nombre(nombre)
                .anioNacimiento(anioNacimiento)
                .build();
    }

}