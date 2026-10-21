package com.salesianos.dam.gestionpeliculas.utils;

import com.salesianos.dam.gestionpeliculas.model.Actor;
import com.salesianos.dam.gestionpeliculas.model.Director;
import com.salesianos.dam.gestionpeliculas.model.Pelicula;
import com.salesianos.dam.gestionpeliculas.repo.ActorRepository;
import com.salesianos.dam.gestionpeliculas.repo.DirectorRepository;
import com.salesianos.dam.gestionpeliculas.repo.PeliculaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeed {

    private final DirectorRepository directorRepository;
    private final PeliculaRepository peliculaRepository;
    private final ActorRepository actorRepository;

    @PostConstruct
    public void initData() {

        Director d1 = Director.builder()
                .nombre("Raúl Ramírez")
                .anioNacimiento(1970)
                .build();

        Director d2 = Director.builder()
                .nombre("Mario Pérez")
                .anioNacimiento(1949)
                .build();

        directorRepository.saveAll(List.of(d1, d2));

        Actor a1 = Actor.builder().nombre("Alberto García").build();
        Actor a2 = Actor.builder().nombre("Manuel Sancho").build();
        Actor a3 = Actor.builder().nombre("Mario Gil").build();

        actorRepository.saveAll(List.of(a1, a2, a3));

        Pelicula p1 = Pelicula.builder()
                .titulo("2 DAM")
                .genero("Informática")
                .fechaEstreno(LocalDate.of(2010, 7, 16))
                .director(d1)
                .actores(new HashSet<>(Set.of(a1, a2)))
                .build();

        Pelicula p2 = Pelicula.builder()
                .titulo("Salesianos de Triana")
                .genero("Religión")
                .fechaEstreno(LocalDate.of(2006, 3, 17))
                .director(d2)
                .actores(new HashSet<>(Set.of(a3)))
                .build();

        peliculaRepository.saveAll(List.of(p1, p2));
    }

}
