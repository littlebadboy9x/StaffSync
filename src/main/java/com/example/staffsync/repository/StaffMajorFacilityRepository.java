package com.example.staffsync.repository;

import com.example.staffsync.Entity.StaffMajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffMajorFacilityRepository extends JpaRepository<StaffMajorFacility, UUID> {

    List<StaffMajorFacility> findByStaffId(UUID staffId);

    boolean existsByStaffIdAndMajorFacilityDepartmentFacilityFacilityId(UUID staffId, UUID facilityId);
}

