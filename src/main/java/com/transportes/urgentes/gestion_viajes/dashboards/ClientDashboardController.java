package com.transportes.urgentes.gestion_viajes.dashboards;

import com.transportes.urgentes.gestion_viajes.orders.Order;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientDashboardController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) auth.getPrincipal();
        
        if (authUser.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        // Get a fresh user object from the database using the username
        User user = userService.getUserByUsername(authUser.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con username: " + authUser.getUsername()));

        List<Order> activeOrders = orderService.getActiveOrdersByClient(user.getId());
        
        model.addAttribute("user", user);
        model.addAttribute("activeOrders", activeOrders);
        return "client/dashboard";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) auth.getPrincipal();
        
        if (authUser.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        // Get a fresh user object from the database using the username
        User user = userService.getUserByUsername(authUser.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con username: " + authUser.getUsername()));

        List<Order> allOrders = orderService.getOrdersByClient(user.getId());
        
        model.addAttribute("user", user);
        model.addAttribute("orders", allOrders);
        return "client/orders";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) auth.getPrincipal();
        
        if (authUser.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        // Get a fresh user object from the database using the username
        User user = userService.getUserByUsername(authUser.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con username: " + authUser.getUsername()));

        model.addAttribute("user", user);
        return "client/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) auth.getPrincipal();
        
        if (authUser.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        try {
            // Get a fresh user object from the database using the username
            User existingUser = userService.getUserByUsername(authUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con username: " + authUser.getUsername()));

            user.setId(existingUser.getId());
            user.setUsername(existingUser.getUsername());
            user.setPassword(existingUser.getPassword());
            user.setRole(UserRole.CLIENT);
            
            userService.updateUser(user.getId(), user);
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el perfil: " + e.getMessage());
        }
        
        return "redirect:/client/profile";
    }
} 