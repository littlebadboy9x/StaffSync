package com.example.staffsync.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;

@Data
public class DepartmentDTO {
    private UUID id;
    
    @NotBlank(message = "Code is required")
    private String code;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private Byte status;
}