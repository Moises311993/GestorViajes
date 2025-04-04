package com.transportes.urgentes.gestion_viajes.config;

import com.transportes.urgentes.gestion_viajes.drivers.Conductor;
import com.transportes.urgentes.gestion_viajes.drivers.ConductorService;
import com.transportes.urgentes.gestion_viajes.orders.Order;
import com.transportes.urgentes.gestion_viajes.orders.OrderRepository;
import com.transportes.urgentes.gestion_viajes.orders.OrderService;
import com.transportes.urgentes.gestion_viajes.travels.Travel;
import com.transportes.urgentes.gestion_viajes.travels.TravelDTO;
import com.transportes.urgentes.gestion_viajes.travels.TravelRepository;
import com.transportes.urgentes.gestion_viajes.travels.TravelService;
import com.transportes.urgentes.gestion_viajes.users.User;
import com.transportes.urgentes.gestion_viajes.users.UserRepository;
import com.transportes.urgentes.gestion_viajes.users.UserRole;
import com.transportes.urgentes.gestion_viajes.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private final UserService userService;

    @Autowired
    private final ConductorService driverService;

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final TravelService travelService;

    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private final TravelRepository travelRepo;

    @Autowired
    private final OrderRepository orderRepo;

    @Override
    public void run(String... args) {
        orderRepo.deleteAll();
        travelRepo.deleteAll();
        userRepo.deleteAll();
        // Crear usuario administrador
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setEmail("admin@example.com");
        admin.setNombre("Administrador");
        admin.setApellido("Sistema");
        admin.setRole(UserRole.ADMIN);
        userService.createUser(admin);

        // Crear conductor de prueba
        Conductor driver = new Conductor();
        driver.setUsername("driver1");
        driver.setPassword("driver123");
        driver.setEmail("driver1@example.com");
        driver.setNombre("Juan");
        driver.setApellido("Pérez");
        driver.setLicenciaConduccion("LIC123456");
        driver.setTipoLicencia("B");
        driver.setDisponible(true);
        driver.setCalificacion(4.5);
        driver.setViajesCompletados(10);
        driverService.createDriver(driver);

        // Crear cliente de prueba
        User client = new User();
        client.setUsername("client1");
        client.setPassword("client123");
        client.setEmail("client1@example.com");
        client.setNombre("María");
        client.setApellido("García");
        client.setRole(UserRole.CLIENT);
        userService.createUser(client);

        // Crear viaje de prueba
        TravelDTO travelDTO = new TravelDTO();
        travelDTO.setOrigen("Ciudad de México");
        travelDTO.setDestino("Guadalajara");
        travelDTO.setEstado("EN_PROGRESO");
        travelDTO.setFechaInicio(LocalDateTime.now());
        travelDTO.setFechaEstimadaLlegada(LocalDateTime.now().plusHours(6));
        travelDTO.setDistanciaTotal(500.0);
        travelDTO.setDistanciaRecorrida(200.0);
        travelDTO.setVelocidadActual(80.0);
        travelDTO.setConductorId(driver.getId());
        travelDTO.setCodigoSeguimiento(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        Travel travel = travelService.createTravel(travelDTO);

        // Crear pedido de prueba
        Order order = new Order();
        order.setNumeroPedido("ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCliente(client);
        order.setTravel(travel);
        order.setEstado("EN_PROGRESO");
        order.setDescripcion("Paquete de documentos");
        order.setPeso(2.5);
        order.setDimensiones("30x20x10 cm");
        order.setDireccionOrigen("Av. Reforma 123, CDMX");
        order.setDireccionDestino("Av. Vallarta 456, GDL");
        order.setInstruccionesEspeciales("Entregar en horario de oficina");
        order.setCodigoSeguimiento(travel.getCodigoSeguimiento());
        orderService.createOrder(order);
    }
} 