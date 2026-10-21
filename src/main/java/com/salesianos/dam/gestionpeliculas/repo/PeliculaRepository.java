package com.salesianos.dam.gestionpeliculas.repo;

import com.salesianos.dam.gestionpeliculas.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

    boolean existsByTitulo(String titulo);
}
