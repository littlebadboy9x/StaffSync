package com.example.staffsync.Service;

import com.example.staffsync.Entity.MajorFacility;
import com.example.staffsync.Entity.Staff;
import com.example.staffsync.Entity.StaffMajorFacility;
import com.example.staffsync.repository.MajorFacilityRepository;
import com.example.staffsync.repository.StaffMajorFacilityRepository;
import com.example.staffsync.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import com.example.staffsync.exception.ResourceNotFoundException;

@Service
public class StaffMajorFacilityService {
    @Autowired
    private StaffMajorFacilityRepository staffMajorFacilityRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private MajorFacilityRepository majorFacilityRepository;

    @Transactional
    public StaffMajorFacility assignMajorToStaff(UUID staffId, UUID majorFacilityId) {
        // Check if staff already has a major in this facility
        if (staffMajorFacilityRepository.existsByStaffIdAndMajorFacilityDepartmentFacilityFacilityId(
                staffId, majorFacilityId)) {
            throw new IllegalArgumentException("Staff already has a major in this facility");
        }

        Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
            
        MajorFacility majorFacility = majorFacilityRepository.findById(majorFacilityId)
            .orElseThrow(() -> new ResourceNotFoundException("Major Facility not found"));

        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
        staffMajorFacility.setId(UUID.randomUUID());
        staffMajorFacility.setStaff(staff);
        staffMajorFacility.setMajorFacility(majorFacility);
        staffMajorFacility.setStatus((byte) 1);
        staffMajorFacility.setCreatedDate(System.currentTimeMillis());
        staffMajorFacility.setLastModifiedDate(System.currentTimeMillis());

        return staffMajorFacilityRepository.save(staffMajorFacility);
    }

    @Transactional
    public void removeMajorFromStaff(UUID staffId, UUID majorFacilityId) {
        Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        
        MajorFacility majorFacility = majorFacilityRepository.findById(majorFacilityId)
            .orElseThrow(() -> new ResourceNotFoundException("Major Facility not found"));

        List<StaffMajorFacility> assignments = staffMajorFacilityRepository
            .findByStaffAndMajorFacility(staff, majorFacility);
        staffMajorFacilityRepository.deleteAll(assignments);
    }
}