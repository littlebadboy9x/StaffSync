package com.example.staffsync.controller;

import com.example.staffsync.dto.StaffDTO;
import com.example.staffsync.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/staff")
@CrossOrigin
public class StaffController {

    @Autowired
    private StaffService staffService;

    // Lấy danh sách tất cả nhân viên
    @GetMapping
    public String getAllStaff(Model model) {
        List<StaffDTO> staffList = staffService.findAll();
        model.addAttribute("staff", staffList); // Thêm danh sách nhân viên vào model
        return "staff/staffList"; // Trả về view staffList.html
    }

    // Lấy thông tin chi tiết nhân viên
    @GetMapping("/{id}")
    public String getStaffById(@PathVariable UUID id, Model model) {
        StaffDTO staff = staffService.findById(id);
        model.addAttribute("staff", staff); // Thêm thông tin nhân viên vào model
        return "staff/staffDetail"; // Trả về view staffDetail.html
    }

    // Hiển thị form thêm nhân viên
    @GetMapping("/new")
    public String showAddStaffForm(Model model) {
        model.addAttribute("staffDTO", new StaffDTO());  // Tạo một DTO rỗng để truyền vào form
        return "staff/addStaff";  // Trả về view addStaff.html
    }

    // Thêm nhân viên mới
    @PostMapping
    public String addStaff(@Validated @ModelAttribute StaffDTO staffDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "staff/addStaff";  // Nếu có lỗi, quay lại form thêm nhân viên
        }
        staffService.save(staffDTO);  // Lưu nhân viên mới vào database
        return "redirect:/staff";  // Chuyển hướng về danh sách nhân viên
    }

    // Thay đổi trạng thái nhân viên
    @PutMapping("/{id}/status")
    public String toggleStaffStatus(@PathVariable UUID id) {
        staffService.toggleStatus(id);
        return "redirect:/staff"; // Sau khi thay đổi trạng thái xong, chuyển hướng về trang danh sách
    }

    // Xóa nhân viên
    @DeleteMapping("/{id}")
    public String deleteStaff(@PathVariable UUID id) {
        staffService.delete(id);
        return "redirect:/staff"; // Sau khi xóa xong, chuyển hướng về trang danh sách
    }

    // Hiển thị form chỉnh sửa thông tin nhân viên
    @GetMapping("/{id}/edit")
    public String showEditStaffForm(@PathVariable UUID id, Model model) {
        StaffDTO staff = staffService.findById(id); // Lấy thông tin nhân viên từ database
        model.addAttribute("staff", staff); // Truyền thông tin nhân viên vào model
        return "staff/editStaff"; // Trả về view form chỉnh sửa
    }

    // Xử lý cập nhật thông tin nhân viên
    @PutMapping("/{id}")
    public String updateStaff(@PathVariable UUID id, @Validated @ModelAttribute StaffDTO staffDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "staff/editStaff"; // Nếu có lỗi, trả về form chỉnh sửa
        }
        staffService.update(id, staffDTO); // Cập nhật thông tin nhân viên
        return "redirect:/staff"; // Sau khi cập nhật thành công, chuyển hướng về trang danh sách nhân viên
    }


}
