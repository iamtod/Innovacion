package com.sistema.solicitudes.repository;

import com.sistema.solicitudes.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de Técnicos de soporte.
 */
@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

    List<Tecnico> findByDisponibleTrue();

    Optional<Tecnico> findByEmail(String email);

    boolean existsByEmail(String email);
}
