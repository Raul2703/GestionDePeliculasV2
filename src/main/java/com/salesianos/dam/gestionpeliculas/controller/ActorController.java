package com.salesianos.dam.gestionpeliculas.controller;

import com.salesianos.dam.gestionpeliculas.dto.ActorRequestDto;
import com.salesianos.dam.gestionpeliculas.dto.ActorSimpleDto;
import com.salesianos.dam.gestionpeliculas.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/actores")
@Tag(name = "Actores", description = "Gestión de actores")
public class ActorController {

    private final ActorService actorService;

    @Operation(summary = "Listar todos los actores")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de actores"),
            @ApiResponse(responseCode = "404", description = "No hay actores",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<List<ActorSimpleDto>> getAll() {
        return ResponseEntity.ok(
                actorService.getAll()
                        .stream()
                        .map(ActorSimpleDto::of)
                        .toList());
    }

    @Operation(summary = "Crear un actor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Actor creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<ActorSimpleDto> create(@Valid @RequestBody ActorRequestDto dto) {
        return ResponseEntity.status(201)
                .body(ActorSimpleDto.of(actorService.add(dto)));
    }

}