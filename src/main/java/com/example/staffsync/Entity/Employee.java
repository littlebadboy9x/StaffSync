package com.example.staffsync.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Code is required")
    @Size(max = 15, message = "Code must be less than 15 characters")
    @Column(name = "code", unique = true, nullable = false, length = 15)
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max =50 , message = "Name must be less than 50 characters")
    private String name;

    @Column(name = "fpt_email", unique = true)
    private String fptEmail;

    @Column(name ="fe_email", unique = true)
    private String feEmail;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeSpecialization> specializations = new HashSet<>();
}
