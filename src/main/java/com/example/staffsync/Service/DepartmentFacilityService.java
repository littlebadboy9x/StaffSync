package com.example.staffsync.Service;

import com.example.staffsync.Entity.DepartmentFacility;
import com.example.staffsync.repository.DepartmentFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentFacilityService {
    @Autowired
    private DepartmentFacilityRepository departmentFacilityRepository;

    public List<DepartmentFacility> getDepartmentsByFacility(UUID facilityId) {
        return departmentFacilityRepository.findByFacilityId(facilityId);
    }

    public List<DepartmentFacility> getFacilitiesByDepartment(UUID departmentId) {
        return departmentFacilityRepository.findByDepartmentId(departmentId);
    }
}