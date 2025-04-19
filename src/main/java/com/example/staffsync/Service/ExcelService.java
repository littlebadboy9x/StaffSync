package com.example.staffsync.Service;

import com.example.staffsync.Entity.*;
import com.example.staffsync.dto.ImportDetailDTO;
import com.example.staffsync.dto.ImportResultDTO;
import com.example.staffsync.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private EmployeeSpecializationRepository employeeSpecializationRepository;

    @Autowired
    private ImportHistoryRepository importHistoryRepository;

    public ByteArrayInputStream downloadTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Employees");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Code");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("FPT Email");
            headerRow.createCell(3).setCellValue("FE Email");
            headerRow.createCell(4).setCellValue("Campus");
            headerRow.createCell(5).setCellValue("Department");
            headerRow.createCell(6).setCellValue("Specialization");

            // Create styles for header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Apply styles to header
            for (int i = 0; i < 7; i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }

            // Auto-size columns
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Transactional
    public ImportResultDTO importEmployees(MultipartFile file) throws IOException {
        ImportResultDTO result = new ImportResultDTO();
        List<ImportDetailDTO> details = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            int totalRows = 0;
            int successCount = 0;
            int failCount = 0;

            // Create import history record
            ImportHistory importHistory = new ImportHistory();
            importHistory.setImportDate(LocalDateTime.now());
            importHistory.setFileName(file.getOriginalFilename());
            importHistory = importHistoryRepository.save(importHistory);

            // Skip header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                totalRows++;
                ImportDetailDTO detail = new ImportDetailDTO();
                detail.setRowNumber(i + 1); // +1 because Excel rows are 1-based for users

                try {
                    // Extract data from row
                    String code = getCellValueAsString(row.getCell(0));
                    String name = getCellValueAsString(row.getCell(1));
                    String fptEmail = getCellValueAsString(row.getCell(2));
                    String feEmail = getCellValueAsString(row.getCell(3));
                    String campusName = getCellValueAsString(row.getCell(4));
                    String departmentName = getCellValueAsString(row.getCell(5));
                    String specializationName = getCellValueAsString(row.getCell(6));

                    detail.setEmployeeCode(code);

                    // Validate required fields
                    if (code.isEmpty() || name.isEmpty()) {
                        throw new IllegalArgumentException("Code and Name are required");
                    }

                    // Validate length constraints
                    if (code.length() > 15) {
                        throw new IllegalArgumentException("Code must be less than 15 characters");
                    }
                    if (name.length() > 100 || fptEmail.length() > 100 || feEmail.length() > 100) {
                        throw new IllegalArgumentException("Fields must be less than 100 characters");
                    }

                    // Validate email format and content
                    if (!fptEmail.isEmpty()) {
                        if (!fptEmail.endsWith("@fpt.edu.vn")) {
                            throw new IllegalArgumentException("FPT email must end with @fpt.edu.vn");
                        }
                        if (fptEmail.contains(" ") || !fptEmail.matches("^[a-zA-Z0-9.@]*$")) {
                            throw new IllegalArgumentException("FPT email cannot contain spaces or Vietnamese characters");
                        }
                        if (!fptEmail.contains(code)) {
                            throw new IllegalArgumentException("FPT email must contain employee code");
                        }
                    }

                    if (!feEmail.isEmpty()) {
                        if (!feEmail.endsWith("@fe.edu.vn")) {
                            throw new IllegalArgumentException("FE email must end with @fe.edu.vn");
                        }
                        if (feEmail.contains(" ") || !feEmail.matches("^[a-zA-Z0-9.@]*$")) {
                            throw new IllegalArgumentException("FE email cannot contain spaces or Vietnamese characters");
                        }
                        if (!feEmail.contains(code)) {
                            throw new IllegalArgumentException("FE email must contain employee code");
                        }
                    }

                    // Validate email contains code
                    if (!fptEmail.isEmpty() && !fptEmail.contains(code)) {
                        throw new IllegalArgumentException("FPT email must contain employee code");
                    }

                    if (!feEmail.isEmpty() && !feEmail.contains(code)) {
                        throw new IllegalArgumentException("FE email must contain employee code");
                    }

                    // Check for duplicate code
                    Optional<Employee> existingEmployee = employeeRepository.findByCode(code);

                    Employee employee;
                    if (existingEmployee.isPresent()) {
                        employee = existingEmployee.get();

                        // Update existing employee
                        employee.setName(name);

                        // Only update emails if they are provided and not already used by another employee
                        if (!fptEmail.isEmpty()) {
                            Optional<Employee> employeeWithFptEmail = employeeRepository.findByFptEmail(fptEmail);
                            if (employeeWithFptEmail.isPresent() && !employeeWithFptEmail.get().getId().equals(employee.getId())) {
                                throw new IllegalArgumentException("FPT email already exists");
                            }
                            employee.setFptEmail(fptEmail);
                        }

                        if (!feEmail.isEmpty()) {
                            Optional<Employee> employeeWithFeEmail = employeeRepository.findByFeEmail(feEmail);
                            if (employeeWithFeEmail.isPresent() && !employeeWithFeEmail.get().getId().equals(employee.getId())) {
                                throw new IllegalArgumentException("FE email already exists");
                            }
                            employee.setFeEmail(feEmail);
                        }
                    } else {
                        // Create new employee
                        employee = new Employee();
                        employee.setCode(code);
                        employee.setName(name);
                        employee.setFptEmail(fptEmail);
                        employee.setFeEmail(feEmail);
                        employee.setActive(true);
                    }

                    employee = employeeRepository.save(employee);

                    // Process specialization if all fields are provided
                    if (!campusName.isEmpty() && !departmentName.isEmpty() && !specializationName.isEmpty()) {
                        // Find campus
                        List<Campus> campuses = campusRepository.findAll();
                        Campus campus = campuses.stream()
                                .filter(c -> c.getName().equalsIgnoreCase(campusName))
                                .findFirst()
                                .orElseThrow(() -> new EntityNotFoundException("Campus not found: " + campusName));

                        // Find department
                        List<Department> departments = departmentRepository.findByCampus(campus);
                        Department department = departments.stream()
                                .filter(d -> d.getName().equalsIgnoreCase(departmentName))
                                .findFirst()
                                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + departmentName));

                        // Find specialization
                        List<Specialization> specializations = specializationRepository.findByDepartment(department);
                        Specialization specialization = specializations.stream()
                                .filter(s -> s.getName().equalsIgnoreCase(specializationName))
                                .findFirst()
                                .orElseThrow(() -> new EntityNotFoundException("Specialization not found: " + specializationName));

                        // Check if employee already has a specialization in this campus
                        // boolean hasSpecializationInCampus = employeeSpecializationRepository
                        //     .existsByEmployeeAndSpecializationDepartmentCampusAndActive(employee, campus, true);
                        
                        // if (hasSpecializationInCampus) {
                        //     throw new IllegalArgumentException(
                        //         "Employee already has an active specialization in campus: " + campusName);
                        // }

                        // Create new employee specialization
                        EmployeeSpecialization employeeSpecialization = new EmployeeSpecialization();
                        employeeSpecialization.setEmployee(employee);
                        employeeSpecialization.setSpecialization(specialization);
                        // employeeSpecialization.setActive(true);
                        employeeSpecialization.setCreatedDate(System.currentTimeMillis());
                        employeeSpecialization.setLastModifiedDate(System.currentTimeMillis());
                        employeeSpecializationRepository.save(employeeSpecialization);

                    }

                    detail.setSuccessful(true);
                    successCount++;
                } catch (Exception e) {
                    detail.setSuccessful(false);
                    detail.setErrorMessage(e.getMessage());
                    failCount++;
                } finally {
                    details.add(detail);

                    // Add import detail to history
                    ImportDetail importDetail = new ImportDetail();
                    importDetail.setImportHistory(importHistory);
                    importDetail.setRowNumber(detail.getRowNumber());
                    importDetail.setEmployeeCode(detail.getEmployeeCode());
                    importDetail.setSuccessful(detail.isSuccessful());
                    importDetail.setErrorMessage(detail.getErrorMessage());
                    importHistory.getDetails().add(importDetail);
                }
            }

            // // Update import history counts
            // importHistory.setTotalRecords(totalRows);
            // importHistory.setSuccessfulRecords(successCount);
            // importHistory.setFailedRecords(failCount);
            // importHistoryRepository.save(importHistory);

            // result.setTotalRecords(totalRows);
            // result.setSuccessfulRecords(successCount);
            // result.setFailedRecords(failCount);
            // result.setDetails(details);

            return result;
        }
    }

    public List<ImportHistory> getImportHistory() {
        return importHistoryRepository.findAllByOrderByImportDateDesc();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}
