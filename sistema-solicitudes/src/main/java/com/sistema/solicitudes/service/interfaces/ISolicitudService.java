package com.sistema.solicitudes.service.interfaces;

import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.SolicitudRequestDTO;
import com.sistema.solicitudes.model.SolicitudResponseDTO;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de solicitudes de soporte técnico.
 */
public interface ISolicitudService {

    List<SolicitudResponseDTO> listarTodas();

    List<SolicitudResponseDTO> listarPorEstado(EstadoSolicitud estado);

    List<SolicitudResponseDTO> listarPorCliente(Long clienteId);

    List<SolicitudResponseDTO> listarPorTecnico(Long tecnicoId);

    List<SolicitudResponseDTO> listarSinAsignar();

    SolicitudResponseDTO obtenerPorId(Long id);

    SolicitudResponseDTO crear(SolicitudRequestDTO dto);

    SolicitudResponseDTO actualizar(Long id, SolicitudRequestDTO dto);

    SolicitudResponseDTO cambiarEstado(Long id, EstadoSolicitud nuevoEstado);

    SolicitudResponseDTO asignarTecnico(Long solicitudId, Long tecnicoId);

    void eliminar(Long id);
}
