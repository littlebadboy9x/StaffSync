package com.example.staffsync.repository;

import com.example.staffsync.Entity.StaffMajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffMajorFacilityRepository extends JpaRepository<StaffMajorFacility, UUID> {
    List<StaffMajorFacility> findByMajorFacilityId(UUID majorFacilityId);
    List<StaffMajorFacility> findByStaffId(UUID staffId);
    boolean existsByStaffIdAndMajorFacilityId(UUID staffId, UUID majorFacilityId);
    StaffMajorFacility findByStaffIdAndMajorFacilityId(UUID staffId, UUID majorFacilityId);
    boolean existsByStaffIdAndMajorFacility_DepartmentFacility_FacilityId(UUID staffId, UUID facilityId);
    
    @Query("SELECT smf FROM StaffMajorFacility smf WHERE smf.majorFacility.departmentFacility.facility.id = :facilityId")
    List<StaffMajorFacility> findByFacilityId(@Param("facilityId") UUID facilityId);
}

