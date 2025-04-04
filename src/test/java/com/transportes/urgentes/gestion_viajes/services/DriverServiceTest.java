package com.transportes.urgentes.gestion_viajes.services;

import com.transportes.urgentes.gestion_viajes.drivers.Conductor;
import com.transportes.urgentes.gestion_viajes.drivers.ConductorRepository;
import com.transportes.urgentes.gestion_viajes.drivers.ConductorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DriverServiceTest {

    @Mock
    private ConductorRepository driverRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ConductorService driverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDriver_Success() {
        // Arrange
        Conductor driver = new Conductor();
        driver.setUsername("driver1");
        driver.setPassword("password");
        driver.setEmail("driver1@example.com");
        driver.setLicenciaConduccion("LIC123456");
        driver.setTipoLicencia("B");
        driver.setDisponible(true);

        when(driverRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(driverRepository.save(any(Conductor.class))).thenReturn(driver);

        // Act
        Conductor createdDriver = driverService.createDriver(driver);

        // Assert
        assertNotNull(createdDriver);
        assertEquals("driver1", createdDriver.getUsername());
        assertEquals("encodedPassword", createdDriver.getPassword());
        assertEquals("driver1@example.com", createdDriver.getEmail());
        assertEquals("LIC123456", createdDriver.getLicenciaConduccion());
        assertEquals("B", createdDriver.getTipoLicencia());
        assertTrue(createdDriver.isDisponible());

        verify(driverRepository).findByUsername("driver1");
        verify(passwordEncoder).encode("password");
        verify(driverRepository).save(any(Conductor.class));
    }

    @Test
    void createDriver_DuplicateUsername() {
        // Arrange
        Conductor driver = new Conductor();
        driver.setUsername("existingdriver");
        driver.setPassword("password");

        when(driverRepository.findByUsername(anyString())).thenReturn(Optional.of(new Conductor()));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> driverService.createDriver(driver));
        verify(driverRepository).findByUsername("existingdriver");
    }

    @Test
    void getDriverByUsername_Success() {
        // Arrange
        Conductor driver = new Conductor();
        driver.setUsername("driver1");
        driver.setEmail("driver1@example.com");

        when(driverRepository.findByUsername(anyString())).thenReturn(Optional.of(driver));

        // Act
        Conductor foundDriver = driverService.getDriverByUsername("driver1").orElse(null);

        // Assert
        assertNotNull(foundDriver);
        assertEquals("driver1", foundDriver.getUsername());
        assertEquals("driver1@example.com", foundDriver.getEmail());

        verify(driverRepository).findByUsername("driver1");
    }

    @Test
    void getDriverByUsername_NotFound() {
        // Arrange
        when(driverRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> driverService.getDriverByUsername("nonexistent"));
        verify(driverRepository).findByUsername("nonexistent");
    }

    @Test
    void updateAvailability_Success() {
        // Arrange
        Conductor driver = new Conductor();
        driver.setId(1L);
        driver.setDisponible(false);

        when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Conductor.class))).thenReturn(driver);

        // Act
        driverService.updateAvailability(1L, true);

        // Assert
        assertTrue(driver.isDisponible());
        verify(driverRepository).findById(1L);
        verify(driverRepository).save(any(Conductor.class));
    }

    @Test
    void updateAvailability_DriverNotFound() {
        // Arrange
        when(driverRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> driverService.updateAvailability(1L, true));
        verify(driverRepository).findById(1L);
    }

    @Test
    void getAllDrivers_Success() {
        // Arrange
        Conductor driver1 = new Conductor();
        driver1.setUsername("driver1");
        Conductor driver2 = new Conductor();
        driver2.setUsername("driver2");

        when(driverRepository.findAll()).thenReturn(Arrays.asList(driver1, driver2));

        // Act
        var drivers = driverService.getAllDrivers();

        // Assert
        assertNotNull(drivers);
        assertEquals(2, drivers.size());
        assertEquals("driver1", drivers.get(0).getUsername());
        assertEquals("driver2", drivers.get(1).getUsername());

        verify(driverRepository).findAll();
    }
} 