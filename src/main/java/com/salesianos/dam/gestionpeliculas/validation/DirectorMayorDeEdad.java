package com.salesianos.dam.gestionpeliculas.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DirectorMayorDeEdadValidator.class)
@Documented
public @interface DirectorMayorDeEdad {

    String message() default "El director debe tener al menos 18 años en la fecha de estreno";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}