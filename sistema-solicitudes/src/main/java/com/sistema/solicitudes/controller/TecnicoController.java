package com.sistema.solicitudes.controller;

import com.sistema.solicitudes.model.ApiResponse;
import com.sistema.solicitudes.model.TecnicoDTO;
import com.sistema.solicitudes.service.interfaces.ITecnicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/tecnicos")
@RequiredArgsConstructor
@Tag(name = "Técnicos", description = "Gestión de técnicos de soporte de COMIC S.A.")
public class TecnicoController {

    private final ITecnicoService tecnicoService;

    @GetMapping
    @Operation(summary = "Listar todos los técnicos")
    public ResponseEntity<ApiResponse<List<TecnicoDTO>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.exito("Técnicos obtenidos correctamente", tecnicoService.listarTodos()));
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar técnicos disponibles")
    public ResponseEntity<ApiResponse<List<TecnicoDTO>>> listarDisponibles() {
        return ResponseEntity.ok(ApiResponse.exito("Técnicos disponibles obtenidos", tecnicoService.listarDisponibles()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un técnico por ID")
    public ResponseEntity<ApiResponse<TecnicoDTO>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.exito("Técnico encontrado", tecnicoService.obtenerPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo técnico")
    public ResponseEntity<ApiResponse<TecnicoDTO>> crear(@Valid @RequestBody TecnicoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.exito("Técnico registrado exitosamente", tecnicoService.crear(dto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un técnico existente")
    public ResponseEntity<ApiResponse<TecnicoDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody TecnicoDTO dto) {
        return ResponseEntity.ok(ApiResponse.exito("Técnico actualizado exitosamente", tecnicoService.actualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un técnico")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        tecnicoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.exito("Técnico eliminado exitosamente", null));
    }
}
