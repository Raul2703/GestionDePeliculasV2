package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Director;

public record DirectorSimpleDto(
        Long id,
        String nombre
) {

    public static DirectorSimpleDto of(Director d) {
        return new DirectorSimpleDto(
                d.getId(),
                d.getNombre()
        );
    }

}