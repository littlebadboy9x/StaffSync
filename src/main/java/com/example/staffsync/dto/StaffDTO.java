package com.example.staffsync.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class StaffDTO {
    private UUID id;
    private String code;
    private String fptEmail;
    private String feEmail;

    @NotBlank(message = "Staff code is required")
    @Size(max = 15, message = "Staff code must be less than 15 characters")
    private String staffCode;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "FPT account is required")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@fpt\\.edu\\.vn$", message = "Invalid FPT email format")
    private String accountFpt;

    @NotBlank(message = "FE account is required")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@fe\\.edu\\.vn$", message = "Invalid FE email format")
    private String accountFe;

    private Byte status;
}