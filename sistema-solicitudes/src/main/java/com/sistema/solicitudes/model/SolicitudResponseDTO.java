package com.sistema.solicitudes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
