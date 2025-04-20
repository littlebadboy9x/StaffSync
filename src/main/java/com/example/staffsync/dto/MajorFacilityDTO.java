package com.example.staffsync.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class MajorFacilityDTO {
    private UUID id;
    private UUID majorId;
    private String majorName;
    private String majorCode;
} 