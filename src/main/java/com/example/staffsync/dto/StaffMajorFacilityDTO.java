package com.example.staffsync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffMajorFacilityDTO {

    private UUID id;
    private UUID staffId;
    private String staffCode;
    private String staffName;
    private UUID majorId;
    private String majorName;
    private UUID departmentId;
    private String departmentName;
    private UUID facilityId;
    private String facilityName;
}
