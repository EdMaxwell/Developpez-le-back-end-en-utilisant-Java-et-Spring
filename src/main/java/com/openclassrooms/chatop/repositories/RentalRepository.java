package com.openclassrooms.chatop.repositories;

import com.openclassrooms.chatop.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA pour l'entité Rental.
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
}