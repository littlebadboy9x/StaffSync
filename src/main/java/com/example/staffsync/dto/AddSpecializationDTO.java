package com.example.staffsync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSpecializationDTO {
    private String facilityName;
    private String departmentName;
    private String majorName;
    private Byte status;
} 