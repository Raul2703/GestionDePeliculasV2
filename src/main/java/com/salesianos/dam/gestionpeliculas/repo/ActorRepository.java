package com.salesianos.dam.gestionpeliculas.repo;

import com.salesianos.dam.gestionpeliculas.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
}
