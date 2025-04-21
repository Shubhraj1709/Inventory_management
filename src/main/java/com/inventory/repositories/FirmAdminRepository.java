package com.inventory.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory.entities.FirmAdmin;

@Repository
public interface FirmAdminRepository extends JpaRepository<FirmAdmin, Long> {
    Optional<FirmAdmin> findByEmail(String email);
}
