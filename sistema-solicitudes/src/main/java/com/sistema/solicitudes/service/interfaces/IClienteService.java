package com.sistema.solicitudes.service.interfaces;

import com.sistema.solicitudes.model.ClienteDTO;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de clientes.
 */
public interface IClienteService {

    List<ClienteDTO> listarTodos();

    ClienteDTO obtenerPorId(Long id);

    ClienteDTO crear(ClienteDTO dto);

    ClienteDTO actualizar(Long id, ClienteDTO dto);

    void eliminar(Long id);
}
