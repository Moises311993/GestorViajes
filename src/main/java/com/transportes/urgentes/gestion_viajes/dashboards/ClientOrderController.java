package com.transportes.urgentes.gestion_viajes.dashboards;

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

@Controller
@RequestMapping("/client/orders")
public class ClientOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/new")
    public String showNewOrderForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("order", new Order());
        return "client/new-order";
    }

    @PostMapping
    public String createOrder(@ModelAttribute Order order, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        try {
            order.setCliente(user);
            order.setEstado("PENDIENTE");
            orderService.createOrder(order);
            redirectAttributes.addFlashAttribute("success", "Orden creada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo crear la orden: " + e.getMessage());
        }
        
        return "redirect:/client/orders";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderById(id);
            if (!order.getCliente().getId().equals(user.getId())) {
                return "redirect:/client/orders";
            }
            
            model.addAttribute("user", user);
            model.addAttribute("order", order);
            return "client/order-details";
        } catch (Exception e) {
            return "redirect:/client/orders";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditOrderForm(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderById(id);
            if (!order.getCliente().getId().equals(user.getId()) || !order.getEstado().equals("PENDIENTE")) {
                return "redirect:/client/orders";
            }
            
            model.addAttribute("user", user);
            model.addAttribute("order", order);
            return "client/edit-order";
        } catch (Exception e) {
            return "redirect:/client/orders";
        }
    }

    @PostMapping("/{id}")
    public String updateOrder(@PathVariable Long id, @ModelAttribute Order order, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        try {
            Order existingOrder = orderService.getOrderById(id);
            if (!existingOrder.getCliente().getId().equals(user.getId()) || !existingOrder.getEstado().equals("PENDIENTE")) {
                return "redirect:/client/orders";
            }
            
            order.setId(id);
            order.setCliente(user);
            order.setEstado("PENDIENTE");
            orderService.updateOrder(id, order);
            redirectAttributes.addFlashAttribute("success", "Orden actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar la orden: " + e.getMessage());
        }
        
        return "redirect:/client/orders";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        if (user.getRole() != UserRole.CLIENT) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderById(id);
            if (!order.getCliente().getId().equals(user.getId()) || !order.getEstado().equals("PENDIENTE")) {
                return "redirect:/client/orders";
            }
            
            order.setEstado("CANCELADA");
            orderService.updateOrder(id, order);
            redirectAttributes.addFlashAttribute("success", "Orden cancelada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo cancelar la orden: " + e.getMessage());
        }
        
        return "redirect:/client/orders";
    }
} 