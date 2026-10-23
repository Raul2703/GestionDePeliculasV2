package com.salesianos.dam.gestionpeliculas.controller;

import com.salesianos.dam.gestionpeliculas.dto.DirectorRequestDto;
import com.salesianos.dam.gestionpeliculas.dto.DirectorResponseDto;
import com.salesianos.dam.gestionpeliculas.service.DirectorService;
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
@RequestMapping("/api/v1/directores")
@Tag(name = "Directores", description = "Gestión de directores")
public class DirectorController {

    private final DirectorService directorService;

    @Operation(summary = "Listar todos los directores")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de directores"),
            @ApiResponse(responseCode = "404", description = "No hay directores",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<List<DirectorResponseDto>> getAll() {
        return ResponseEntity.ok(
                directorService.getAll()
                        .stream()
                        .map(DirectorResponseDto::of)
                        .toList());
    }

    @Operation(summary = "Obtener un director por su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Director encontrado"),
            @ApiResponse(responseCode = "404", description = "Director no encontrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<DirectorResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(DirectorResponseDto.of(directorService.getById(id)));
    }

    @Operation(summary = "Crear un director")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Director creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<DirectorResponseDto> create(@Valid @RequestBody DirectorRequestDto dto) {
        return ResponseEntity.status(201)
                .body(DirectorResponseDto.of(directorService.add(dto)));
    }

    @Operation(summary = "Editar un director")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Director editado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Director no encontrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<DirectorResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody DirectorRequestDto dto) {
        return ResponseEntity.ok(DirectorResponseDto.of(directorService.update(id, dto)));
    }

    @Operation(summary = "Borrar un director",
            description = "Sus películas no se borran, se quedan sin director")
    @ApiResponse(responseCode = "204", description = "Director borrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        directorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}