package com.transportes.urgentes.gestion_viajes.services;

import com.transportes.urgentes.gestion_viajes.orders.Order;
import com.transportes.urgentes.gestion_viajes.orders.OrderRepository;
import com.transportes.urgentes.gestion_viajes.orders.OrderService;
import com.transportes.urgentes.gestion_viajes.travels.Travel;
import com.transportes.urgentes.gestion_viajes.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_Success() {
        // Arrange
        Order order = new Order();
        order.setNumeroPedido("ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setEstado("PENDIENTE");
        order.setDescripcion("Paquete de documentos");
        order.setPeso(2.5);
        order.setDimensiones("30x20x10 cm");
        order.setDireccionOrigen("Av. Reforma 123");
        order.setDireccionDestino("Av. Vallarta 456");
        order.setInstruccionesEspeciales("Entregar en horario de oficina");

        User client = new User();
        client.setId(1L);
        order.setCliente(client);

        Travel travel = new Travel();
        travel.setId(1L);
        travel.setCodigoSeguimiento(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setTravel(travel);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order createdOrder = orderService.createOrder(order);

        // Assert
        assertNotNull(createdOrder);
        assertNotNull(createdOrder.getNumeroPedido());
        assertEquals("PENDIENTE", createdOrder.getEstado());
        assertEquals("Paquete de documentos", createdOrder.getDescripcion());
        assertEquals(2.5, createdOrder.getPeso());
        assertEquals("30x20x10 cm", createdOrder.getDimensiones());
        assertEquals("Av. Reforma 123", createdOrder.getDireccionOrigen());
        assertEquals("Av. Vallarta 456", createdOrder.getDireccionDestino());
        assertEquals("Entregar en horario de oficina", createdOrder.getInstruccionesEspeciales());
        assertEquals(client.getId(), createdOrder.getCliente().getId());
        assertEquals(travel.getId(), createdOrder.getTravel().getId());
        assertEquals(travel.getCodigoSeguimiento(), createdOrder.getCodigoSeguimiento());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrderByTrackingCode_Success() {
        // Arrange
        String trackingCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Order order = new Order();
        order.setCodigoSeguimiento(trackingCode);
        order.setNumeroPedido("ORD123");
        order.setEstado("EN_PROGRESO");

        when(orderRepository.findByCodigoSeguimiento(anyString())).thenReturn(Optional.of(order));  

        // Act
        Order foundOrder = orderService.getOrderByTrackingCode(trackingCode);

        // Assert
        assertNotNull(foundOrder);
        assertEquals(trackingCode, foundOrder.getCodigoSeguimiento());
        assertEquals("ORD123", foundOrder.getNumeroPedido());
        assertEquals("EN_PROGRESO", foundOrder.getEstado());

        verify(orderRepository).findByCodigoSeguimiento(trackingCode);
    }

    @Test
    void getOrderByTrackingCode_NotFound() {
        // Arrange
        String trackingCode = "NONEXISTENT";
        when(orderRepository.findByCodigoSeguimiento(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.getOrderByTrackingCode(trackingCode));
        verify(orderRepository).findByCodigoSeguimiento(trackingCode);
    }

    @Test
    void updateOrderStatus_Success() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setEstado("PENDIENTE");

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        orderService.updateOrderStatus(1L, "EN_PROGRESO");

        // Assert
        assertEquals("EN_PROGRESO", order.getEstado());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_OrderNotFound() {
        // Arrange
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.updateOrderStatus(1L, "EN_PROGRESO"));
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrdersByClient_Success() {
        // Arrange
        User client = new User();
        client.setId(1L);

        Order order1 = new Order();
        order1.setNumeroPedido("ORD1");
        Order order2 = new Order();
        order2.setNumeroPedido("ORD2");

        when(orderRepository.findByClienteId(anyLong())).thenReturn(Arrays.asList(order1, order2));

        // Act
        var orders = orderService.getOrdersByClient(client.getId());

        // Assert
        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals("ORD1", orders.get(0).getNumeroPedido());
        assertEquals("ORD2", orders.get(1).getNumeroPedido());

        verify(orderRepository).findByClienteId(client.getId());
    }
} 