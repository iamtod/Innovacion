package com.sistema.solicitudes.repository;

import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la gestión de Solicitudes de soporte técnico.
 */
@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findByEstado(EstadoSolicitud estado);

    List<Solicitud> findByClienteId(Long clienteId);

    List<Solicitud> findByTecnicoId(Long tecnicoId);

    @Query("SELECT s FROM Solicitud s WHERE s.tecnico IS NULL AND s.estado = 'ABIERTA'")
    List<Solicitud> findSinAsignar();
}
