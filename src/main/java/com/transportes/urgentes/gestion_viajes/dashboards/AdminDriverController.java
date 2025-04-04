package com.transportes.urgentes.gestion_viajes.controllers;

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

@Controller
@RequestMapping("/admin/drivers")
public class AdminDriverController {

    @Autowired
    private ConductorService conductorService;

    @GetMapping
    public String listDrivers(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (currentUser.getRole() != UserRole.ADMIN) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", currentUser);
        model.addAttribute("drivers", conductorService.getAllDrivers());
        return "admin/drivers";
    }

    @GetMapping("/new")
    public String showNewDriverForm(Model model) {
        model.addAttribute("driver", new Conductor());
        return "admin/new-driver";
    }

    @PostMapping
    public String createDriver(@ModelAttribute Conductor driver) {
        conductorService.save(driver);
        return "redirect:/admin/drivers";
    }

    @GetMapping("/{id}/edit")
    public String showEditDriverForm(@PathVariable Long id, Model model) {
        Conductor driver = conductorService.getDriverById(id)
            .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
        model.addAttribute("driver", driver);
        return "admin/edit-driver";
    }

    @PostMapping("/{id}")
    public String updateDriver(@PathVariable Long id, @ModelAttribute Conductor driver) {
        conductorService.updateDriver(driver.getId(),driver);
        return "redirect:/admin/drivers";
    }

    @PostMapping("/{id}/delete")
    public String deleteDriver(@PathVariable Long id) {
        conductorService.deleteDriver(id);
        return "redirect:/admin/drivers";
    }
} 