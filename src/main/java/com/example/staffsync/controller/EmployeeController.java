package com.example.staffsync.controller;

import com.example.staffsync.Service.EmployeeService;
import com.example.staffsync.dto.EmployeeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<EmployeeDTO> getEmployeeByCode(@PathVariable String code) {
        return ResponseEntity.ok(employeeService.getEmployeeByCode(code));
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return new ResponseEntity<>(employeeService.createEmployee(employeeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<EmployeeDTO> toggleEmployeeStatus(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.toggleEmployeeStatus(id));
    }
}

