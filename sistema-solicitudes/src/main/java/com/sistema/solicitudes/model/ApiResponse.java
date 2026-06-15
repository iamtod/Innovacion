package com.sistema.solicitudes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Envoltorio estándar para todas las respuestas de la API de COMIC S.A.
 *
 * @param <T> Tipo de datos en el campo 'data'.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean exito;
    private String mensaje;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> exito(String mensaje, T data) {
        return ApiResponse.<T>builder()
                .exito(true)
                .mensaje(mensaje)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String mensaje) {
        return ApiResponse.<T>builder()
                .exito(false)
                .mensaje(mensaje)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
