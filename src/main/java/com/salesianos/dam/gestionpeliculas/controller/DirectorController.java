package com.salesianos.dam.gestionpeliculas.controller;

import com.salesianos.dam.gestionpeliculas.dto.DirectorRequestDto;
import com.salesianos.dam.gestionpeliculas.dto.DirectorResponseDto;
import com.salesianos.dam.gestionpeliculas.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/directores")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    public ResponseEntity<List<DirectorResponseDto>> getAll() {
        return ResponseEntity.ok(
                directorService.getAll()
                        .stream()
                        .map(DirectorResponseDto::of)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(DirectorResponseDto.of(directorService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<DirectorResponseDto> create(@RequestBody DirectorRequestDto dto) {
        return ResponseEntity.status(201)
                .body(DirectorResponseDto.of(directorService.add(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DirectorResponseDto> update(
            @PathVariable Long id,
            @RequestBody DirectorRequestDto dto) {
        return ResponseEntity.ok(DirectorResponseDto.of(directorService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        directorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}