package com.salesianos.dam.gestionpeliculas.controller;

import com.salesianos.dam.gestionpeliculas.dto.ActorRequestDto;
import com.salesianos.dam.gestionpeliculas.dto.ActorSimpleDto;
import com.salesianos.dam.gestionpeliculas.service.ActorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/actores")
public class ActorController {

    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<List<ActorSimpleDto>> getAll() {
        return ResponseEntity.ok(
                actorService.getAll()
                        .stream()
                        .map(ActorSimpleDto::of)
                        .toList());
    }

    @PostMapping
    public ResponseEntity<ActorSimpleDto> create(@Valid @RequestBody ActorRequestDto dto) {
        return ResponseEntity.status(201)
                .body(ActorSimpleDto.of(actorService.add(dto)));
    }

}