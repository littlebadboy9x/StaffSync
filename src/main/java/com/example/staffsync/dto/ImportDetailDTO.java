package com.example.staffsync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDetailDTO {

    private int rowNumber;
    private String employeeCode;
    private boolean successful;
    private String errorMessage;
}
