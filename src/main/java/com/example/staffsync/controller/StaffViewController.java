package com.example.staffsync.controller;

import com.example.staffsync.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaffViewController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/staff/view")
    public String viewStaffList(Model model) {
        model.addAttribute("staffList", staffService.findAll());
        return "staff-list";
    }
}