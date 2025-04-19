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
@Table(name = "campuses")
public class Campus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campus name is required")
    @Size(max = 100, message = "Campus name must be less than 100 characters")
    private String name;

    @OneToOne(mappedBy = "campus", cascade = CascadeType.ALL)
    private Set<Department> departmentSet = new HashSet<>();
}
