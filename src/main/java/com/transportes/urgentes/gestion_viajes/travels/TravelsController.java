package com.transportes.urgentes.gestion_viajes.travels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelsController {

    @Autowired
    private TravelService travelService;

    @PostMapping
    public ResponseEntity<Travel> createTravel(@RequestBody TravelDTO travelDTO) {
        return ResponseEntity.ok(travelService.createTravel(travelDTO));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Travel> updateStatus(
            @PathVariable Long id,
            @RequestParam String newStatus) {
        return ResponseEntity.ok(travelService.updateTravelStatus(id, newStatus));
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<Travel> updateLocation(
            @PathVariable Long id,
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        return ResponseEntity.ok(travelService.updateLocation(id, latitud, longitud));
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<Travel> updateProgress(
            @PathVariable Long id,
            @RequestParam Double distanciaRecorrida,
            @RequestParam Double velocidadActual) {
        return ResponseEntity.ok(travelService.updateProgress(id, distanciaRecorrida, velocidadActual));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Travel> getTravel(@PathVariable Long id) {
        return travelService.getTravelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Travel>> getAllTravels() {
        return ResponseEntity.ok(travelService.getAllTravels());
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Travel>> getTravelsByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(travelService.getTravelsByDriver(driverId));
    }

    @GetMapping("/tracking/{trackingCode}")
    public ResponseEntity<Travel> getTravelByTrackingCode(@PathVariable String trackingCode) {
        return travelService.getTravelByTrackingCode(trackingCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
