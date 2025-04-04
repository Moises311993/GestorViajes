package com.transportes.urgentes.gestion_viajes.controllers;

import com.transportes.urgentes.gestion_viajes.drivers.Conductor;
import com.transportes.urgentes.gestion_viajes.drivers.ConductorService;
import com.transportes.urgentes.gestion_viajes.orders.Order;
import com.transportes.urgentes.gestion_viajes.orders.OrderService;
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
@RequestMapping("/driver")
public class DriverDashboardController {

    @Autowired
    private ConductorService conductorService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.DRIVER) {
            return "redirect:/login";
        }

        Conductor conductor = (Conductor) user;
        List<Order> activeOrders = orderService.getActiveOrdersByDriver(conductor.getId());
        
        model.addAttribute("conductor", conductor);
        model.addAttribute("activeOrders", activeOrders);
        return "driver/dashboard";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.DRIVER) {
            return "redirect:/login";
        }

        Conductor conductor = (Conductor) user;
        List<Order> allOrders = orderService.getActiveOrdersByDriver(conductor.getId());
        
        model.addAttribute("conductor", conductor);
        model.addAttribute("orders", allOrders);
        return "driver/orders";
    }

    @PostMapping("/toggle-availability")
    public String toggleAvailability(RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.DRIVER) {
            return "redirect:/login";
        }

        Conductor conductor = (Conductor) user;
        conductor.setDisponible(!conductor.isDisponible());
        conductorService.updateDriver(conductor.getId(), conductor);
        
        String message = conductor.isDisponible() ? 
            "Ahora estás disponible para recibir órdenes" : 
            "Ya no estás disponible para recibir órdenes";
            
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/driver/dashboard";
    }

    @PostMapping("/orders/{orderId}/accept")
    public String acceptOrder(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.DRIVER) {
            return "redirect:/login";
        }

        try {
            Conductor conductor = (Conductor) user;
            Order order = orderService.assignOrderToDriver(orderId, conductor.getId());
            redirectAttributes.addFlashAttribute("success", "Orden aceptada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo aceptar la orden: " + e.getMessage());
        }
        
        return "redirect:/driver/orders";
    }

    @PostMapping("/orders/{orderId}/complete")
    public String completeOrder(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.DRIVER) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.completeOrder(orderId);
            redirectAttributes.addFlashAttribute("success", "Orden completada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo completar la orden: " + e.getMessage());
        }
        
        return "redirect:/driver/orders";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.DRIVER) {
            return "redirect:/login";
        }

        Conductor conductor = (Conductor) user;
        model.addAttribute("conductor", conductor);
        return "driver/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Conductor conductor, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.DRIVER) {
            return "redirect:/login";
        }

        try {
            Conductor existingConductor = (Conductor) user;
            conductor.setId(existingConductor.getId());
            conductor.setUsername(existingConductor.getUsername());
            conductor.setPassword(existingConductor.getPassword());
            conductor.setRole(UserRole.DRIVER);
            
            conductorService.updateDriver(conductor.getId(), conductor);
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el perfil: " + e.getMessage());
        }
        
        return "redirect:/driver/profile";
    }
} 