package com.example.staffsync.repository;

import com.example.staffsync.Entity.Department;
import com.example.staffsync.Entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    List<Specialization> findByDepartment(Department department);

    List<Specialization> findByDepartmentId(Long departmentId);

    @Query("SELECT s FROM Specialization s WHERE s.department.campus.id = :campusId")
    List<Specialization> findByCampusId(Long campusId);
}
