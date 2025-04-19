package com.example.staffsync.dto;

import lombok.Data;
import java.util.List;

@Data
public class ImportResultDTO {
    private int totalRecords;
    private int successCount;
    private int failureCount;
    private List<String> errorMessages;
}
