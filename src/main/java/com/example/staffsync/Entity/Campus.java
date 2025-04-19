package com.example.staffsync.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "facility") // Tên bảng trong database là "facility"
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Campus {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Byte status;

    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "last_modified_date")
    private Long lastModifiedDate;


    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    private Set<DepartmentFacility> departmentFacilities = new HashSet<>();
}