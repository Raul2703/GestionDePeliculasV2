package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Pelicula;

import java.time.LocalDate;

public record PeliculaRequestDto(
        String titulo,
        String genero,
        LocalDate fechaEstreno,
        Long directorId
) {

    public Pelicula to() {
        return Pelicula.builder()
                .titulo(titulo)
                .genero(genero)
                .fechaEstreno(fechaEstreno)
                .build();
    }

}