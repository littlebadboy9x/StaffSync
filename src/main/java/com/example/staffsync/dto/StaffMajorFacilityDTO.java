package com.example.staffsync.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class StaffMajorFacilityDTO {
    private UUID id;
    private UUID staffId;
    private String staffName;
    private String staffFptEmail;
    private String staffFeEmail;
    private UUID majorFacilityId;
    private String majorName;
    private String departmentName;
    private String facilityName;
    private Long createdDate;
    private Long lastModifiedDate;
}
