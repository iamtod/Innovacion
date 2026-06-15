package com.sistema.solicitudes.config;

import com.sistema.solicitudes.model.*;
import com.sistema.solicitudes.repository.ClienteRepository;
import com.sistema.solicitudes.repository.SolicitudRepository;
import com.sistema.solicitudes.repository.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;
    private final SolicitudRepository solicitudRepository;

    @Override
    public void run(String... args) {
        log.info("══════════════════════════════════════════════");
        log.info("  Cargando datos iniciales de COMIC S.A...");
        log.info("══════════════════════════════════════════════");

        cargarClientes();
        cargarTecnicos();
        cargarSolicitudes();

        log.info("✔ Clientes cargados  : {}", clienteRepository.count());
        log.info("✔ Técnicos cargados  : {}", tecnicoRepository.count());
        log.info("✔ Solicitudes cargadas: {}", solicitudRepository.count());
        log.info("══════════════════════════════════════════════");
        log.info("  Swagger UI : http://localhost:8080/swagger-ui.html");
        log.info("  H2 Console : http://localhost:8080/h2-console");
        log.info("══════════════════════════════════════════════");
    }

    private void cargarClientes() {
        clienteRepository.save(Cliente.builder()
                .nombre("Ana").apellido("García Romero")
                .email("ana.garcia@empresaabc.com").telefono("999-101-001")
                .empresa("Empresa ABC S.A.C.").build());

        clienteRepository.save(Cliente.builder()
                .nombre("Luis").apellido("Torres Mendez")
                .email("ltorres@corpxyz.pe").telefono("999-102-002")
                .empresa("Corp XYZ E.I.R.L.").build());

        clienteRepository.save(Cliente.builder()
                .nombre("María").apellido("Ríos Huanca")
                .email("maria.rios@startupe.io").telefono("999-103-003")
                .empresa("Startup E S.A.").build());

        clienteRepository.save(Cliente.builder()
                .nombre("Roberto").apellido("Salas Condori")
                .email("r.salas@globalnet.pe").telefono("999-104-004")
                .empresa("GlobalNet Perú S.A.C.").build());

        clienteRepository.save(Cliente.builder()
                .nombre("Carmen").apellido("Vega Pillco")
                .email("cvega@tecnoandes.com").telefono("999-105-005")
                .empresa("TecnoAndes Ltd.").build());
    }

    private void cargarTecnicos() {
        tecnicoRepository.save(Tecnico.builder()
                .nombre("Carlos").apellido("Mendoza Quispe")
                .especialidad("Redes y Conectividad")
                .email("c.mendoza@comicsa.pe").disponible(true).build());

        tecnicoRepository.save(Tecnico.builder()
                .nombre("Sofía").apellido("Vargas Lazo")
                .especialidad("Software y Aplicaciones")
                .email("s.vargas@comicsa.pe").disponible(true).build());

        tecnicoRepository.save(Tecnico.builder()
                .nombre("Jorge").apellido("Castillo Apaza")
                .especialidad("Hardware y Equipos")
                .email("j.castillo@comicsa.pe").disponible(false).build());

        tecnicoRepository.save(Tecnico.builder()
                .nombre("Diana").apellido("Flores Ticona")
                .especialidad("Seguridad Informática")
                .email("d.flores@comicsa.pe").disponible(true).build());

        tecnicoRepository.save(Tecnico.builder()
                .nombre("Andrés").apellido("Paredes Chuquimia")
                .especialidad("Base de Datos y Servidores")
                .email("a.paredes@comicsa.pe").disponible(true).build());
    }

    private void cargarSolicitudes() {
        Cliente ana     = clienteRepository.findByEmail("ana.garcia@empresaabc.com").orElseThrow();
        Cliente luis    = clienteRepository.findByEmail("ltorres@corpxyz.pe").orElseThrow();
        Cliente maria   = clienteRepository.findByEmail("maria.rios@startupe.io").orElseThrow();
        Cliente roberto = clienteRepository.findByEmail("r.salas@globalnet.pe").orElseThrow();
        Cliente carmen  = clienteRepository.findByEmail("cvega@tecnoandes.com").orElseThrow();

        Tecnico carlos = tecnicoRepository.findByEmail("c.mendoza@comicsa.pe").orElseThrow();
        Tecnico sofia  = tecnicoRepository.findByEmail("s.vargas@comicsa.pe").orElseThrow();
        Tecnico diana  = tecnicoRepository.findByEmail("d.flores@comicsa.pe").orElseThrow();
        Tecnico andres = tecnicoRepository.findByEmail("a.paredes@comicsa.pe").orElseThrow();

        // 1 - CERRADA (resuelta hace 2 días)
        LocalDateTime hace2dias = LocalDateTime.now().minusDays(2);
        solicitudRepository.save(Solicitud.builder()
                .titulo("Falla en VPN corporativa")
                .descripcion("Los usuarios no pueden conectarse a la VPN desde casa. El error es 'Auth failed' en todos los clientes Windows 10.")
                .estado(EstadoSolicitud.CERRADA).prioridad(Prioridad.ALTA)
                .cliente(ana).tecnico(carlos)
                .fechaCreacion(hace2dias).fechaActualizacion(hace2dias.plusHours(5))
                .fechaCierre(hace2dias.plusHours(5))
                .resolucion("Se reconfiguró el servidor Radius y se renovaron los certificados SSL. Todos los usuarios pueden conectarse correctamente.")
                .build());

        // 2 - EN_PROCESO
        solicitudRepository.save(Solicitud.builder()
                .titulo("Error crítico en sistema de facturación")
                .descripcion("El módulo de facturación del ERP no genera comprobantes electrónicos desde ayer. Impacta a todas las ventas del día.")
                .estado(EstadoSolicitud.EN_PROCESO).prioridad(Prioridad.CRITICA)
                .cliente(luis).tecnico(sofia)
                .fechaCreacion(LocalDateTime.now().minusHours(3))
                .fechaActualizacion(LocalDateTime.now().minusHours(1))
                .build());

        // 3 - ABIERTA sin técnico
        solicitudRepository.save(Solicitud.builder()
                .titulo("Impresora de red no responde")
                .descripcion("La impresora HP LaserJet del piso 3 no aparece en la red. Los usuarios del área de contabilidad no pueden imprimir.")
                .estado(EstadoSolicitud.ABIERTA).prioridad(Prioridad.MEDIA)
                .cliente(maria).tecnico(null)
                .fechaCreacion(LocalDateTime.now().minusMinutes(45))
                .fechaActualizacion(LocalDateTime.now().minusMinutes(45))
                .build());

        // 4 - PENDIENTE
        solicitudRepository.save(Solicitud.builder()
                .titulo("Solicitud de nuevas licencias Office 365")
                .descripcion("Se requieren 10 licencias adicionales de Microsoft 365 Business para el nuevo equipo de marketing que ingresa la próxima semana.")
                .estado(EstadoSolicitud.PENDIENTE).prioridad(Prioridad.BAJA)
                .cliente(roberto).tecnico(andres)
                .fechaCreacion(LocalDateTime.now().minusDays(1))
                .fechaActualizacion(LocalDateTime.now().minusHours(6))
                .build());

        // 5 - ABIERTA sin técnico (CRITICA)
        solicitudRepository.save(Solicitud.builder()
                .titulo("Posible brecha de seguridad en servidor web")
                .descripcion("Se detectaron intentos de acceso no autorizados en los logs del servidor web. IPs desconocidas intentando acceso SSH en puerto 22.")
                .estado(EstadoSolicitud.ABIERTA).prioridad(Prioridad.CRITICA)
                .cliente(carmen).tecnico(null)
                .fechaCreacion(LocalDateTime.now().minusMinutes(10))
                .fechaActualizacion(LocalDateTime.now().minusMinutes(10))
                .build());

        // 6 - RESUELTA
        LocalDateTime hace1dia = LocalDateTime.now().minusDays(1);
        solicitudRepository.save(Solicitud.builder()
                .titulo("Lentitud en base de datos de producción")
                .descripcion("Las consultas al sistema de inventario tardan más de 30 segundos. El sistema está prácticamente inutilizable desde las 8am.")
                .estado(EstadoSolicitud.RESUELTA).prioridad(Prioridad.ALTA)
                .cliente(ana).tecnico(andres)
                .fechaCreacion(hace1dia).fechaActualizacion(hace1dia.plusHours(2))
                .fechaCierre(hace1dia.plusHours(2))
                .resolucion("Se identificaron índices faltantes en las tablas principales. Se crearon los índices y se optimizaron 3 queries críticas. El tiempo de respuesta bajó a menos de 1 segundo.")
                .build());

        // 7 - EN_PROCESO
        solicitudRepository.save(Solicitud.builder()
                .titulo("Configurar nuevo servidor de archivos")
                .descripcion("Se necesita configurar un servidor NAS Synology recién adquirido con permisos por departamento y backup automático diario.")
                .estado(EstadoSolicitud.EN_PROCESO).prioridad(Prioridad.MEDIA)
                .cliente(luis).tecnico(diana)
                .fechaCreacion(LocalDateTime.now().minusHours(8))
                .fechaActualizacion(LocalDateTime.now().minusHours(2))
                .build());

        // 8 - ABIERTA
        solicitudRepository.save(Solicitud.builder()
                .titulo("Equipo portátil no enciende")
                .descripcion("El laptop Dell Latitude del gerente de finanzas no enciende después de una actualización de Windows. Se necesita recuperar información urgente.")
                .estado(EstadoSolicitud.ABIERTA).prioridad(Prioridad.ALTA)
                .cliente(maria).tecnico(null)
                .fechaCreacion(LocalDateTime.now().minusMinutes(20))
                .fechaActualizacion(LocalDateTime.now().minusMinutes(20))
                .build());
    }
}
