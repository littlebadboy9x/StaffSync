package com.example.staffsync.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "department_facility")
public class DepartmentFacility {
    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToMany
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @ManyToMany
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "status")
    private Byte status;

    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "last_modified_date")
    private Long lastModifiedDate;
}
