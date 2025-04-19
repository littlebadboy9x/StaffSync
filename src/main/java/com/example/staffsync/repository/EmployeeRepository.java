package com.example.staffsync.repository;

import com.example.staffsync.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByFptEmail(String fptEmail);

    boolean existsByFeEmail(String feEmail);

    Optional<Employee> findByFptEmail(String fptEmail);

    Optional<Employee> findByFeEmail(String feEmail);
}
