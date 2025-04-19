package com.example.staffsync.repository;

import com.example.staffsync.Entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {
}
