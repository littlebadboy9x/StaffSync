package com.example.staffsync.Service;

import com.example.staffsync.Entity.*;
import com.example.staffsync.dto.StaffMajorFacilityDTO;
import com.example.staffsync.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SpecializationService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentFacilityRepository departmentFacilityRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private MajorFacilityRepository majorFacilityRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffMajorFacilityRepository staffMajorFacilityRepository;

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public List<DepartmentFacility> getDepartmentsByFacility(UUID facilityId) {
        return departmentFacilityRepository.findByFacilityId(facilityId);
    }

    public List<MajorFacility> getMajorsByDepartmentFacility(UUID departmentFacilityId) {
        return majorFacilityRepository.findByDepartmentFacilityId(departmentFacilityId);
    }

    public List<StaffMajorFacilityDTO> getStaffSpecializations(UUID staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + staffId));

        return staffMajorFacilityRepository.findByStaffId(staffId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StaffMajorFacilityDTO addStaffSpecialization(UUID staffId, UUID majorFacilityId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + staffId));

        MajorFacility majorFacility = majorFacilityRepository.findById(majorFacilityId)
                .orElseThrow(() -> new EntityNotFoundException("Major facility not found with id: " + majorFacilityId));

        UUID facilityId = majorFacility.getDepartmentFacility().getFacility().getId();

        // Check if staff already has a specialization in this facility
        if (staffMajorFacilityRepository.existsByStaffIdAndMajorFacilityDepartmentFacilityFacilityId(staffId, facilityId)) {
            throw new IllegalArgumentException("Staff already has a specialization in this facility");
        }

        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
        staffMajorFacility.setId(UUID.randomUUID());
        staffMajorFacility.setStaff(staff);
        staffMajorFacility.setMajorFacility(majorFacility);
        staffMajorFacility.setStatus((byte) 1);
        staffMajorFacility.setCreatedDate(System.currentTimeMillis());
        staffMajorFacility.setLastModifiedDate(System.currentTimeMillis());

        staffMajorFacility = staffMajorFacilityRepository.save(staffMajorFacility);

        return convertToDTO(staffMajorFacility);
    }

    @Transactional
    public void removeStaffSpecialization(UUID id) {
        StaffMajorFacility staffMajorFacility = staffMajorFacilityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff specialization not found with id: " + id));

        staffMajorFacilityRepository.delete(staffMajorFacility);
    }

    private StaffMajorFacilityDTO convertToDTO(StaffMajorFacility staffMajorFacility) {
        StaffMajorFacilityDTO dto = new StaffMajorFacilityDTO();
        dto.setId(staffMajorFacility.getId());
        dto.setStaffId(staffMajorFacility.getStaff().getId());
        dto.setStaffCode(staffMajorFacility.getStaff().getStaffCode());
        dto.setStaffName(staffMajorFacility.getStaff().getName());
        dto.setMajorId(staffMajorFacility.getMajorFacility().getMajor().getId());
        dto.setMajorName(staffMajorFacility.getMajorFacility().getMajor().getName());
        dto.setDepartmentId(staffMajorFacility.getMajorFacility().getDepartmentFacility().getDepartment().getId());
        dto.setDepartmentName(staffMajorFacility.getMajorFacility().getDepartmentFacility().getDepartment().getName());
        dto.setFacilityId(staffMajorFacility.getMajorFacility().getDepartmentFacility().getFacility().getId());
        dto.setFacilityName(staffMajorFacility.getMajorFacility().getDepartmentFacility().getFacility().getName());
        return dto;
    }
}
