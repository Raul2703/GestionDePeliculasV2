package com.salesianos.dam.gestionpeliculas.validation;

import com.salesianos.dam.gestionpeliculas.dto.PeliculaRequestDto;
import com.salesianos.dam.gestionpeliculas.repo.DirectorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class DirectorMayorDeEdadValidator
        implements ConstraintValidator<DirectorMayorDeEdad, PeliculaRequestDto> {

    @Autowired
    private DirectorRepository directorRepository;

    @Override
    public boolean isValid(PeliculaRequestDto dto, ConstraintValidatorContext context) {


        if (dto.directorId() == null || dto.fechaEstreno() == null) {
            return true;
        }

        return directorRepository.findById(dto.directorId())
                .map(d -> dto.fechaEstreno().getYear() - d.getAnioNacimiento() >= 18)
                .orElse(true);
    }
}