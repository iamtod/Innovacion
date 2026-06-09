package com.sistema.solicitudes.exception;

/**
 * Excepción para errores de reglas de negocio dentro del sistema de soporte.
 */
public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
