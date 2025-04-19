package com.example.staffsync.repository;

import com.example.staffsync.Entity.Employee;
import com.example.staffsync.Entity.EmployeeSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeSpecializationRepository extends JpaRepository<EmployeeSpecialization, Long> {

    List<EmployeeSpecialization> findByEmployee(Employee employee);

    List<EmployeeSpecialization> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeIdAndCampusId(Long employeeId, Long campusId);
}
