package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Pelicula;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.salesianos.dam.gestionpeliculas.validation.DirectorMayorDeEdad;

import java.time.LocalDate;

@DirectorMayorDeEdad(message = "{peliculaRequestDto.directorMayorDeEdad}")

public record PeliculaRequestDto(
        @NotBlank(message = "{peliculaRequestDto.titulo.notBlank}")
        String titulo,

        @NotBlank(message = "{peliculaRequestDto.genero.notBlank}")
        String genero,

        @NotNull(message = "{peliculaRequestDto.fechaEstreno.notNull}")
        LocalDate fechaEstreno,

        @NotNull(message = "{peliculaRequestDto.directorId.notNull}")
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