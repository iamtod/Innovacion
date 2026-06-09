package com.sistema.solicitudes.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa a un técnico de soporte de COMIC S.A.
 */
@Entity
@Table(name = "tecnicos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, length = 100)
    private String especialidad;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private Boolean disponible;
}
