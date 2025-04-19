package com.example.staffsync.repository;

import com.example.staffsync.Entity.Department;
import com.example.staffsync.Entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    List<Specialization> findByDepartment(Department department);


    List<Specialization> findByDepartmentId(UUID departmentId); // Thay đổi kiểu dữ liệu thành UUID


    @Query("SELECT s FROM Specialization s WHERE s.department.campus.id = :campusId")
    List<Specialization> findByCampusId(Long campusId);
}
