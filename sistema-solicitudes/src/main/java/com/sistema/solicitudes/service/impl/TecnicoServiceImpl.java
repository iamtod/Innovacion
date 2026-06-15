package com.sistema.solicitudes.service.impl;

import com.sistema.solicitudes.exception.NegocioException;
import com.sistema.solicitudes.exception.RecursoNoEncontradoException;
import com.sistema.solicitudes.model.Tecnico;
import com.sistema.solicitudes.model.TecnicoDTO;
import com.sistema.solicitudes.repository.TecnicoRepository;
import com.sistema.solicitudes.service.interfaces.ITecnicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de gestión de técnicos de soporte.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TecnicoServiceImpl implements ITecnicoService {

    private final TecnicoRepository tecnicoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TecnicoDTO> listarTodos() {
        return tecnicoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TecnicoDTO> listarDisponibles() {
        return tecnicoRepository.findByDisponibleTrue().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TecnicoDTO obtenerPorId(Long id) {
        return toDTO(tecnicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico", id)));
    }

    @Override
    public TecnicoDTO crear(TecnicoDTO dto) {
        if (tecnicoRepository.existsByEmail(dto.getEmail()))
            throw new NegocioException("Ya existe un técnico registrado con el email: " + dto.getEmail());
        return toDTO(tecnicoRepository.save(toEntity(dto)));
    }

    @Override
    public TecnicoDTO actualizar(Long id, TecnicoDTO dto) {
        Tecnico existente = tecnicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico", id));

        if (!existente.getEmail().equals(dto.getEmail()) && tecnicoRepository.existsByEmail(dto.getEmail()))
            throw new NegocioException("Ya existe un técnico registrado con el email: " + dto.getEmail());

        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEspecialidad(dto.getEspecialidad());
        existente.setEmail(dto.getEmail());
        existente.setDisponible(dto.getDisponible());
        return toDTO(tecnicoRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!tecnicoRepository.existsById(id))
            throw new RecursoNoEncontradoException("Técnico", id);
        tecnicoRepository.deleteById(id);
    }

    public TecnicoDTO toDTO(Tecnico tecnico) {
        return TecnicoDTO.builder()
                .id(tecnico.getId()).nombre(tecnico.getNombre()).apellido(tecnico.getApellido())
                .especialidad(tecnico.getEspecialidad()).email(tecnico.getEmail()).disponible(tecnico.getDisponible())
                .build();
    }

    private Tecnico toEntity(TecnicoDTO dto) {
        return Tecnico.builder()
                .nombre(dto.getNombre()).apellido(dto.getApellido()).especialidad(dto.getEspecialidad())
                .email(dto.getEmail()).disponible(dto.getDisponible() != null ? dto.getDisponible() : true)
                .build();
    }
}
