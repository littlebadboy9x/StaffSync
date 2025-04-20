package com.example.staffsync.Service;

import com.example.staffsync.Entity.*;
import com.example.staffsync.dto.StaffDTO;
import com.example.staffsync.dto.StaffMajorFacilityDTO;
import com.example.staffsync.dto.AddSpecializationDTO;
import com.example.staffsync.dto.SpecializationDTO;
import com.example.staffsync.dto.DepartmentFacilityDTO;
import com.example.staffsync.dto.MajorFacilityDTO;

import java.util.List;
import java.util.UUID;

public interface SpecializationService {
    List<SpecializationDTO> getAllSpecializations();
    SpecializationDTO getSpecializationById(UUID id);
    void addSpecialization(AddSpecializationDTO dto);
    void deleteSpecialization(UUID id);
    List<DepartmentFacilityDTO> getDepartmentsByFacility(UUID facilityId);
    List<MajorFacilityDTO> getMajorsByDepartment(UUID departmentId);
    List<Facility> getAllFacilities();
    List<MajorFacility> getMajorsByDepartmentFacility(UUID departmentFacilityId);
    List<StaffMajorFacilityDTO> getStaffSpecializations(UUID staffId);
    void addStaffSpecialization(UUID staffId, UUID majorFacilityId);
    void removeStaffSpecialization(UUID id);
    List<StaffDTO> getStaffInSpecialization(UUID specializationId);
    void addStaffToSpecialization(UUID specializationId, UUID staffId);
    void removeStaffFromSpecialization(UUID specializationId, UUID staffId);
    List<StaffDTO> getAvailableStaff(UUID id);
    void updateSpecialization(UUID id, AddSpecializationDTO dto);
}
