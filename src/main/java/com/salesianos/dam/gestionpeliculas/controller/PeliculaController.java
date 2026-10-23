package com.salesianos.dam.gestionpeliculas.controller;

import com.salesianos.dam.gestionpeliculas.dto.PeliculaRequestDto;
import com.salesianos.dam.gestionpeliculas.dto.PeliculaResponseDto;
import com.salesianos.dam.gestionpeliculas.service.PeliculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/peliculas")
public class PeliculaController {

    private final PeliculaService peliculaService;

    @GetMapping
    public ResponseEntity<List<PeliculaResponseDto>> getAll() {
        return ResponseEntity.ok(
                peliculaService.getAll()
                        .stream()
                        .map(PeliculaResponseDto::of)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeliculaResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(PeliculaResponseDto.of(peliculaService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<PeliculaResponseDto> create(@RequestBody PeliculaRequestDto dto) {
        return ResponseEntity.status(201)
                .body(PeliculaResponseDto.of(peliculaService.add(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeliculaResponseDto> update(
            @PathVariable Long id,
            @RequestBody PeliculaRequestDto dto) {
        return ResponseEntity.ok(PeliculaResponseDto.of(peliculaService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        peliculaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}