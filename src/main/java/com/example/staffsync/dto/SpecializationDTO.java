package com.example.staffsync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationDTO {
    private UUID id;
    private UUID facilityId;
    private UUID departmentId;
    private UUID majorId;
    private UUID departmentFacilityId;
    private String facilityName;
    private String departmentName;
    private String majorName;
    private Byte status;
    private Long createdDate;
    private Long lastModifiedDate;
    private List<StaffDTO> staffList;
} 