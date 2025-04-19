package com.example.staffsync.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "major_facility")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MajorFacility {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_department_facility")
    private DepartmentFacility departmentFacility;

    @ManyToOne
    @JoinColumn(name = "id_major")
    private Major major;

    @Column(name = "status", nullable = false)
    private Byte status;

    @Column(name = "created_date", nullable = false)
    private Long createdDate;

    @Column(name = "last_modified_date", nullable = false)
    private Long lastModifiedDate;

    @OneToMany(mappedBy = "majorFacility")
    private Set<StaffMajorFacility> staffMajorFacilities;
}