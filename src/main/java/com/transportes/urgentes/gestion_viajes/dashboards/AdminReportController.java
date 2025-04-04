package com.transportes.urgentes.gestion_viajes.controllers;

import com.transportes.urgentes.gestion_viajes.drivers.ConductorService;
import com.transportes.urgentes.gestion_viajes.orders.OrderService;
import com.transportes.urgentes.gestion_viajes.users.User;
import com.transportes.urgentes.gestion_viajes.users.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@RequestMapping("/admin/reports")
public class AdminReportController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ConductorService conductorService;

    @GetMapping
    public String showReports(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (currentUser.getRole() != UserRole.ADMIN) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", currentUser);
        model.addAttribute("dailyOrders", orderService.getDailyOrderCount());
        model.addAttribute("monthlyRevenue", orderService.getMonthlyRevenue());
        model.addAttribute("driverStats", conductorService.getDriverStatistics());
        return "admin/reports";
    }

    @PostMapping("/generate")
    public String generateReport(@RequestParam String reportType,
                               @RequestParam String startDate,
                               @RequestParam String endDate,
                               Model model) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        
        Map<String, Object> reportData = switch (reportType) {
            case "daily" -> orderService.generateDailyReport(start, end);
            case "weekly" -> orderService.generateWeeklyReport(start, end);
            case "monthly" -> orderService.generateMonthlyReport(start, end);
            default -> throw new IllegalArgumentException("Tipo de reporte no válido");
        };
        
        model.addAttribute("reportData", reportData);
        model.addAttribute("reportType", reportType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "admin/report-details";
    }
} 