package com.example.staffsync.Entity;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;

    import java.util.Set;
    import java.util.UUID;

    @Entity
    @Table(name = "department_facility")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class DepartmentFacility {
        @Id
        @Column(name = "id", nullable = false)
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "id_department")
        private Department department;

        @ManyToOne
        @JoinColumn(name = "id_facility")
        private Facility facility;

        @ManyToOne
        @JoinColumn(name = "id_staff")
        private Staff staff;

        @Column(name = "status", nullable = false)
        private Byte status;

        @Column(name = "created_date", nullable = false)
        private Long createdDate;

        @Column(name = "last_modified_date", nullable = false)
        private Long lastModifiedDate;

        @OneToMany(mappedBy = "departmentFacility")
        private Set<MajorFacility> majorFacilities;
    }