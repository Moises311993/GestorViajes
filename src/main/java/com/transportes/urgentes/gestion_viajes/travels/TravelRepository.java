package com.transportes.urgentes.gestion_viajes.travels;

import com.transportes.urgentes.gestion_viajes.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    Optional<Travel> findByConductor(User user);
}