package com.example.staffsync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSpecializationDTO {

    private Long id;
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private Long specializationId;
    private String specializationName;
    private Long departmentId;
    private String departmentName;
    private Long campusId;
    private String campusName;
}
