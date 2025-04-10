package com.transportes.urgentes.gestion_viajes.dashboards;

import com.transportes.urgentes.gestion_viajes.orders.Order;
import com.transportes.urgentes.gestion_viajes.orders.OrderService;
import com.transportes.urgentes.gestion_viajes.drivers.Conductor;
import com.transportes.urgentes.gestion_viajes.drivers.ConductorService;
import com.transportes.urgentes.gestion_viajes.users.User;
import com.transportes.urgentes.gestion_viajes.users.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ConductorService conductorService;

    @GetMapping
    public String listOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (currentUser.getRole() != UserRole.ADMIN) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", currentUser);
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/order-management";
    }

    @GetMapping("/{id}/manage")
    public String manageOrder(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.ADMIN) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(id);
        List<Conductor> availableDrivers = conductorService.getAvailableDrivers();
        
        model.addAttribute("order", order);
        model.addAttribute("availableDrivers", availableDrivers);
        return "admin/manage-order";
    }

    @PostMapping("/{id}/update-status")
    public String updateOrderStatus(@PathVariable Long id, 
                                  @RequestParam String newStatus,
                                  RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.ADMIN) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderById(id);
            order.setEstado(newStatus);
            orderService.updateOrder(id, order);
            redirectAttributes.addFlashAttribute("success", "Estado de la orden actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el estado: " + e.getMessage());
        }
        
        return "redirect:/admin/orders";
    }

    @PostMapping("/{id}/assign-driver")
    public String assignDriver(@PathVariable Long id,
                             @RequestParam Long driverId,
                             RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.ADMIN) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderById(id);
            Conductor driver = conductorService.getConductorById(driverId);
            
            order.setConductor(driver);
            order.setEstado("EN_PROGRESO");
            orderService.updateOrder(id, order);
            
            redirectAttributes.addFlashAttribute("success", "Conductor asignado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo asignar el conductor: " + e.getMessage());
        }
        
        return "redirect:/admin/orders";
    }

    @PostMapping("/{id}/complete")
    public String completeOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.ADMIN) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderById(id);
            order.setEstado("COMPLETADA");
            orderService.updateOrder(id, order);
            redirectAttributes.addFlashAttribute("success", "Orden completada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo completar la orden: " + e.getMessage());
        }
        
        return "redirect:/admin/orders";
    }
} 