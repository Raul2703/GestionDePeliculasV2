package com.salesianos.dam.gestionpeliculas.repo;

import com.salesianos.dam.gestionpeliculas.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {
}
