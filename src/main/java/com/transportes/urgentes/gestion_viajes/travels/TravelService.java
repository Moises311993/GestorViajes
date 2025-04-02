package com.transportes.urgentes.gestion_viajes.travels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelService {
    @Autowired
    private TravelRepository viajeRepository;

    public Travel crearViaje(Travel viaje) {
        return viajeRepository.save(viaje);
    }

    public List<Travel> obtenerTodos() {
        return viajeRepository.findAll();
    }
}

