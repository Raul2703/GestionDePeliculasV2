package com.salesianos.dam.gestionpeliculas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pelicula {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String titulo;

    private String genero;
    private LocalDate fechaEstreno;

    @ManyToOne
    private Director director;

    @ManyToMany
    @Builder.Default
    private Set<Actor> actores = new HashSet<>();

}
