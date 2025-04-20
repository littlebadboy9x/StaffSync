package com.example.staffsync.controller;

import com.example.staffsync.Service.SpecializationService;
import com.example.staffsync.Service.StaffService;
import com.example.staffsync.Entity.*;
import com.example.staffsync.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/specialized-subjects")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @Autowired
    private StaffService staffService;

    @GetMapping
    public String list(Model model) {
        List<SpecializationDTO> specializations = specializationService.getAllSpecializations();
        model.addAttribute("specializations", specializations);
        return "specialized-subjects/list";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> add(@RequestBody AddSpecializationDTO dto) {
        try {
            specializationService.addSpecialization(dto);
            return ResponseEntity.ok(new SuccessResponse("Thêm bộ môn chuyên ngành thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Không thể thêm bộ môn chuyên ngành", e.getMessage()));
        }
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
    public ResponseEntity<?> getDepartmentsByFacility(@PathVariable UUID facilityId) {
        try {
            List<DepartmentFacilityDTO> departments = specializationService.getDepartmentsByFacility(facilityId);
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error fetching departments", e.getMessage()));
        }
    }

    @GetMapping("/api/department-facilities/{departmentFacilityId}/majors")
    @ResponseBody
    public ResponseEntity<?> getMajorsByDepartment(@PathVariable UUID departmentFacilityId) {
        try {
            List<MajorFacilityDTO> majors = specializationService.getMajorsByDepartment(departmentFacilityId);
            return ResponseEntity.ok(majors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error fetching majors", e.getMessage()));
        }
    }

    @GetMapping("/api/facilities")
    @ResponseBody
    public ResponseEntity<List<FacilityDTO>> getAllFacilities() {
        List<Facility> facilities = specializationService.getAllFacilities();
        List<FacilityDTO> dtos = facilities.stream()
                .map(f -> {
                    FacilityDTO dto = new FacilityDTO();
                    dto.setId(f.getId());
                    dto.setName(f.getName());
                    dto.setCode(f.getCode());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/department-facilities/{departmentFacilityId}/majors")
    public ResponseEntity<List<MajorFacility>> getMajorsByDepartmentFacility(
            @PathVariable UUID departmentFacilityId) {
        return ResponseEntity.ok(specializationService.getMajorsByDepartmentFacility(departmentFacilityId));
    }

    @GetMapping("/staff/{staffId}")
    public String listByStaff(@PathVariable UUID staffId, Model model) {
        StaffDTO staff = staffService.findById(staffId);
        List<StaffMajorFacilityDTO> specializations = specializationService.getStaffSpecializations(staffId);
        List<Facility> facilities = specializationService.getAllFacilities();
        
        model.addAttribute("staff", staff);
        model.addAttribute("specializations", specializations);
        model.addAttribute("facilities", facilities);
        model.addAttribute("activeMenu", "staff");
        return "specialized-subjects/staff-specialization";
    }

    @PostMapping("/staff/{staffId}/major-facilities/{majorFacilityId}")
    public ResponseEntity<?> addStaffSpecialization(
            @PathVariable UUID staffId,
            @PathVariable UUID majorFacilityId) {
        try {
            specializationService.addStaffSpecialization(staffId, majorFacilityId);
            return ResponseEntity.ok(new SuccessResponse("Thêm chuyên ngành cho nhân viên thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Không thể thêm chuyên ngành", e.getMessage()));
        }
    }

    @DeleteMapping("/staff/{staffId}/major-facilities/{majorFacilityId}")
    public ResponseEntity<?> removeStaffSpecialization(
            @PathVariable UUID staffId,
            @PathVariable UUID majorFacilityId) {
        try {
            specializationService.removeStaffSpecialization(majorFacilityId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Không thể xóa chuyên ngành", e.getMessage()));
        }
    }

    @GetMapping("/api/{specializationId}/staff")
    @ResponseBody
    public ResponseEntity<List<StaffDTO>> getStaffInSpecialization(@PathVariable UUID specializationId) {
        List<StaffDTO> staffList = specializationService.getStaffInSpecialization(specializationId).stream()
                .map(staff -> {
                    StaffDTO dto = new StaffDTO();
                    dto.setId(staff.getId());
                    dto.setName(staff.getName());
                    dto.setFptEmail(staff.getFptEmail());
                    dto.setFeEmail(staff.getFeEmail());
                    return dto;
                })
                .collect(Collectors.toList());
        List<StaffDTO> dtos = staffList.stream()
                .map(staff -> {
                    StaffDTO dto = new StaffDTO();
                    dto.setId(staff.getId());
                    dto.setName(staff.getName());
                    dto.setFptEmail(staff.getFptEmail());
                    dto.setFeEmail(staff.getFeEmail());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
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

    @GetMapping("/staff/{staffId}/specializations")
    public String listStaffSpecializations(@PathVariable UUID staffId, Model model) {
        StaffDTO staff = staffService.findById(staffId);
        List<StaffMajorFacilityDTO> specializations = specializationService.getStaffSpecializations(staffId);
        List<Facility> facilities = specializationService.getAllFacilities();
        
        model.addAttribute("staff", staff);
        model.addAttribute("specializations", specializations);
        model.addAttribute("facilities", facilities);
        model.addAttribute("activeMenu", "staff");
        return "specialized-subjects/staff-specialization";
    }

    @GetMapping("/staff/{staffId}/download-template")
    public void downloadTemplate(@PathVariable UUID staffId, HttpServletResponse response) throws IOException {
        // Set response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=staff_specialization_template.xlsx");

        // Create workbook and sheet
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Template");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"STT", "Mã nhân viên", "Tên nhân viên", "Email FPT", "Email FE", "Chuyên ngành"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Get staff information
            StaffDTO staff = staffService.findById(staffId);
            if (staff != null) {
                Row dataRow = sheet.createRow(1);
                dataRow.createCell(0).setCellValue(1);
                dataRow.createCell(1).setCellValue(staff.getCode());
                dataRow.createCell(2).setCellValue(staff.getName());
                dataRow.createCell(3).setCellValue(staff.getFptEmail());
                dataRow.createCell(4).setCellValue(staff.getFeEmail());
                
                // Get staff's specializations
                List<StaffMajorFacilityDTO> specializations = specializationService.getStaffSpecializations(staffId);
                StringBuilder specializationText = new StringBuilder();
                for (StaffMajorFacilityDTO spec : specializations) {
                    if (specializationText.length() > 0) {
                        specializationText.append(", ");
                    }
                    specializationText.append(spec.getFacilityName())
                                     .append("-")
                                     .append(spec.getDepartmentName())
                                     .append("-")
                                     .append(spec.getMajorName());
                }
                dataRow.createCell(5).setCellValue(specializationText.toString());
            }

            // Auto-size columns
            for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Write workbook to response
            workbook.write(response.getOutputStream());
        }
    }

    private static class ErrorResponse {
        private String message;
        private String details;

        public ErrorResponse(String message, String details) {
            this.message = message;
            this.details = details;
        }

        public String getMessage() {
            return message;
        }

        public String getDetails() {
            return details;
        }
    }

    private static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}