package com.transportes.urgentes.gestion_viajes.travels;

import com.transportes.urgentes.gestion_viajes.users.User;
import com.transportes.urgentes.gestion_viajes.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Travel createTravel(TravelDTO travelDTO) {
        Travel travel = new Travel();
        travel.setOrigen(travelDTO.getOrigen());
        travel.setDestino(travelDTO.getDestino());
        travel.setEstado("PENDIENTE");
        travel.setFechaInicio(LocalDateTime.now());
        travel.setFechaEstimadaLlegada(travelDTO.getFechaEstimadaLlegada());
        travel.setDistanciaTotal(travelDTO.getDistanciaTotal());
        travel.setCodigoSeguimiento(generateTrackingCode());
        
        if (travelDTO.getConductorId() != null) {
            User conductor = userService.getUserById(travelDTO.getConductorId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
            travel.setConductor(conductor);
        }

        return travelRepository.save(travel);
    }

    @Transactional
    public Travel updateTravelStatus(Long travelId, String newStatus) {
        Travel travel = getTravelById(travelId)
            .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        
        travel.setEstado(newStatus);
        travel.setUltimaActualizacion(LocalDateTime.now());
        
        if ("COMPLETADO".equals(newStatus)) {
            travel.setFechaRealLlegada(LocalDateTime.now());
        }
        
        return travelRepository.save(travel);
    }

    @Transactional
    public Travel updateLocation(Long travelId, Double latitud, Double longitud) {
        Travel travel = getTravelById(travelId)
            .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        
        travel.setLatitudActual(latitud);
        travel.setLongitudActual(longitud);
        travel.setUltimaActualizacion(LocalDateTime.now());
        
        return travelRepository.save(travel);
    }

    @Transactional
    public Travel updateProgress(Long travelId, Double distanciaRecorrida, Double velocidadActual) {
        Travel travel = getTravelById(travelId)
            .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        
        travel.setDistanciaRecorrida(distanciaRecorrida);
        travel.setVelocidadActual(velocidadActual);
        travel.setUltimaActualizacion(LocalDateTime.now());
        
        // Calcular tiempo estimado restante
        if (velocidadActual > 0 && travel.getDistanciaTotal() > 0) {
            double distanciaRestante = travel.getDistanciaTotal() - distanciaRecorrida;
            int tiempoRestante = (int) (distanciaRestante / velocidadActual * 60); // en minutos
            travel.setTiempoEstimadoRestante(tiempoRestante);
        }
        
        return travelRepository.save(travel);
    }

    public Optional<Travel> getTravelById(Long id) {
        return travelRepository.findById(id);
    }

    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    public List<Travel> getTravelsByDriver(Long driverId) {
        return travelRepository.findByConductorId(driverId);
    }

    public Optional<Travel> getTravelByTrackingCode(String trackingCode) {
        return travelRepository.findByCodigoSeguimiento(trackingCode);
    }

    private String generateTrackingCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void updateTravelLocation(long l, double d, double e) {
    }
}

