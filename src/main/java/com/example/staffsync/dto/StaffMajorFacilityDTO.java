package com.example.staffsync.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class StaffMajorFacilityDTO {
    private UUID id;
    private UUID staffId;
    private UUID majorFacilityId;
    private Byte status;
}
