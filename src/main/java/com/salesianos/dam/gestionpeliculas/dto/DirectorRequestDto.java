package com.salesianos.dam.gestionpeliculas.dto;

import com.salesianos.dam.gestionpeliculas.model.Director;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DirectorRequestDto(
        @NotBlank(message = "{directorRequestDto.nombre.notBlank}")
        String nombre,

        @NotNull(message = "{directorRequestDto.anioNacimiento.notNull}")
        @Min(value = 1880, message = "{directorRequestDto.anioNacimiento.min}")
        Integer anioNacimiento
) {

    public Director to() {
        return Director.builder()
                .nombre(nombre)
                .anioNacimiento(anioNacimiento)
                .build();
    }

}