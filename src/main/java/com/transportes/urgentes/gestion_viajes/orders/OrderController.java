package com.transportes.urgentes.gestion_viajes.orders;

import com.transportes.urgentes.gestion_viajes.travels.Travel;
import com.transportes.urgentes.gestion_viajes.travels.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired 
    private TravelService travelService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @PutMapping("/{orderId}/assign-travel/{travelId}")
    public ResponseEntity<Order> assignTravel(@PathVariable Long orderId, @PathVariable Long travelId) {
        Travel travel = travelService.getTravelById(travelId)
            .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        return ResponseEntity.ok(orderService.assignTravel(orderId, travel));
    }

    // Endpoint público para rastreo de pedidos
    @GetMapping("/track/{id}")
    public ResponseEntity<Order> trackOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

}
