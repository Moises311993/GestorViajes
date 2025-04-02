package com.transportes.urgentes.gestion_viajes.travels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viajes")
public class TravelsController {
    @Autowired
    private TravelService viajeService;

    @PostMapping
    public Travel crearViaje(@RequestBody Travel viaje) {
        return viajeService.crearViaje(viaje);
    }

    @GetMapping
    public List<Travel> obtenerViajes() {
        return viajeService.obtenerTodos();
    }
}
