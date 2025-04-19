package com.example.staffsync.repository;

import com.example.staffsync.Entity.MajorFacility;
import com.example.staffsync.Entity.Staff;
import com.example.staffsync.Entity.StaffMajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffMajorFacilityRepository extends JpaRepository<StaffMajorFacility, UUID> {
    List<StaffMajorFacility> findByStaffId(UUID staffId);
    List<StaffMajorFacility> findByMajorFacilityId(UUID majorFacilityId);
    boolean existsByStaffIdAndMajorFacilityDepartmentFacilityFacilityId(UUID staffId, UUID facilityId);
    List<StaffMajorFacility> findByStaffAndMajorFacility(Staff staff, MajorFacility majorFacility);
}

