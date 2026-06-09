package com.sistema.solicitudes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta con la información de una solicitud de soporte técnico.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudResponseDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private EstadoSolicitud estado;
    private Prioridad prioridad;
    private ClienteDTO cliente;
    private TecnicoDTO tecnico;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private LocalDateTime fechaCierre;
    private String resolucion;
}
