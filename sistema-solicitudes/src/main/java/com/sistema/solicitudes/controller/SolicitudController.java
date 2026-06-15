package com.sistema.solicitudes.controller;

import com.sistema.solicitudes.model.ApiResponse;
import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.SolicitudRequestDTO;
import com.sistema.solicitudes.model.SolicitudResponseDTO;
import com.sistema.solicitudes.service.interfaces.ISolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Solicitudes de Soporte", description = "Gestión de solicitudes de soporte técnico de COMIC S.A.")
public class SolicitudController {

    private final ISolicitudService solicitudService;

    @GetMapping
    @Operation(summary = "Listar todas las solicitudes",
               description = "Retorna la lista completa. Filtrar opcionalmente por estado.")
    public ResponseEntity<ApiResponse<List<SolicitudResponseDTO>>> listarTodas(
            @Parameter(description = "Estado: ABIERTA, EN_PROCESO, PENDIENTE, RESUELTA, CERRADA, CANCELADA")
            @RequestParam(required = false) EstadoSolicitud estado) {

        List<SolicitudResponseDTO> solicitudes = (estado != null)
                ? solicitudService.listarPorEstado(estado)
                : solicitudService.listarTodas();

        return ResponseEntity.ok(ApiResponse.exito("Solicitudes obtenidas correctamente", solicitudes));
    }

    @GetMapping("/sin-asignar")
    @Operation(summary = "Listar solicitudes abiertas sin técnico asignado")
    public ResponseEntity<ApiResponse<List<SolicitudResponseDTO>>> listarSinAsignar() {
        return ResponseEntity.ok(ApiResponse.exito("Solicitudes sin asignar", solicitudService.listarSinAsignar()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una solicitud por ID")
    public ResponseEntity<ApiResponse<SolicitudResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.exito("Solicitud encontrada", solicitudService.obtenerPorId(id)));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar solicitudes por cliente")
    public ResponseEntity<ApiResponse<List<SolicitudResponseDTO>>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ApiResponse.exito("Solicitudes del cliente obtenidas", solicitudService.listarPorCliente(clienteId)));
    }

    @GetMapping("/tecnico/{tecnicoId}")
    @Operation(summary = "Listar solicitudes asignadas a un técnico")
    public ResponseEntity<ApiResponse<List<SolicitudResponseDTO>>> listarPorTecnico(@PathVariable Long tecnicoId) {
        return ResponseEntity.ok(ApiResponse.exito("Solicitudes del técnico obtenidas", solicitudService.listarPorTecnico(tecnicoId)));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva solicitud de soporte")
    public ResponseEntity<ApiResponse<SolicitudResponseDTO>> crear(@Valid @RequestBody SolicitudRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.exito("Solicitud creada exitosamente", solicitudService.crear(dto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una solicitud existente")
    public ResponseEntity<ApiResponse<SolicitudResponseDTO>> actualizar(
            @PathVariable Long id, @Valid @RequestBody SolicitudRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.exito("Solicitud actualizada exitosamente", solicitudService.actualizar(id, dto)));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de una solicitud",
               description = "Estados válidos: ABIERTA, EN_PROCESO, PENDIENTE, RESUELTA, CERRADA, CANCELADA")
    public ResponseEntity<ApiResponse<SolicitudResponseDTO>> cambiarEstado(
            @PathVariable Long id, @RequestParam EstadoSolicitud estado) {
        return ResponseEntity.ok(ApiResponse.exito("Estado actualizado a: " + estado, solicitudService.cambiarEstado(id, estado)));
    }

    @PatchMapping("/{id}/asignar-tecnico/{tecnicoId}")
    @Operation(summary = "Asignar un técnico disponible a una solicitud")
    public ResponseEntity<ApiResponse<SolicitudResponseDTO>> asignarTecnico(
            @PathVariable Long id, @PathVariable Long tecnicoId) {
        return ResponseEntity.ok(ApiResponse.exito("Técnico asignado exitosamente", solicitudService.asignarTecnico(id, tecnicoId)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una solicitud",
               description = "No se pueden eliminar solicitudes en estado EN_PROCESO.")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        solicitudService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.exito("Solicitud eliminada exitosamente", null));
    }
}
