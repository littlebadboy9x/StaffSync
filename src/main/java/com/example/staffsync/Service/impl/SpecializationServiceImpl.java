package com.example.staffsync.Service.impl;

import com.example.staffsync.Entity.*;
import com.example.staffsync.dto.*;
import com.example.staffsync.repository.*;
import com.example.staffsync.Service.SpecializationService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SpecializationServiceImpl implements SpecializationService {

    @Override
    @Transactional
    public void updateSpecialization(UUID id, AddSpecializationDTO dto) {
        MajorFacility majorFacility = majorFacilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specialization not found"));

        // Find the facility
        Facility facility = facilityRepository.findAll().stream()
                .filter(f -> f.getName().equalsIgnoreCase(dto.getFacilityName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Facility not found: " + dto.getFacilityName()));

        // Find the department
        Department department = departmentRepository.findAll().stream()
                .filter(d -> d.getName().equalsIgnoreCase(dto.getDepartmentName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Department not found: " + dto.getDepartmentName()));

        // Find the major
        Major major = majorRepository.findAll().stream()
                .filter(m -> m.getName().equalsIgnoreCase(dto.getMajorName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Major not found: " + dto.getMajorName()));

        // Get or create the DepartmentFacility
        DepartmentFacility departmentFacility = departmentFacilityRepository
                .findByDepartmentIdAndFacilityId(department.getId(), facility.getId())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    DepartmentFacility newDepartmentFacility = new DepartmentFacility();
                    newDepartmentFacility.setDepartment(department);
                    newDepartmentFacility.setFacility(facility);
                    newDepartmentFacility.setStatus((byte) 1);
                    newDepartmentFacility.setCreatedDate(System.currentTimeMillis());
                    newDepartmentFacility.setLastModifiedDate(System.currentTimeMillis());
                    return departmentFacilityRepository.save(newDepartmentFacility);
                });

        // Update the MajorFacility
        majorFacility.setDepartmentFacility(departmentFacility);
        majorFacility.setMajor(major);
        majorFacility.setStatus(dto.getStatus() != null ? dto.getStatus() : majorFacility.getStatus());
        majorFacility.setLastModifiedDate(System.currentTimeMillis());

        majorFacilityRepository.save(majorFacility);
    }

    @Override
    public SpecializationDTO getSpecializationById(UUID id) {
        MajorFacility majorFacility = majorFacilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specialization not found"));

        SpecializationDTO dto = new SpecializationDTO();
        dto.setId(majorFacility.getId());
        dto.setFacilityId(majorFacility.getDepartmentFacility().getFacility().getId());
        dto.setDepartmentId(majorFacility.getDepartmentFacility().getDepartment().getId());
        dto.setMajorId(majorFacility.getMajor().getId());
        dto.setDepartmentFacilityId(majorFacility.getDepartmentFacility().getId());
        dto.setFacilityName(majorFacility.getDepartmentFacility().getFacility().getName());
        dto.setDepartmentName(majorFacility.getDepartmentFacility().getDepartment().getName());
        dto.setMajorName(majorFacility.getMajor().getName());
        dto.setStatus(majorFacility.getStatus());
        dto.setCreatedDate(majorFacility.getCreatedDate());
        dto.setLastModifiedDate(majorFacility.getLastModifiedDate());

        return dto;
    }

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private DepartmentFacilityRepository departmentFacilityRepository;

    @Autowired
    private MajorFacilityRepository majorFacilityRepository;

    @Autowired
    private StaffMajorFacilityRepository staffMajorFacilityRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<SpecializationDTO> getAllSpecializations() {
        return majorFacilityRepository.findAll().stream()
                .map(mf -> {
                    SpecializationDTO dto = new SpecializationDTO();
                    dto.setId(mf.getId());
                    dto.setFacilityId(mf.getDepartmentFacility().getFacility().getId());
                    dto.setDepartmentId(mf.getDepartmentFacility().getDepartment().getId());
                    dto.setMajorId(mf.getMajor().getId());
                    dto.setDepartmentFacilityId(mf.getDepartmentFacility().getId());
                    dto.setFacilityName(mf.getDepartmentFacility().getFacility().getName());
                    dto.setDepartmentName(mf.getDepartmentFacility().getDepartment().getName());
                    dto.setMajorName(mf.getMajor().getName());
                    dto.setStatus(mf.getStatus());
                    dto.setCreatedDate(mf.getCreatedDate());
                    dto.setLastModifiedDate(mf.getLastModifiedDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addSpecialization(AddSpecializationDTO dto) {
        // Tìm Facility theo tên
        List<Facility> facilities = facilityRepository.findAll();
        Facility facility = facilities.stream()
                .filter(f -> f.getName().equalsIgnoreCase(dto.getFacilityName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cơ sở với tên: " + dto.getFacilityName()));

        // Tìm Department theo tên
        List<Department> departments = departmentRepository.findAll();
        Department department = departments.stream()
                .filter(d -> d.getName().equalsIgnoreCase(dto.getDepartmentName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bộ môn với tên: " + dto.getDepartmentName()));

        // Tìm Major theo tên
        List<Major> majors = majorRepository.findAll();
        Major major = majors.stream()
                .filter(m -> m.getName().equalsIgnoreCase(dto.getMajorName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên ngành với tên: " + dto.getMajorName()));

        // Tìm hoặc tạo mới DepartmentFacility
        DepartmentFacility departmentFacility = departmentFacilityRepository
                .findByDepartmentIdAndFacilityId(department.getId(), facility.getId())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    DepartmentFacility newDepartmentFacility = new DepartmentFacility();
                    newDepartmentFacility.setDepartment(department);
                    newDepartmentFacility.setFacility(facility);
                    newDepartmentFacility.setStatus((byte) 1);
                    newDepartmentFacility.setCreatedDate(System.currentTimeMillis());
                    newDepartmentFacility.setLastModifiedDate(System.currentTimeMillis());
                    return departmentFacilityRepository.save(newDepartmentFacility);
                });

        // Kiểm tra xem đã tồn tại chưa
        if (majorFacilityRepository.existsByDepartmentFacilityAndMajorId(departmentFacility, major.getId())) {
            throw new RuntimeException("Chuyên ngành này đã tồn tại trong bộ môn của cơ sở này");
        }

        // Tạo mới MajorFacility
        MajorFacility majorFacility = new MajorFacility();
        majorFacility.setDepartmentFacility(departmentFacility);
        majorFacility.setMajor(major);
        majorFacility.setStatus(dto.getStatus() != null ? dto.getStatus() : (byte) 1);
        majorFacility.setCreatedDate(System.currentTimeMillis());
        majorFacility.setLastModifiedDate(System.currentTimeMillis());

        majorFacilityRepository.save(majorFacility);
    }

    private String generateCode(String name) {
        // Loại bỏ dấu và chuyển thành chữ hoa
        String normalized = name.toLowerCase()
                .replaceAll("[đĐ]", "d")
                .replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .replaceAll("\\s+", "_")
                .toUpperCase();

        // Thêm timestamp để đảm bảo unique
        return normalized + "_" + System.currentTimeMillis();
    }

    @Override
    @Transactional
    public void deleteSpecialization(UUID id) {
        MajorFacility majorFacility = majorFacilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bộ môn chuyên ngành"));

        // Kiểm tra xem có nhân viên nào đang sử dụng không
        if (!majorFacility.getStaffMajorFacilities().isEmpty()) {
            throw new RuntimeException("Không thể xóa vì có nhân viên đang sử dụng");
        }

        majorFacilityRepository.delete(majorFacility);
    }

    @Override
    public List<DepartmentFacilityDTO> getDepartmentsByFacility(UUID facilityId) {
        String jpql = "SELECT df FROM DepartmentFacility df JOIN FETCH df.department d JOIN FETCH df.facility f WHERE f.id = :facilityId";
        List<DepartmentFacility> departments = entityManager.createQuery(jpql, DepartmentFacility.class)
                .setParameter("facilityId", facilityId)
                .getResultList();

        return departments.stream()
                .map(df -> new DepartmentFacilityDTO(
                        df.getId(),
                        df.getDepartment().getId(),
                        df.getFacility().getId(),
                        null, // staffId is not needed here
                        df.getStatus(),
                        df.getCreatedDate(),
                        df.getLastModifiedDate(),
                        df.getDepartment().getCode(),
                        df.getDepartment().getName(),
                        df.getFacility().getCode(),
                        df.getFacility().getName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<MajorFacilityDTO> getMajorsByDepartment(UUID departmentFacilityId) {
        String jpql = "SELECT mf FROM MajorFacility mf " +
                "JOIN FETCH mf.departmentFacility df " +
                "JOIN FETCH mf.major m " +
                "WHERE df.id = :departmentFacilityId";
        
        return entityManager.createQuery(jpql, MajorFacility.class)
                .setParameter("departmentFacilityId", departmentFacilityId)
                .getResultList()
                .stream()
                .map(mf -> {
                    MajorFacilityDTO dto = new MajorFacilityDTO();
                    dto.setId(mf.getId());
                    dto.setMajorId(mf.getMajor().getId());
                    dto.setMajorName(mf.getMajor().getName());
                    dto.setMajorCode(mf.getMajor().getCode());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    @Override
    public List<MajorFacility> getMajorsByDepartmentFacility(UUID departmentFacilityId) {
        return majorFacilityRepository.findByDepartmentFacilityId(departmentFacilityId);
    }

    @Override
    public List<StaffDTO> getStaffInSpecialization(UUID specializationId) {
        // Kiểm tra major facility có tồn tại không
        MajorFacility majorFacility = majorFacilityRepository.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên ngành"));

        // Lấy danh sách staff major facility
        List<StaffMajorFacility> staffMajorFacilities = staffMajorFacilityRepository
                .findByMajorFacilityId(specializationId);

        // Convert sang DTO
        return staffMajorFacilities.stream()
                .map(smf -> {
                    Staff staff = smf.getStaff();
                    StaffDTO dto = new StaffDTO();
                    dto.setId(staff.getId());
                    dto.setCode(staff.getCode());
                    dto.setName(staff.getName());
                    dto.setFptEmail(staff.getFptEmail());
                    dto.setFeEmail(staff.getFeEmail());
                    dto.setStatus(staff.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StaffDTO> getAvailableStaff(UUID specializationId) {
        // Kiểm tra major facility có tồn tại không
        MajorFacility majorFacility = majorFacilityRepository.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên ngành"));

        // Lấy facility ID
        UUID facilityId = majorFacility.getDepartmentFacility().getFacility().getId();

        // Lấy danh sách ID của nhân viên đã có trong facility này
        List<UUID> existingStaffIds = staffMajorFacilityRepository
                .findByFacilityId(facilityId)
                .stream()
                .map(smf -> smf.getStaff().getId())
                .collect(Collectors.toList());

        // Lấy danh sách nhân viên:
        // 1. Chưa có trong facility này
        // 2. Đang hoạt động (status = true)
        return staffRepository.findByStatus(true).stream()
                .filter(staff -> !existingStaffIds.contains(staff.getId()))
                .map(staff -> {
                    StaffDTO dto = new StaffDTO();
                    dto.setId(staff.getId());
                    dto.setCode(staff.getCode());
                    dto.setName(staff.getName());
                    dto.setFptEmail(staff.getFptEmail());
                    dto.setFeEmail(staff.getFeEmail());
                    dto.setStatus(staff.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addStaffToSpecialization(UUID specializationId, UUID staffId) {
        MajorFacility majorFacility = majorFacilityRepository.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bộ môn chuyên ngành"));

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        // Kiểm tra xem nhân viên đã có trong chuyên ngành chưa
        if (staffMajorFacilityRepository.existsByStaffIdAndMajorFacilityId(staffId, specializationId)) {
            throw new RuntimeException("Nhân viên đã có trong chuyên ngành này");
        }

        // Tạo mới
        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
        staffMajorFacility.setStaff(staff);
        staffMajorFacility.setMajorFacility(majorFacility);
        staffMajorFacility.setCreatedDate(System.currentTimeMillis());
        staffMajorFacility.setLastModifiedDate(System.currentTimeMillis());

        staffMajorFacilityRepository.save(staffMajorFacility);
    }

    @Override
    @Transactional
    public void removeStaffFromSpecialization(UUID specializationId, UUID staffId) {
        StaffMajorFacility staffMajorFacility = staffMajorFacilityRepository
                .findByStaffIdAndMajorFacilityId(staffId, specializationId);


        staffMajorFacilityRepository.delete(staffMajorFacility);
    }

    @Override
    public List<StaffMajorFacilityDTO> getStaffSpecializations(UUID staffId) {
        return staffMajorFacilityRepository.findByStaffId(staffId).stream()
                .map(smf -> {
                    StaffMajorFacilityDTO dto = new StaffMajorFacilityDTO();
                    dto.setId(smf.getId());
                    dto.setStaffId(smf.getStaff().getId());
                    dto.setStaffName(smf.getStaff().getName());
                    dto.setStaffFptEmail(smf.getStaff().getFptEmail());
                    dto.setStaffFeEmail(smf.getStaff().getFeEmail());
                    dto.setMajorFacilityId(smf.getMajorFacility().getId());
                    dto.setMajorCode(smf.getMajorFacility().getMajor().getCode());
                    dto.setDepartmentCode(smf.getMajorFacility().getDepartmentFacility().getDepartment().getCode());
                    dto.setFacilityCode(smf.getMajorFacility().getDepartmentFacility().getFacility().getCode());
                    dto.setMajorName(smf.getMajorFacility().getMajor().getName());
                    dto.setDepartmentName(smf.getMajorFacility().getDepartmentFacility().getDepartment().getName());
                    dto.setFacilityName(smf.getMajorFacility().getDepartmentFacility().getFacility().getName());
                    dto.setCreatedDate(smf.getCreatedDate());
                    dto.setLastModifiedDate(smf.getLastModifiedDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addStaffSpecialization(UUID staffId, UUID majorFacilityId) {
        // Kiểm tra nhân viên tồn tại
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        // Kiểm tra chuyên ngành tồn tại
        MajorFacility majorFacility = majorFacilityRepository.findById(majorFacilityId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên ngành"));

        // Kiểm tra xem nhân viên đã có chuyên ngành trong cơ sở này chưa
        DepartmentFacility departmentFacility = majorFacility.getDepartmentFacility();
        if (staffMajorFacilityRepository.existsByStaffIdAndMajorFacility_DepartmentFacility_FacilityId(
                staffId, departmentFacility.getFacility().getId())) {
            throw new RuntimeException("Nhân viên đã có chuyên ngành trong cơ sở này");
        }

        // Tạo mới StaffMajorFacility
        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
        staffMajorFacility.setId(UUID.randomUUID()); // Gán ID mới
        staffMajorFacility.setStaff(staff);
        staffMajorFacility.setMajorFacility(majorFacility);
        staffMajorFacility.setStatus((byte) 1);
        staffMajorFacility.setCreatedDate(System.currentTimeMillis());
        staffMajorFacility.setLastModifiedDate(System.currentTimeMillis());

        staffMajorFacilityRepository.save(staffMajorFacility);
    }

    @Override
    @Transactional
    public void removeStaffSpecialization(UUID id) {
        // Kiểm tra xem bản ghi StaffMajorFacility có tồn tại không
        StaffMajorFacility staffMajorFacility = staffMajorFacilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên ngành của nhân viên"));

        // Xóa bản ghi
        staffMajorFacilityRepository.delete(staffMajorFacility);
    }
} 