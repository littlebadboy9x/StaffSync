package com.example.staffsync.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DepartmentDTO {
    private UUID id;
    private String code;
    private String name;
    private int status;
}