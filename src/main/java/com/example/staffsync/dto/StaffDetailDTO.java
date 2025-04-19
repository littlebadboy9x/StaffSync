package com.example.staffsync.dto;

import lombok.Data;
import java.util.List;

@Data
public class StaffDetailDTO {
    private StaffDTO staff;
    private List<DepartmentFacilityInfoDTO> departmentFacilities;
    @Data
public class DepartmentFacilityInfoDTO {
    private String facilityName;
    private String departmentName;
    private List<String> majors;
}
}
