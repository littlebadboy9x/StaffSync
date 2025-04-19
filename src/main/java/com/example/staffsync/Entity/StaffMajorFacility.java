package com.example.staffsync.Entity;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;

    import java.util.UUID;

    @Entity
    @Table(name = "staff_major_facility")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class StaffMajorFacility {

        @Id
        @Column(name = "id", nullable = false)
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "id_major_facility")
        private MajorFacility majorFacility;

        @ManyToOne
        @JoinColumn(name = "id_staff")
        private Staff staff;

        @Column(name = "status", nullable = false)
        private Byte status;

        @Column(name = "created_date", nullable = false)
        private Long createdDate;

        @Column(name = "last_modified_date", nullable = false)
        private Long lastModifiedDate;
    }