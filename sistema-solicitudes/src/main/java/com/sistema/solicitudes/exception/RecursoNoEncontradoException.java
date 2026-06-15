package com.sistema.solicitudes.exception;
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(String.format("%s con ID %d no fue encontrado.", recurso, id));
    }
}
