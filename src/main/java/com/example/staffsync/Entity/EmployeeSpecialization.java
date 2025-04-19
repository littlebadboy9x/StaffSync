package com.example.staffsync.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_specializations")
public class EmployeeSpecialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToMany
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization;

    @ManyToMany
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

}
