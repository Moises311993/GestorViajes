package com.transportes.urgentes.gestion_viajes.dashboards;

import com.transportes.urgentes.gestion_viajes.drivers.ConductorService;
import com.transportes.urgentes.gestion_viajes.orders.OrderService;
import com.transportes.urgentes.gestion_viajes.users.User;
import com.transportes.urgentes.gestion_viajes.users.UserRole;
import com.transportes.urgentes.gestion_viajes.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConductorService conductorService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.ADMIN) {
            return "redirect:/login";
        }
        
        // Asegurarse de que el usuario esté en el modelo
        model.addAttribute("user", user);
        model.addAttribute("totalUsers", userService.countAllUsers());
        model.addAttribute("activeDrivers", conductorService.countActiveDrivers());
        model.addAttribute("pendingOrders", orderService.countPendingOrders().size());
        return "admin/dashboard";
    }

    // ... resto del código existente ...
} 