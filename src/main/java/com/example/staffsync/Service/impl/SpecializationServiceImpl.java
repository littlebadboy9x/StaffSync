package com.example.staffsync.Service.impl;

import com.example.staffsync.Entity.*;
import com.example.staffsync.dto.AddSpecializationDTO;
import com.example.staffsync.dto.SpecializationDTO;
import com.example.staffsync.dto.StaffMajorFacilityDTO;
import com.example.staffsync.dto.StaffDTO;
import com.example.staffsync.repository.*;
import com.example.staffsync.Service.SpecializationService;
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
    public SpecializationDTO getSpecializationById(UUID id) {
        MajorFacility majorFacility = majorFacilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specialization not found"));

        SpecializationDTO dto = new SpecializationDTO();
        dto.setId(majorFacility.getId());
        dto.setFacilityName(majorFacility.getDepartmentFacility().getFacility().getName());
        dto.setDepartmentName(majorFacility.getDepartmentFacility().getDepartment().getName());
        dto.setMajorName(majorFacility.getMajor().getName());
        dto.setStatus(majorFacility.getStatus());

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

    @Override
    public List<SpecializationDTO> getAllSpecializations() {
        return majorFacilityRepository.findAll().stream()
                .map(mf -> {
                    SpecializationDTO dto = new SpecializationDTO();
                    dto.setId(mf.getId());
                    dto.setFacilityName(mf.getDepartmentFacility().getFacility().getName());
                    dto.setDepartmentName(mf.getDepartmentFacility().getDepartment().getName());
                    dto.setMajorName(mf.getMajor().getName());
                    dto.setStatus(mf.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addSpecialization(AddSpecializationDTO dto) {
        // Kiểm tra xem đã tồn tại chưa
        DepartmentFacility departmentFacility = departmentFacilityRepository
                .findByDepartmentIdAndFacilityId(dto.getDepartmentId(), dto.getFacilityId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bộ môn trong cơ sở này"));

        if (majorFacilityRepository.existsByDepartmentFacilityAndMajorId(departmentFacility, dto.getMajorId())) {
            throw new RuntimeException("Chuyên ngành này đã tồn tại trong bộ môn của cơ sở này");
        }

        // Tạo mới
        MajorFacility majorFacility = new MajorFacility();
        majorFacility.setDepartmentFacility(departmentFacility);
        majorFacility.setMajor(majorRepository.findById(dto.getMajorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên ngành")));
        majorFacility.setStatus((byte) 1);
        majorFacility.setCreatedDate(System.currentTimeMillis());
        majorFacility.setLastModifiedDate(System.currentTimeMillis());

        majorFacilityRepository.save(majorFacility);
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
    public List<DepartmentFacility> getDepartmentsByFacility(UUID facilityId) {
        return departmentFacilityRepository.findByFacilityId(facilityId);
    }

    @Override
    public List<MajorFacility> getMajorsByDepartment(UUID departmentId) {
        return majorFacilityRepository.findByDepartmentFacility_DepartmentId(departmentId);
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
        staffMajorFacility.setStaff(staff);
        staffMajorFacility.setMajorFacility(majorFacility);
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