package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Pelicula;

import java.time.LocalDate;

public record PeliculaResponseDto(
        Long id,
        String titulo,
        String genero,
        LocalDate fechaEstreno,
        DirectorSimpleDto director
) {

    public static PeliculaResponseDto of(Pelicula p) {
        return new PeliculaResponseDto(
                p.getId(),
                p.getTitulo(),
                p.getGenero(),
                p.getFechaEstreno(),
                p.getDirector() != null ? DirectorSimpleDto.of(p.getDirector()) : null
        );
    }

}