package com.sistema.solicitudes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudRequestDTO {

    @Schema(example = "Falla en el disco duro")
    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @Schema(example = "El equipo hace un ruido extraño y no inicia Windows.")
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Schema(example = "ALTA")
    @NotNull(message = "La prioridad es obligatoria")
    private Prioridad prioridad;

    @Schema(example = "1", description = "ID de un cliente válido (1 al 5)")
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @Schema(example = "3", description = "ID de un técnico válido (1 al 5)")
    private Long tecnicoId;
}