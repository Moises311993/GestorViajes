package com.transportes.urgentes.gestion_viajes.drivers;

import com.transportes.urgentes.gestion_viajes.exception.ResourceNotFoundException;
import com.transportes.urgentes.gestion_viajes.users.User;
import com.transportes.urgentes.gestion_viajes.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConductorService {

    @Autowired
    private ConductorRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Conductor> getDriverByUsername(String username) {
        return driverRepository.findByUsername(username);
    }

    @Transactional
    public Conductor updateAvailability(Long driverId, boolean disponible) {
        Conductor driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));
        
        driver.setDisponible(disponible);
        return driverRepository.save(driver);
    }

    public List<Conductor> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Optional<Conductor> getDriverById(Long id) {
        return driverRepository.findById(id);
    }

    @Transactional
    public Conductor createDriver(Conductor driver) {
        if (driverRepository.existsByUsername(driver.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if (driverRepository.existsByEmail(driver.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }
        
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));
        return driverRepository.save(driver);
    }

    @Transactional
    public Conductor updateDriver(Long id, Conductor driverDetails) {
        Conductor driver = driverRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));
            
        driver.setDisponible(driverDetails.isDisponible());
        driver.setNombre(driverDetails.getNombre());
        driver.setApellido(driverDetails.getApellido());
        driver.setEmail(driverDetails.getEmail());
        driver.setTipoLicencia(driverDetails.getTipoLicencia());
        driver.setLicenciaConduccion(driverDetails.getLicenciaConduccion());
        driver.setCalificacion(driverDetails.getCalificacion());
        driver.setViajesCompletados(driverDetails.getViajesCompletados());
        
        return driverRepository.save(driver);
    }

    @Transactional
    public void deleteDriver(Long id) {
        Conductor driver = driverRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));
        driverRepository.delete(driver);
    }

    public Optional<Conductor> findByUserId(Long userId) {
        return driverRepository.findById(userId);
    }

    public Conductor save(Conductor conductor) {
        return driverRepository.save(conductor);
    }

    public long countActiveDrivers() {
        return driverRepository.countByDisponibleIsTrue();
    }

    public Map<String, Object> getDriverStatistics() {
        List<Conductor> allDrivers = driverRepository.findAll();
        
        return Map.of(
            "totalDrivers", allDrivers.size(),
            "activeDrivers", allDrivers.stream()
                .filter(Conductor::isDisponible)
                .count(),
            "averageRating", allDrivers.stream()
                .mapToDouble(Conductor::getCalificacion)
                .average()
                .orElse(0.0),
            "totalTripsCompleted", allDrivers.stream()
                .mapToInt(Conductor::getViajesCompletados)
                .sum(),
            "topDrivers", allDrivers.stream()
                .sorted((d1, d2) -> Double.compare(d2.getCalificacion(), d1.getCalificacion()))
                .limit(5)
                .collect(Collectors.toList())
        );
    }

    public List<Conductor> getAvailableDrivers() {
        return driverRepository.findByDisponibleTrue();
    }

    public Conductor getConductorById(Long driverId) {
        return driverRepository.findById(driverId).orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));
    }
}
