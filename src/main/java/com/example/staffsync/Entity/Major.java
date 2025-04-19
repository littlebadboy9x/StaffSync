package com.example.staffsync.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "major")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Major {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private Byte status;

    @Column(name = "created_date", nullable = false)
    private Long createdDate;

    @Column(name = "last_modified_date", nullable = false)
    private Long lastModifiedDate;

    @OneToMany(mappedBy = "major")
    private Set<MajorFacility> majorFacilities;
}