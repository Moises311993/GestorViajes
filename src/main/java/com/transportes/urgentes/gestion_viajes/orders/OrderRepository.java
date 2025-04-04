package com.transportes.urgentes.gestion_viajes.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByCodigoSeguimiento(String codigoSeguimiento);
    List<Order> findByClienteId(Long clienteId);

    List<Order> findByEstado(String estado);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Order> findByCreatedAtBetweenAndEstado(LocalDateTime start, LocalDateTime end, String status);

    @Query("SELECT o FROM Order o WHERE o.cliente.id = :clienteId AND o.estado IN ('PENDIENTE', 'EN_PROGRESO')")
    List<Order> findActiveOrdersByClient(@Param("clienteId") Long clienteId);
    
    @Query("SELECT o FROM Order o WHERE o.conductor.id = :conductorId AND o.estado = 'EN_PROGRESO'")
    List<Order> findActiveOrdersByDriver(@Param("conductorId") Long conductorId);

    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
