package com.transportes.urgentes.gestion_viajes.drivers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Long> {

    Optional<Conductor> findByUsername(String username);

    long countByDisponibleIsTrue();
}
