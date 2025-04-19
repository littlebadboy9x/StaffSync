package com.example.staffsync.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "import_details")
public class ImportDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "import_history_id", nullable = false)
    private ImportHistory importHistory;

    @Column(name = "row_number")
    private int rowNumber;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "status")
    private boolean successful;

    @Column(name = "error_message", length = 500)
    private String errorMessage;
}
