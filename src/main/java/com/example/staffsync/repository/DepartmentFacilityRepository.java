package com.example.staffsync.repository;

import com.example.staffsync.Entity.DepartmentFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentFacilityRepository extends JpaRepository<DepartmentFacility, UUID> {
    List<DepartmentFacility> findByFacilityId(UUID facilityId);
    List<DepartmentFacility> findByDepartmentId(UUID departmentId);
}