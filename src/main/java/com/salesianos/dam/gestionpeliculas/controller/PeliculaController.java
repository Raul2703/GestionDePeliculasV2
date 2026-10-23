package com.salesianos.dam.gestionpeliculas.controller;

import com.salesianos.dam.gestionpeliculas.dto.PeliculaRequestDto;
import com.salesianos.dam.gestionpeliculas.dto.PeliculaResponseDto;
import com.salesianos.dam.gestionpeliculas.service.PeliculaService;
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
@RequestMapping("/api/v1/peliculas")
@Tag(name = "Películas", description = "Gestión de películas y de su reparto")
public class PeliculaController {

    private final PeliculaService peliculaService;

    @Operation(summary = "Listar todas las películas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de películas"),
            @ApiResponse(responseCode = "404", description = "No hay películas",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<List<PeliculaResponseDto>> getAll() {
        return ResponseEntity.ok(
                peliculaService.getAll()
                        .stream()
                        .map(PeliculaResponseDto::of)
                        .toList());
    }

    @Operation(summary = "Obtener una película con su director y su reparto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película encontrada"),
            @ApiResponse(responseCode = "404", description = "Película no encontrada",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PeliculaResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(PeliculaResponseDto.of(peliculaService.getById(id)));
    }

    @Operation(summary = "Crear una película",
            description = "Requiere el id de un director existente que tenga al menos 18 años en la fecha de estreno")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Película creada"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos o director menor de edad",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Director no encontrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe una película con ese título",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<PeliculaResponseDto> create(@Valid @RequestBody PeliculaRequestDto dto) {
        return ResponseEntity.status(201)
                .body(PeliculaResponseDto.of(peliculaService.add(dto)));
    }

    @Operation(summary = "Editar una película")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película editada"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos o director menor de edad",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Película o director no encontrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe una película con ese título",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PeliculaResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody PeliculaRequestDto dto) {
        return ResponseEntity.ok(PeliculaResponseDto.of(peliculaService.update(id, dto)));
    }

    @Operation(summary = "Borrar una película")
    @ApiResponse(responseCode = "204", description = "Película borrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        peliculaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asignar un actor al reparto de una película",
            description = "Relación muchos a muchos entre Película y Actor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Actor añadido al reparto"),
            @ApiResponse(responseCode = "404", description = "Película o actor no encontrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "El actor ya está en el reparto",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{peliculaId}/actores/{actorId}")
    public ResponseEntity<PeliculaResponseDto> addActor(
            @PathVariable Long peliculaId,
            @PathVariable Long actorId) {
        return ResponseEntity.status(201)
                .body(PeliculaResponseDto.of(peliculaService.addActor(peliculaId, actorId)));
    }

}