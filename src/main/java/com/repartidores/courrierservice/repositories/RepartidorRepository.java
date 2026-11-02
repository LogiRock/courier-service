package com.repartidores.courrierservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.repartidores.courrierservice.models.Repartidor;

public interface RepartidorRepository extends JpaRepository<Repartidor, UUID> {
    Optional<Repartidor> findByUserId(UUID userId);
}
