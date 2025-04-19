package com.example.staffsync.repository;

import com.example.staffsync.Entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    boolean existsByCode(String code);  // Sửa từ existsByStaffCode thành existsByCode
    boolean existsByFptEmail(String fptEmail);
    boolean existsByFeEmail(String feEmail);

    Optional<Staff> findByCode(String code);
    Optional<Staff> findByFptEmail(String fptEmail);
    Optional<Staff> findByFeEmail(String feEmail);
}
