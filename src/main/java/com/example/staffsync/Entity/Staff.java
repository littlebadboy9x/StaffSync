package com.example.staffsync.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staff")
public class Staff {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "staff_code")
    private String staffCode;

    @Column(name = "name")
    private String name;

    @Column(name = "account_fpt")
    private String accountFpt;

    @Column(name = "account_fe")
    private String accountFe;

    @Column(name = "status")
    private Byte status;

    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "last_modified_date")
    private Long lastModifiedDate;
}
