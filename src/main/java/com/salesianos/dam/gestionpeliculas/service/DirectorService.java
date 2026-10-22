package com.salesianos.dam.gestionpeliculas.service;

import com.salesianos.dam.gestionpeliculas.dto.DirectorRequestDto;
import com.salesianos.dam.gestionpeliculas.error.notfound.DirectorNoEncontradoException;
import com.salesianos.dam.gestionpeliculas.model.Director;
import com.salesianos.dam.gestionpeliculas.repo.DirectorRepository;
import com.salesianos.dam.gestionpeliculas.repo.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final PeliculaRepository peliculaRepository;

    public List<Director> getAll() {
        List<Director> result = directorRepository.findAll();
        if (result.isEmpty()) {
            throw new DirectorNoEncontradoException();
        }
        return result;
    }

    public Director getById(Long id) {
        return directorRepository.findById(id)
                .orElseThrow(() -> new DirectorNoEncontradoException(id));
    }

    public Director add(DirectorRequestDto dto) {
        return directorRepository.save(dto.to());
    }

    public Director update(Long id, DirectorRequestDto dto) {
        return directorRepository.findById(id)
                .map(d -> {
                    d.setNombre(dto.nombre());
                    d.setAnioNacimiento(dto.anioNacimiento());
                    return directorRepository.save(d);
                })
                .orElseThrow(() -> new DirectorNoEncontradoException(id));
    }

    @Transactional
    public void delete(Long id) {
        peliculaRepository.findByDirectorId(id)
                .forEach(p -> p.setDirector(null));
        directorRepository.deleteById(id);
    }

}