package com.example.staffsync.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee code is required")
    @Size(max = 15, message = "Employee code must be less than 15 characters")
    @Column(unique = true, nullable = false, length = 15)
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Column(name = "fpt_email", unique = true)
    private String fptEmail;

    @Column(name = "fe_email", unique = true)
    private String feEmail;

    @Column(name = "is_active")
    private boolean active = true;

    // Thêm quan hệ với Campus (Dùng UUID cho campus_id)
    @ManyToOne
    @JoinColumn(name = "campus_id")  // khóa ngoại
    private Campus campus;  // Đảm bảo class Campus đã được khai báo đúng

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeSpecialization> specializations = new HashSet<>();
}
