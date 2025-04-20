package com.example.staffsync.controller;

import com.example.staffsync.Service.SpecializationService;
import com.example.staffsync.Entity.*;
import com.example.staffsync.dto.StaffDTO;
import com.example.staffsync.dto.StaffMajorFacilityDTO;
import com.example.staffsync.dto.AddSpecializationDTO;
import com.example.staffsync.dto.SpecializationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/specialized-subjects")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @GetMapping
    public String list(Model model) {
        List<SpecializationDTO> specializations = specializationService.getAllSpecializations();
        model.addAttribute("specializations", specializations);
        return "specialized-subjects/list";
    }

    @PostMapping
    public String add(@ModelAttribute AddSpecializationDTO dto, RedirectAttributes redirectAttributes) {
        try {
            specializationService.addSpecialization(dto);
            redirectAttributes.addFlashAttribute("message", "Thêm bộ môn chuyên ngành thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/specialized-subjects";
    }

    @DeleteMapping("/specialization/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            specializationService.deleteSpecialization(id);
            redirectAttributes.addFlashAttribute("message", "Xóa bộ môn chuyên ngành thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/specialized-subjects";
    }

    @GetMapping("/api/facilities/{facilityId}/departments")
    @ResponseBody
    public ResponseEntity<List<DepartmentFacility>> getDepartmentsByFacilityAsList(@PathVariable UUID facilityId) {
        return ResponseEntity.ok(specializationService.getDepartmentsByFacility(facilityId));
    }

    @GetMapping("/api/department-facilities/{departmentId}/majors")
    @ResponseBody
    public ResponseEntity<?> getMajorsByDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(specializationService.getMajorsByDepartment(departmentId));
    }

    @GetMapping("/api/facilities")
    @ResponseBody
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
        specializationService.addStaffSpecialization(staffId, majorFacilityId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/staff-specialization/{id}")
    public ResponseEntity<Void> removeStaffSpecialization(@PathVariable UUID id) {
        specializationService.removeStaffSpecialization(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/{specializationId}/staff")
    @ResponseBody
    public ResponseEntity<List<StaffDTO>> getStaffInSpecialization(@PathVariable UUID specializationId) {
        return ResponseEntity.ok(specializationService.getStaffInSpecialization(specializationId)
                .stream()
                .map(staff -> {
                    StaffDTO dto = new StaffDTO();
                    dto.setId(staff.getId());
                    dto.setName(staff.getName());
                    dto.setFptEmail(staff.getFptEmail());
                    dto.setFeEmail(staff.getFeEmail());
                    return dto;
                })
                .toList());
    }

    @PostMapping("/api/{specializationId}/staff/{staffId}")
    @ResponseBody
    public ResponseEntity<?> addStaffToSpecialization(
            @PathVariable UUID specializationId,
            @PathVariable UUID staffId) {
        try {
            specializationService.addStaffToSpecialization(specializationId, staffId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/api/{specializationId}/staff/{staffId}")
    @ResponseBody
    public ResponseEntity<?> removeStaffFromSpecialization(
            @PathVariable UUID specializationId,
            @PathVariable UUID staffId) {
        try {
            specializationService.removeStaffFromSpecialization(specializationId, staffId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String viewSpecialization(@PathVariable UUID id, Model model) {
        SpecializationDTO specialization = specializationService.getSpecializationById(id);
        List<StaffDTO> staffList = specializationService.getStaffInSpecialization(id);
        List<StaffDTO> availableStaff = specializationService.getAvailableStaff(id);
        
        model.addAttribute("specialization", specialization);
        model.addAttribute("staffList", staffList);
        model.addAttribute("availableStaff", availableStaff);
        return "specialized-subjects/specialization-detail";
    }
}