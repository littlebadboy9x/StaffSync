package com.example.staffsync.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DepartmentFacilityDTO {
    private UUID id;
    private UUID departmentId;
    private UUID facilityId;
    private UUID staffId;
    private Byte status;
    private Long createdDate;
    private Long lastModifiedDate;
    private String departmentCode;
    private String departmentName;
    private String facilityCode;
    private String facilityName;

    public DepartmentFacilityDTO(UUID id, UUID departmentId, UUID facilityId, UUID staffId,
                               Byte status, Long createdDate, Long lastModifiedDate,
                               String departmentCode, String departmentName,
                               String facilityCode, String facilityName) {
        this.id = id;
        this.departmentId = departmentId;
        this.facilityId = facilityId;
        this.staffId = staffId;
        this.status = status;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.facilityCode = facilityCode;
        this.facilityName = facilityName;
    }
} 