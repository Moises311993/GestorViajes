package com.transportes.urgentes.gestion_viajes.orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transportes.urgentes.gestion_viajes.travels.Travel;
import com.transportes.urgentes.gestion_viajes.drivers.Conductor;
import com.transportes.urgentes.gestion_viajes.users.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;
import com.transportes.urgentes.gestion_viajes.drivers.ConductorService;
import com.transportes.urgentes.gestion_viajes.exception.ResourceNotFoundException;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ConductorService conductorService;

    @Transactional
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setEstado(status);
        return orderRepository.save(order);
    }

    @Transactional
    public Order assignTravel(Long orderId, Travel travel) {
        Order order = getOrderById(orderId);
        order.setTravel(travel);
        return orderRepository.save(order);
    }

    public Order getOrderByTrackingCode(String trackingCode) {
        return orderRepository.findByCodigoSeguimiento(trackingCode)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<Order> getOrdersByClient(Long id) { 
        return orderRepository.findByClienteId(id);
    }

    public List<Order> countPendingOrders() {
        return orderRepository.findByEstado("EN_PROGRESO");
    }

    public long getDailyOrderCount() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        
        return orderRepository.countByCreatedAtBetween(startOfDay, endOfDay);
    }

    public double getMonthlyRevenue() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);
        
        List<Order> completedOrders = orderRepository.findByCreatedAtBetweenAndEstado(
            startOfMonth, endOfMonth, "COMPLETADA"
        );
        
        return completedOrders.stream()
            .mapToDouble(Order::getTotalAmount)
            .sum();
    }

    public List<Order> getActiveOrdersByClient(Long clienteId) {
        return orderRepository.findActiveOrdersByClient(clienteId);
    }

    public List<Order> getActiveOrdersByDriver(Long conductorId) {
        return orderRepository.findActiveOrdersByDriver(conductorId);
    }

    @Transactional
    public Order completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));
        
        order.setEstado("COMPLETADA");
        order.setFechaCompletada(LocalDateTime.now());
        
        // Actualizar estadísticas del conductor
        if (order.getConductor() != null) {
            Conductor conductor = order.getConductor();
            conductor.setViajesCompletados(conductor.getViajesCompletados() + 1);
            conductorService.updateDriver(conductor.getId(),conductor);
        }
        
        return orderRepository.save(order);
    }

    @Transactional
    public Order assignOrderToDriver(Long orderId, Long conductorId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));
        
        Conductor conductor = conductorService.getDriverById(conductorId)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));
        
        if (!conductor.isDisponible()) {
            throw new IllegalStateException("El conductor no está disponible");
        }
        
        order.setConductor(conductor);
        order.setEstado("EN_PROGRESO");
        order.setFechaAsignacion(LocalDateTime.now());
        
        return orderRepository.save(order);
    }

    public Map<String, Object> generateDailyReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
        
        Map<String, Object> report = new HashMap<>();
        report.put("totalOrders", orders.size());
        report.put("totalRevenue", orders.stream()
            .mapToDouble(Order::getTotalAmount)
            .sum());
        
        // Estadísticas por día
        Map<LocalDate, Long> ordersByDay = orders.stream()
            .collect(Collectors.groupingBy(
                order -> order.getCreatedAt().toLocalDate(),
                Collectors.counting()
            ));
        report.put("ordersByDay", ordersByDay);
        
        // Estadísticas por estado
        Map<String, Long> ordersByStatus = orders.stream()
            .collect(Collectors.groupingBy(
                Order::getEstado,
                Collectors.counting()
            ));
        report.put("ordersByStatus", ordersByStatus);
        
        // Top conductores
        Map<Conductor, Long> topDrivers = orders.stream()
            .filter(order -> order.getConductor() != null)
            .collect(Collectors.groupingBy(
                Order::getConductor,
                Collectors.counting()
            ));
        Object LinkedHashMap;
        report.put("topDrivers", topDrivers.entrySet().stream()
            .sorted(Map.Entry.<Conductor, Long>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            )));
        
        return report;
    }

    public Map<String, Object> generateWeeklyReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
        
        Map<String, Object> report = new HashMap<>();
        report.put("totalOrders", orders.size());
        report.put("totalRevenue", orders.stream()
            .mapToDouble(Order::getTotalAmount)
            .sum());
        
        // Estadísticas por semana
        Map<String, Long> ordersByWeek = orders.stream()
            .collect(Collectors.groupingBy(
                order -> "Semana " + order.getCreatedAt().getDayOfYear() / 7,
                Collectors.counting()
            ));
        report.put("ordersByWeek", ordersByWeek);
        
        // Ingresos por semana
        Map<String, Double> revenueByWeek = orders.stream()
            .collect(Collectors.groupingBy(
                order -> "Semana " + order.getCreatedAt().getDayOfYear() / 7,
                Collectors.summingDouble(Order::getTotalAmount)
            ));
        report.put("revenueByWeek", revenueByWeek);
        
        // Conductores más activos
        Map<Conductor, Long> activeDrivers = orders.stream()
            .filter(order -> order.getConductor() != null)
            .collect(Collectors.groupingBy(
                Order::getConductor,
                Collectors.counting()
            ));
            
        // Ordenar y limitar los conductores más activos
        Map<Conductor, Long> topDrivers = new LinkedHashMap<>();
        activeDrivers.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(10)
            .forEach(entry -> topDrivers.put(entry.getKey(), entry.getValue()));
            
        report.put("activeDrivers", topDrivers);
        
        return report;
    }

    public Map<String, Object> generateMonthlyReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
        
        Map<String, Object> report = new HashMap<>();
        report.put("totalOrders", orders.size());
        report.put("totalRevenue", orders.stream()
            .mapToDouble(Order::getTotalAmount)
            .sum());
        
        // Estadísticas por mes
        Map<String, Long> ordersByMonth = orders.stream()
            .collect(Collectors.groupingBy(
                order -> order.getCreatedAt().getMonth().toString(),
                Collectors.counting()
            ));
        report.put("ordersByMonth", ordersByMonth);
        
        // Ingresos por mes
        Map<String, Double> revenueByMonth = orders.stream()
            .collect(Collectors.groupingBy(
                order -> order.getCreatedAt().getMonth().toString(),
                Collectors.summingDouble(Order::getTotalAmount)
            ));
        report.put("revenueByMonth", revenueByMonth);
        
        // Tasa de crecimiento
        double growthRate = calculateGrowthRate(orders);
        report.put("growthRate", growthRate);
        
        // Análisis de tendencias
        Map<String, Object> trendAnalysis = analyzeTrends(orders);
        report.put("trendAnalysis", trendAnalysis);
        
        return report;
    }

    private double calculateGrowthRate(List<Order> orders) {
        if (orders.size() < 2) return 0.0;
        
        // Agrupar órdenes por mes
        Map<YearMonth, Long> ordersByMonth = orders.stream()
            .collect(Collectors.groupingBy(
                order -> YearMonth.from(order.getCreatedAt()),
                Collectors.counting()
            ));
        
        // Calcular tasa de crecimiento
        List<Long> monthlyCounts = new ArrayList<>(ordersByMonth.values());
        double sum = 0.0;
        for (int i = 1; i < monthlyCounts.size(); i++) {
            double growth = (monthlyCounts.get(i) - monthlyCounts.get(i-1)) / (double) monthlyCounts.get(i-1);
            sum += growth;
        }
        
        return sum / (monthlyCounts.size() - 1);
    }

    private Map<String, Object> analyzeTrends(List<Order> orders) {
        Map<String, Object> trends = new HashMap<>();
        
        // Tendencias por día de la semana
        Map<String, Long> ordersByDayOfWeek = orders.stream()
            .collect(Collectors.groupingBy(
                order -> order.getCreatedAt().getDayOfWeek().toString(),
                Collectors.counting()
            ));
        trends.put("ordersByDayOfWeek", ordersByDayOfWeek);
        
        // Tendencias por hora del día
        Map<Integer, Long> ordersByHour = orders.stream()
            .collect(Collectors.groupingBy(
                order -> order.getCreatedAt().getHour(),
                Collectors.counting()
            ));
        trends.put("ordersByHour", ordersByHour);
        
        // Tendencias por estado
        Map<String, Long> ordersByStatus = orders.stream()
            .collect(Collectors.groupingBy(
                Order::getEstado,
                Collectors.counting()
            ));
        trends.put("ordersByStatus", ordersByStatus);
        
        return trends;
    }

    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        // Actualizar solo los campos permitidos
        existingOrder.setDireccionOrigen(order.getDireccionOrigen());
        existingOrder.setDireccionDestino(order.getDireccionDestino());
        existingOrder.setDescripcion(order.getDescripcion());
        existingOrder.setPeso(order.getPeso());
        existingOrder.setFechaAsignacion(order.getFechaAsignacion());
        existingOrder.setTotalAmount(order.getTotalAmount());
        existingOrder.setCliente(order.getCliente());
        existingOrder.setEstado(order.getEstado());
        
        return orderRepository.save(existingOrder);
    }
}
