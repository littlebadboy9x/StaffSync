package com.example.staffsync.Service;

import com.example.staffsync.Entity.Employee;
import com.example.staffsync.dto.EmployeeDTO;
import com.example.staffsync.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public EmployeeDTO getEmployeeByCode(String code) {
        Employee employee = employeeRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with code: " + code));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        validateEmployee(employeeDTO, null);

        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee = employeeRepository.save(employee);
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        validateEmployee(employeeDTO, id);

        modelMapper.map(employeeDTO, existingEmployee);
        existingEmployee = employeeRepository.save(existingEmployee);
        return modelMapper.map(existingEmployee, EmployeeDTO.class);
    }

    @Transactional
    public EmployeeDTO toggleEmployeeStatus(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        employee.setActive(!employee.isActive());
        employee = employeeRepository.save(employee);
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    private void validateEmployee(EmployeeDTO employeeDTO, Long id) {
        // Check for duplicate code
        if (employeeRepository.existsByCode(employeeDTO.getCode()) &&
                (id == null || !employeeRepository.findByCode(employeeDTO.getCode()).get().getId().equals(id))) {
            throw new IllegalArgumentException("Employee code already exists");
        }

        // Check for duplicate FPT email
        if (employeeDTO.getFptEmail() != null && !employeeDTO.getFptEmail().isEmpty() &&
                employeeRepository.existsByFptEmail(employeeDTO.getFptEmail()) &&
                (id == null || !employeeRepository.findByFptEmail(employeeDTO.getFptEmail()).get().getId().equals(id))) {
            throw new IllegalArgumentException("FPT email already exists");
        }

        // Check for duplicate FE email
        if (employeeDTO.getFeEmail() != null && !employeeDTO.getFeEmail().isEmpty() &&
                employeeRepository.existsByFeEmail(employeeDTO.getFeEmail()) &&
                (id == null || !employeeRepository.findByFeEmail(employeeDTO.getFeEmail()).get().getId().equals(id))) {
            throw new IllegalArgumentException("FE email already exists");
        }

        // Validate FPT email format
        if (employeeDTO.getFptEmail() != null && !employeeDTO.getFptEmail().isEmpty() &&
                !employeeDTO.getFptEmail().endsWith("@fpt.edu.vn")) {
            throw new IllegalArgumentException("FPT email must end with @fpt.edu.vn");
        }

        // Validate FE email format
        if (employeeDTO.getFeEmail() != null && !employeeDTO.getFeEmail().isEmpty() &&
                !employeeDTO.getFeEmail().endsWith("@fe.edu.vn")) {
            throw new IllegalArgumentException("FE email must end with @fe.edu.vn");
        }

        // Check for spaces and Vietnamese characters in emails
        if (employeeDTO.getFptEmail() != null && !employeeDTO.getFptEmail().isEmpty() &&
                (employeeDTO.getFptEmail().contains(" ") || !employeeDTO.getFptEmail().matches("^[a-zA-Z0-9._-]+@fpt\\.edu\\.vn$"))) {
            throw new IllegalArgumentException("FPT email cannot contain spaces or Vietnamese characters");
        }

        if (employeeDTO.getFeEmail() != null && !employeeDTO.getFeEmail().isEmpty() &&
                (employeeDTO.getFeEmail().contains(" ") || !employeeDTO.getFeEmail().matches("^[a-zA-Z0-9._-]+@fe\\.edu\\.vn$"))) {
            throw new IllegalArgumentException("FE email cannot contain spaces or Vietnamese characters");
        }
    }
}