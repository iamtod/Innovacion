package com.sistema.solicitudes.service.impl;

import com.sistema.solicitudes.exception.NegocioException;
import com.sistema.solicitudes.exception.RecursoNoEncontradoException;
import com.sistema.solicitudes.model.Cliente;
import com.sistema.solicitudes.model.ClienteDTO;
import com.sistema.solicitudes.repository.ClienteRepository;
import com.sistema.solicitudes.service.interfaces.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de gestión de clientes.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements IClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO obtenerPorId(Long id) {
        return toDTO(clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id)));
    }

    @Override
    public ClienteDTO crear(ClienteDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail()))
            throw new NegocioException("Ya existe un cliente registrado con el email: " + dto.getEmail());
        return toDTO(clienteRepository.save(toEntity(dto)));
    }

    @Override
    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));

        // Verificar email duplicado solo si cambió
        if (!existente.getEmail().equals(dto.getEmail()) && clienteRepository.existsByEmail(dto.getEmail()))
            throw new NegocioException("Ya existe un cliente registrado con el email: " + dto.getEmail());

        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setEmpresa(dto.getEmpresa());
        return toDTO(clienteRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id))
            throw new RecursoNoEncontradoException("Cliente", id);
        clienteRepository.deleteById(id);
    }

    public ClienteDTO toDTO(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId()).nombre(cliente.getNombre()).apellido(cliente.getApellido())
                .email(cliente.getEmail()).telefono(cliente.getTelefono()).empresa(cliente.getEmpresa())
                .build();
    }

    private Cliente toEntity(ClienteDTO dto) {
        return Cliente.builder()
                .nombre(dto.getNombre()).apellido(dto.getApellido()).email(dto.getEmail())
                .telefono(dto.getTelefono()).empresa(dto.getEmpresa())
                .build();
    }
}
