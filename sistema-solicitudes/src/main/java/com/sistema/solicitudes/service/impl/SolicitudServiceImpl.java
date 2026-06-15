package com.sistema.solicitudes.service.impl;

import com.sistema.solicitudes.exception.NegocioException;
import com.sistema.solicitudes.exception.RecursoNoEncontradoException;
import com.sistema.solicitudes.model.*;
import com.sistema.solicitudes.repository.ClienteRepository;
import com.sistema.solicitudes.repository.SolicitudRepository;
import com.sistema.solicitudes.repository.TecnicoRepository;
import com.sistema.solicitudes.service.interfaces.ISolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudServiceImpl implements ISolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ClienteServiceImpl clienteService;
    private final TecnicoServiceImpl tecnicoService;

    @Override @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarTodas() {
        return solicitudRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstado(estado).stream().map(this::toDTO).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId))
            throw new RecursoNoEncontradoException("Cliente", clienteId);
        return solicitudRepository.findByClienteId(clienteId).stream().map(this::toDTO).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarPorTecnico(Long tecnicoId) {
        if (!tecnicoRepository.existsById(tecnicoId))
            throw new RecursoNoEncontradoException("Técnico", tecnicoId);
        return solicitudRepository.findByTecnicoId(tecnicoId).stream().map(this::toDTO).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarSinAsignar() {
        return solicitudRepository.findSinAsignar().stream().map(this::toDTO).toList();
    }

    @Override @Transactional(readOnly = true)
    public SolicitudResponseDTO obtenerPorId(Long id) {
        return toDTO(solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", id)));
    }

    @Override
    public SolicitudResponseDTO crear(SolicitudRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", dto.getClienteId()));

        Tecnico tecnico = null;
        if (dto.getTecnicoId() != null) {
            tecnico = tecnicoRepository.findById(dto.getTecnicoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Técnico", dto.getTecnicoId()));
        }

        Solicitud solicitud = Solicitud.builder()
                .titulo(dto.getTitulo()).descripcion(dto.getDescripcion())
                .estado(EstadoSolicitud.ABIERTA).prioridad(dto.getPrioridad())
                .cliente(cliente).tecnico(tecnico)
                .fechaCreacion(LocalDateTime.now()).fechaActualizacion(LocalDateTime.now())
                .build();

        return toDTO(solicitudRepository.save(solicitud));
    }

    @Override
    public SolicitudResponseDTO actualizar(Long id, SolicitudRequestDTO dto) {
        Solicitud existente = solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", id));

        if (existente.getEstado() == EstadoSolicitud.CERRADA || existente.getEstado() == EstadoSolicitud.CANCELADA)
            throw new NegocioException("No se puede modificar una solicitud en estado " + existente.getEstado());

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", dto.getClienteId()));

        Tecnico tecnico = null;
        if (dto.getTecnicoId() != null)
            tecnico = tecnicoRepository.findById(dto.getTecnicoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Técnico", dto.getTecnicoId()));

        existente.setTitulo(dto.getTitulo());
        existente.setDescripcion(dto.getDescripcion());
        existente.setPrioridad(dto.getPrioridad());
        existente.setCliente(cliente);
        existente.setTecnico(tecnico);
        existente.setFechaActualizacion(LocalDateTime.now());
        return toDTO(solicitudRepository.save(existente));
    }

    @Override
    public SolicitudResponseDTO cambiarEstado(Long id, EstadoSolicitud nuevoEstado) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", id));

        validarTransicionEstado(solicitud.getEstado(), nuevoEstado);
        solicitud.setEstado(nuevoEstado);
        solicitud.setFechaActualizacion(LocalDateTime.now());

        if (nuevoEstado == EstadoSolicitud.CERRADA || nuevoEstado == EstadoSolicitud.RESUELTA)
            solicitud.setFechaCierre(LocalDateTime.now());

        return toDTO(solicitudRepository.save(solicitud));
    }

    @Override
    public SolicitudResponseDTO asignarTecnico(Long solicitudId, Long tecnicoId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", solicitudId));

        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico", tecnicoId));

        if (!Boolean.TRUE.equals(tecnico.getDisponible()))
            throw new NegocioException("El técnico " + tecnico.getNombre() + " no está disponible actualmente.");

        solicitud.setTecnico(tecnico);
        solicitud.setEstado(EstadoSolicitud.EN_PROCESO);
        solicitud.setFechaActualizacion(LocalDateTime.now());
        return toDTO(solicitudRepository.save(solicitud));
    }

    @Override
    public void eliminar(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", id));

        if (solicitud.getEstado() == EstadoSolicitud.EN_PROCESO)
            throw new NegocioException("No se puede eliminar una solicitud que está en proceso.");

        solicitudRepository.deleteById(id);
    }

    private void validarTransicionEstado(EstadoSolicitud actual, EstadoSolicitud nuevo) {
        if (actual == EstadoSolicitud.CERRADA)
            throw new NegocioException("No se puede cambiar el estado de una solicitud ya cerrada.");
        if (actual == EstadoSolicitud.CANCELADA && nuevo != EstadoSolicitud.ABIERTA)
            throw new NegocioException("Una solicitud cancelada solo puede reabrirse como ABIERTA.");
    }

    private SolicitudResponseDTO toDTO(Solicitud s) {
        return SolicitudResponseDTO.builder()
                .id(s.getId()).titulo(s.getTitulo()).descripcion(s.getDescripcion())
                .estado(s.getEstado()).prioridad(s.getPrioridad())
                .cliente(s.getCliente() != null ? clienteService.toDTO(s.getCliente()) : null)
                .tecnico(s.getTecnico() != null ? tecnicoService.toDTO(s.getTecnico()) : null)
                .fechaCreacion(s.getFechaCreacion()).fechaActualizacion(s.getFechaActualizacion())
                .fechaCierre(s.getFechaCierre()).resolucion(s.getResolucion())
                .build();
    }
}
