package com.example.staffsync.controller;

import com.example.staffsync.dto.StaffDTO;
import com.example.staffsync.Service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/staff")
@CrossOrigin
public class StaffController {

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    private StaffService staffService;

    // Lấy danh sách tất cả nhân viên
    @GetMapping
    public String getAllStaff(Model model) {
        logger.info("Getting all staff");
        List<StaffDTO> staffList = staffService.findAll();
        model.addAttribute("staffList", staffList);
        model.addAttribute("activeMenu", "staff");
        return "staff/staffList";
    }

    // Lấy thông tin chi tiết nhân viên
    @GetMapping("/{id}")
    public String getStaffById(@PathVariable UUID id, Model model) {
        logger.info("Getting staff with id: {}", id);
        StaffDTO staff = staffService.findById(id);
        model.addAttribute("staff", staff);
        model.addAttribute("isNew", false);
        model.addAttribute("activeMenu", "staff");
        return "staff/staffDetail";
    }

    // Hiển thị form thêm nhân viên
    @GetMapping("/new")
    public String showAddStaffForm(Model model) {
        logger.info("Showing add staff form");
        model.addAttribute("staff", new StaffDTO());
        model.addAttribute("isNew", true);
        model.addAttribute("activeMenu", "staff");
        return "staff/form";
    }

    // Thêm nhân viên mới
    @PostMapping
    public String addStaff(@Validated @ModelAttribute("staff") StaffDTO staff, 
                          BindingResult result, 
                          RedirectAttributes redirectAttributes) {
        logger.info("Adding new staff: {}", staff);
        
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getAllErrors());
            return "staff/form";
        }
        
        try {
            staffService.save(staff);
            redirectAttributes.addFlashAttribute("message", "Thêm nhân viên thành công!");
            logger.info("Staff added successfully");
            return "redirect:/staff";
        } catch (IllegalArgumentException e) {
            logger.error("Error adding staff: {}", e.getMessage());
            result.rejectValue("code", "error.staff", e.getMessage());
            return "staff/form";
        }
    }

    // Hiển thị form chỉnh sửa thông tin nhân viên
    @GetMapping("/{id}/edit")
    public String showEditStaffForm(@PathVariable UUID id, Model model) {
        logger.info("Showing edit form for staff with id: {}", id);
        StaffDTO staff = staffService.findById(id);
        model.addAttribute("staff", staff);
        model.addAttribute("isNew", false);
        model.addAttribute("activeMenu", "staff");
        return "staff/form";
    }

    // Xử lý cập nhật thông tin nhân viên
    @PutMapping("/{id}")
    public String updateStaff(@PathVariable UUID id,
                            @Validated @ModelAttribute("staff") StaffDTO staff,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        logger.info("Updating staff with id: {}", id);
        
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getAllErrors());
            return "staff/form";
        }
        
        try {
            staffService.update(id, staff);
            redirectAttributes.addFlashAttribute("message", "Cập nhật nhân viên thành công!");
            logger.info("Staff updated successfully");
            return "redirect:/staff";
        } catch (IllegalArgumentException e) {
            logger.error("Error updating staff: {}", e.getMessage());
            result.rejectValue("code", "error.staff", e.getMessage());
            return "staff/form";
        }
    }

    // Thay đổi trạng thái nhân viên
    @PutMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        logger.info("Toggling status for staff with id: {}", id);
        try {
            staffService.toggleStatus(id);
            redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái thành công!");
            logger.info("Status toggled successfully");
        } catch (Exception e) {
            logger.error("Error toggling status: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Không thể cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/staff";
    }

    // AJAX endpoint for adding staff
    @PostMapping("/ajax")
    @ResponseBody
    public ResponseEntity<?> addStaffAjax(@Validated @RequestBody StaffDTO staff) {
        logger.info("Adding new staff via AJAX: {}", staff);
        try {
            StaffDTO savedStaff = staffService.save(staff);
            return ResponseEntity.ok(savedStaff);
        } catch (IllegalArgumentException e) {
            logger.error("Error adding staff: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // AJAX endpoint for updating staff
    @PutMapping("/{id}/ajax")
    @ResponseBody
    public ResponseEntity<?> updateStaffAjax(@PathVariable UUID id, @Validated @RequestBody StaffDTO staff) {
        logger.info("Updating staff via AJAX with id: {}", id);
        try {
            StaffDTO updatedStaff = staffService.update(id, staff);
            return ResponseEntity.ok(updatedStaff);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating staff: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            if (e.getMessage().contains("FPT email")) {
                response.put("fptEmail", e.getMessage());
            } else if (e.getMessage().contains("FE email")) {
                response.put("feEmail", e.getMessage());
            } else {
                response.put("error", e.getMessage());
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}/ajax")
    @ResponseBody
    public ResponseEntity<?> getStaffAjax(@PathVariable String id) {
        try {
            StaffDTO staff = staffService.findById(UUID.fromString(id));
            if (staff == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi xảy ra khi lấy thông tin nhân viên: " + e.getMessage()));
        }
    }
}
