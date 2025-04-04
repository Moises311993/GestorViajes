package com.transportes.urgentes.gestion_viajes.controllers;

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
@RequestMapping("/client")
public class ClientDashboardController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        List<Order> activeOrders = orderService.getActiveOrdersByClient(user.getId());
        
        model.addAttribute("user", user);
        model.addAttribute("activeOrders", activeOrders);
        return "client/dashboard";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        List<Order> allOrders = orderService.getOrdersByClient(user.getId());
        
        model.addAttribute("user", user);
        model.addAttribute("orders", allOrders);
        return "client/orders";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "client/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = (User) auth.getPrincipal();
        
        if (existingUser.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        try {
            user.setId(existingUser.getId());
            user.setUsername(existingUser.getUsername());
            user.setPassword(existingUser.getPassword());
            user.setRole(UserRole.CLIENT);
            
            // Aquí deberías llamar al servicio de usuarios para actualizar el perfil
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el perfil: " + e.getMessage());
        }
        
        return "redirect:/client/profile";
    }
} 