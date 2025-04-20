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
    private String facilityName;
    private String departmentName;
    private String majorName;
    private Byte status;
    private List<StaffDTO> staffList;
} 