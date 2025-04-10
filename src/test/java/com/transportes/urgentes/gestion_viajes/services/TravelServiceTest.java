package com.transportes.urgentes.gestion_viajes.services;

import com.transportes.urgentes.gestion_viajes.drivers.Conductor;
import com.transportes.urgentes.gestion_viajes.travels.Travel;
import com.transportes.urgentes.gestion_viajes.travels.TravelDTO;
import com.transportes.urgentes.gestion_viajes.travels.TravelRepository;
import com.transportes.urgentes.gestion_viajes.travels.TravelService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TravelServiceTest {

    @Mock
    private TravelRepository travelRepository;

    @InjectMocks
    private TravelService travelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTravel_Success() {
        // Arrange
        TravelDTO travelDTO = new TravelDTO();
        travelDTO.setOrigen("Ciudad de México");
        travelDTO.setDestino("Guadalajara");
        travelDTO.setEstado("PENDIENTE");
        travelDTO.setFechaInicio(LocalDateTime.now());
        travelDTO.setFechaEstimadaLlegada(LocalDateTime.now().plusHours(6));
        travelDTO.setDistanciaTotal(500.0);
        travelDTO.setDistanciaRecorrida(0.0);
        travelDTO.setVelocidadActual(0.0);
        travelDTO.setCodigoSeguimiento(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        Travel travel = new Travel();
        travel.setOrigen(travelDTO.getOrigen());
        travel.setDestino(travelDTO.getDestino());
        travel.setEstado(travelDTO.getEstado());
        travel.setFechaInicio(travelDTO.getFechaInicio());
        travel.setFechaEstimadaLlegada(travelDTO.getFechaEstimadaLlegada());
        travel.setDistanciaTotal(travelDTO.getDistanciaTotal());
        travel.setDistanciaRecorrida(travelDTO.getDistanciaRecorrida());
        travel.setVelocidadActual(travelDTO.getVelocidadActual());
        travel.setCodigoSeguimiento(travelDTO.getCodigoSeguimiento());

        Conductor driver = new Conductor();
        driver.setId(1L);
        travel.setConductor(driver);

        when(travelRepository.save(any(Travel.class))).thenReturn(travel);

        // Act
        Travel createdTravel = travelService.createTravel(travelDTO);

        // Assert
        assertNotNull(createdTravel);
        assertEquals("Ciudad de México", createdTravel.getOrigen());
        assertEquals("Guadalajara", createdTravel.getDestino());
        assertEquals("PENDIENTE", createdTravel.getEstado());
        assertEquals(500.0, createdTravel.getDistanciaTotal());
        assertEquals(0.0, createdTravel.getDistanciaRecorrida());
        assertEquals(0.0, createdTravel.getVelocidadActual());
        assertNotNull(createdTravel.getCodigoSeguimiento());
        assertEquals(driver.getId(), createdTravel.getConductor().getId());

        verify(travelRepository).save(any(Travel.class));
    }

    @Test
    void getTravelByTrackingCode_Success() {
        // Arrange
        String trackingCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Travel travel = new Travel();
        travel.setCodigoSeguimiento(trackingCode);
        travel.setEstado("EN_PROGRESO");
        travel.setDistanciaRecorrida(200.0);
        travel.setDistanciaTotal(500.0);

        when(travelRepository.findByCodigoSeguimiento(anyString())).thenReturn(Optional.of(travel));

        // Act
        Travel foundTravel = travelService.getTravelByTrackingCode(trackingCode).orElse(null);

        // Assert
        assertNotNull(foundTravel);
        assertEquals(trackingCode, foundTravel.getCodigoSeguimiento());
        assertEquals("EN_PROGRESO", foundTravel.getEstado());
        assertEquals(200.0, foundTravel.getDistanciaRecorrida());
        assertEquals(500.0, foundTravel.getDistanciaTotal());

        verify(travelRepository).findByCodigoSeguimiento(trackingCode);
    }


    @Test
    void updateTravelStatus_TravelNotFound() {
        // Arrange
        when(travelRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> travelService.updateTravelStatus(1L, "EN_PROGRESO"));
        verify(travelRepository).findById(1L);
    }

} 