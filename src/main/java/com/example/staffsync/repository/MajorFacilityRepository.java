package com.example.staffsync.repository;

import com.example.staffsync.Entity.MajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MajorFacilityRepository extends JpaRepository<MajorFacility, UUID> {

    List<MajorFacility> findByDepartmentFacilityId(UUID departmentFacilityId);
}
