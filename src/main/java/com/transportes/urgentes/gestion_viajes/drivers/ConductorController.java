package com.transportes.urgentes.gestion_viajes.drivers;

import com.transportes.urgentes.gestion_viajes.travels.Travel;
import com.transportes.urgentes.gestion_viajes.travels.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/driver")
public class ConductorController {

    @Autowired
    private ConductorService driverService;

    @Autowired
    private TravelService travelService;

    @PutMapping("/availability")
    @ResponseBody
    public Conductor updateAvailability(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam boolean disponible) {
        Conductor driver = driverService.getDriverByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
        return driverService.updateAvailability(driver.getId(), disponible);
    }
}
