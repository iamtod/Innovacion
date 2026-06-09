package com.sistema.solicitudes.service.interfaces;

import com.sistema.solicitudes.model.TecnicoDTO;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de técnicos de soporte.
 */
public interface ITecnicoService {

    List<TecnicoDTO> listarTodos();

    List<TecnicoDTO> listarDisponibles();

    TecnicoDTO obtenerPorId(Long id);

    TecnicoDTO crear(TecnicoDTO dto);

    TecnicoDTO actualizar(Long id, TecnicoDTO dto);

    void eliminar(Long id);
}
