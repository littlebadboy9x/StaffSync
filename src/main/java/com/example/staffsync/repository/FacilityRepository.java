package com.example.staffsync.repository;

import com.example.staffsync.Entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, UUID> {
    boolean existsByCode(String code);
}

