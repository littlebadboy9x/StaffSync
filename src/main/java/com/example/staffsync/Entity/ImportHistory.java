package com.example.staffsync.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "import_history")
public class ImportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "import_date")
    private LocalDateTime importDate;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "total_records")
    private int totalRecords;

    @Column(name = "successful_records")
    private int successfulRecords;

    @Column(name = "failed_records")
    private int failedRecords;

    @OneToMany(mappedBy = "importHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ImportDetail> details = new HashSet<>();
}
