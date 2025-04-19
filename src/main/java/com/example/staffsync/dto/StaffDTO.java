package com.example.staffsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {

    private UUID id;

    @NotBlank(message = "Staff code is required")
    @Size(max = 15, message = "Staff code must be less than 15 characters")
    private String staffCode;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9._-]+@fpt\\.edu\\.vn$", message = "FPT email must end with @fpt.edu.vn and cannot contain spaces or Vietnamese characters")
    private String accountFpt;

    @Pattern(regexp = "^[a-zA-Z0-9._-]+@fe\\.edu\\.vn$", message = "FE email must end with @fe.edu.vn and cannot contain spaces or Vietnamese characters")
    private String accountFe;

    private Byte status = 1;
}
