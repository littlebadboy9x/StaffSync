package com.example.staffsync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSpecializationDTO {
    private UUID facilityId;
    private UUID departmentId;
    private UUID majorId;
} 