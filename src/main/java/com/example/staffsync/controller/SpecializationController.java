package com.example.staffsync.controller;

import com.example.staffsync.Service.SpecializationService;
import com.example.staffsync.Entity.*;
import com.example.staffsync.dto.StaffMajorFacilityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specializations")
@CrossOrigin
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @GetMapping("/facilities")
    public ResponseEntity<List<Facility>> getAllFacilities() {
        return ResponseEntity.ok(specializationService.getAllFacilities());
    }

    @GetMapping("/facilities/{facilityId}/departments")
    public ResponseEntity<List<DepartmentFacility>> getDepartmentsByFacility(
            @PathVariable UUID facilityId) {
        return ResponseEntity.ok(specializationService.getDepartmentsByFacility(facilityId));
    }

    @GetMapping("/department-facilities/{departmentFacilityId}/majors")
    public ResponseEntity<List<MajorFacility>> getMajorsByDepartmentFacility(
            @PathVariable UUID departmentFacilityId) {
        return ResponseEntity.ok(specializationService.getMajorsByDepartmentFacility(departmentFacilityId));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<StaffMajorFacilityDTO>> getStaffSpecializations(
            @PathVariable UUID staffId) {
        return ResponseEntity.ok(specializationService.getStaffSpecializations(staffId));
    }

    @PostMapping("/staff/{staffId}/major-facilities/{majorFacilityId}")
    public ResponseEntity<StaffMajorFacilityDTO> addStaffSpecialization(
            @PathVariable UUID staffId,
            @PathVariable UUID majorFacilityId) {
        return ResponseEntity.ok(specializationService.addStaffSpecialization(staffId, majorFacilityId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStaffSpecialization(@PathVariable UUID id) {
        specializationService.removeStaffSpecialization(id);
        return ResponseEntity.ok().build();
    }
}