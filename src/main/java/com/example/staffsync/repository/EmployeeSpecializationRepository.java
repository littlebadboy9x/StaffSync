package com.example.staffsync.repository;

import com.example.staffsync.Entity.Employee;
import com.example.staffsync.Entity.EmployeeSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeSpecializationRepository extends JpaRepository<EmployeeSpecialization, Long> {

    List<EmployeeSpecialization> findByEmployee(Employee employee);

    List<EmployeeSpecialization> findByEmployeeId(Long employeeId);

    // Nếu Employee có mối quan hệ với Campus
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END " +
            "FROM EmployeeSpecialization e " +
            "WHERE e.employee.id = :employeeId AND e.employee.campus.id = :campusId")
    boolean existsByEmployeeIdAndCampusId(@Param("employeeId") Long employeeId, @Param("campusId") UUID campusId);
}
