package com.salesianos.dam.gestionpeliculas.service;

import com.salesianos.dam.gestionpeliculas.dto.PeliculaRequestDto;
import com.salesianos.dam.gestionpeliculas.error.notfound.ActorNoEncontradoException;
import com.salesianos.dam.gestionpeliculas.error.notfound.DirectorNoEncontradoException;
import com.salesianos.dam.gestionpeliculas.error.notfound.PeliculaNoEncontradaException;
import com.salesianos.dam.gestionpeliculas.model.Actor;
import com.salesianos.dam.gestionpeliculas.model.Director;
import com.salesianos.dam.gestionpeliculas.model.Pelicula;
import com.salesianos.dam.gestionpeliculas.repo.ActorRepository;
import com.salesianos.dam.gestionpeliculas.repo.DirectorRepository;
import com.salesianos.dam.gestionpeliculas.repo.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeliculaService {

    private final PeliculaRepository peliculaRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;

    public List<Pelicula> getAll() {
        List<Pelicula> result = peliculaRepository.findAll();
        if (result.isEmpty()) {
            throw new PeliculaNoEncontradaException();
        }
        return result;
    }

    public Pelicula getById(Long id) {
        return peliculaRepository.findById(id)
                .orElseThrow(() -> new PeliculaNoEncontradaException(id));
    }

    public Pelicula add(PeliculaRequestDto dto) {

        Director director = directorRepository.findById(dto.directorId())
                .orElseThrow(() -> new DirectorNoEncontradoException(dto.directorId()));

        Pelicula p = dto.to();
        p.setDirector(director);
        return peliculaRepository.save(p);
    }

    public Pelicula update(Long id, PeliculaRequestDto dto) {
        Director director = directorRepository.findById(dto.directorId())
                .orElseThrow(() -> new DirectorNoEncontradoException(dto.directorId()));

        return peliculaRepository.findById(id)
                .map(p -> {
                    p.setTitulo(dto.titulo());
                    p.setGenero(dto.genero());
                    p.setFechaEstreno(dto.fechaEstreno());
                    p.setDirector(director);
                    return peliculaRepository.save(p);
                })
                .orElseThrow(() -> new PeliculaNoEncontradaException(id));
    }

    public void delete(Long id) {
        peliculaRepository.deleteById(id);
    }

    @Transactional
    public Pelicula addActor(Long peliculaId, Long actorId) {
        Pelicula p = getById(peliculaId);
        Actor a = actorRepository.findById(actorId)
                .orElseThrow(() -> new ActorNoEncontradoException(actorId));

        p.getActores().add(a);
        return peliculaRepository.save(p);
    }

}