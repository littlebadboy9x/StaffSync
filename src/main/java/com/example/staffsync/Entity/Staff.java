package com.example.staffsync.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Staff {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "staff_code", nullable = false, length = 15, unique = true)
    @Pattern(regexp = "^[^\\s]{1,15}$", message = "Staff code must not contain spaces and be less than 15 characters")
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "account_fpt", nullable = false, unique = true, length = 100)
    private String fptEmail;

    @Column(name = "account_fe", nullable = false, unique = true, length = 100)
    private String feEmail;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "created_date", nullable = false)
    private Long createdDate;

    @Column(name = "last_modified_date", nullable = false)
    private Long lastModifiedDate;

    @JsonIgnore
    @OneToMany(mappedBy = "staff")
    private Set<DepartmentFacility> departmentFacilities;

    @JsonIgnore
    @OneToMany(mappedBy = "staff")
    private Set<StaffMajorFacility> staffMajorFacilities;
}