package com.example.staffsync.repository;

import com.example.staffsync.Entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {

    Optional<Staff> findByStaffCode(String staffCode);

    boolean existsByStaffCode(String staffCode);

    boolean existsByAccountFpt(String accountFpt);

    boolean existsByAccountFe(String accountFe);

    Optional<Staff> findByAccountFpt(String accountFpt);

    Optional<Staff> findByAccountFe(String accountFe);
}
