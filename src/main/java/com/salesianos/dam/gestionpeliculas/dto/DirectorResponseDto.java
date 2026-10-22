package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Director;

public record DirectorResponseDto(
        Long id,
        String nombre,
        Integer anioNacimiento
) {

    public static DirectorResponseDto of(Director d) {
        return new DirectorResponseDto(
                d.getId(),
                d.getNombre(),
                d.getAnioNacimiento()
        );
    }

}