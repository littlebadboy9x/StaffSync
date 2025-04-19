package com.example.staffsync.repository;

import com.example.staffsync.Entity.Campus;
import com.example.staffsync.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    List<Department> findByCampus(Campus campus);
}