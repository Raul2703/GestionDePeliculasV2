package com.salesianos.dam.gestionpeliculas.service;

import com.salesianos.dam.gestionpeliculas.dto.ActorRequestDto;
import com.salesianos.dam.gestionpeliculas.error.notfound.ActorNoEncontradoException;
import com.salesianos.dam.gestionpeliculas.model.Actor;
import com.salesianos.dam.gestionpeliculas.repo.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    public List<Actor> getAll() {
        List<Actor> result = actorRepository.findAll();
        if (result.isEmpty()) {
            throw new ActorNoEncontradoException();
        }
        return result;
    }

    public Actor add(ActorRequestDto dto) {
        return actorRepository.save(dto.to());
    }

}