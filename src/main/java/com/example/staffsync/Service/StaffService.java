package com.example.staffsync.Service;

import com.example.staffsync.Entity.Staff;
import com.example.staffsync.dto.StaffDTO;
import com.example.staffsync.repository.StaffRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(staff -> modelMapper.map(staff, StaffDTO.class))
                .collect(Collectors.toList());
    }

    public StaffDTO getStaffById(UUID id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + id));
        return modelMapper.map(staff, StaffDTO.class);
    }

    public StaffDTO getStaffByCode(String code) {
        Staff staff = staffRepository.findByStaffCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with code: " + code));
        return modelMapper.map(staff, StaffDTO.class);
    }

    @Transactional
    public StaffDTO createStaff(StaffDTO staffDTO) {
        validateStaff(staffDTO, null);

        Staff staff = modelMapper.map(staffDTO, Staff.class);
        staff.setId(UUID.randomUUID());
        staff.setStatus((byte) 1);
        staff.setCreatedDate(System.currentTimeMillis());
        staff.setLastModifiedDate(System.currentTimeMillis());

        staff = staffRepository.save(staff);
        return modelMapper.map(staff, StaffDTO.class);
    }

    @Transactional
    public StaffDTO updateStaff(UUID id, StaffDTO staffDTO) {
        Staff existingStaff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + id));

        validateStaff(staffDTO, id);

        existingStaff.setName(staffDTO.getName());
        existingStaff.setAccountFpt(staffDTO.getAccountFpt());
        existingStaff.setAccountFe(staffDTO.getAccountFe());
        existingStaff.setLastModifiedDate(System.currentTimeMillis());

        existingStaff = staffRepository.save(existingStaff);
        return modelMapper.map(existingStaff, StaffDTO.class);
    }

    @Transactional
    public StaffDTO toggleStaffStatus(UUID id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + id));

        staff.setStatus(staff.getStatus() == 1 ? (byte) 0 : (byte) 1);
        staff.setLastModifiedDate(System.currentTimeMillis());

        staff = staffRepository.save(staff);
        return modelMapper.map(staff, StaffDTO.class);
    }

    private void validateStaff(StaffDTO staffDTO, UUID id) {
        // Check for duplicate code
        if (staffRepository.existsByStaffCode(staffDTO.getStaffCode()) &&
                (id == null || !staffRepository.findByStaffCode(staffDTO.getStaffCode()).get().getId().equals(id))) {
            throw new IllegalArgumentException("Staff code already exists");
        }

        // Check for duplicate FPT email
        if (staffDTO.getAccountFpt() != null && !staffDTO.getAccountFpt().isEmpty() &&
                staffRepository.existsByAccountFpt(staffDTO.getAccountFpt()) &&
                (id == null || !staffRepository.findByAccountFpt(staffDTO.getAccountFpt()).get().getId().equals(id))) {
            throw new IllegalArgumentException("FPT email already exists");
        }

        // Check for duplicate FE email
        if (staffDTO.getAccountFe() != null && !staffDTO.getAccountFe().isEmpty() &&
                staffRepository.existsByAccountFe(staffDTO.getAccountFe()) &&
                (id == null || !staffRepository.findByAccountFe(staffDTO.getAccountFe()).get().getId().equals(id))) {
            throw new IllegalArgumentException("FE email already exists");
        }

        // Validate FPT email format
        if (staffDTO.getAccountFpt() != null && !staffDTO.getAccountFpt().isEmpty() &&
                !staffDTO.getAccountFpt().endsWith("@fpt.edu.vn")) {
            throw new IllegalArgumentException("FPT email must end with @fpt.edu.vn");
        }

        // Validate FE email format
        if (staffDTO.getAccountFe() != null && !staffDTO.getAccountFe().isEmpty() &&
                !staffDTO.getAccountFe().endsWith("@fe.edu.vn")) {
            throw new IllegalArgumentException("FE email must end with @fe.edu.vn");
        }

        // Check for spaces and Vietnamese characters in emails
        if (staffDTO.getAccountFpt() != null && !staffDTO.getAccountFpt().isEmpty() &&
                (staffDTO.getAccountFpt().contains(" ") || !staffDTO.getAccountFpt().matches("^[a-zA-Z0-9._-]+@fpt\\.edu\\.vn$"))) {
            throw new IllegalArgumentException("FPT email cannot contain spaces or Vietnamese characters");
        }

        if (staffDTO.getAccountFe() != null && !staffDTO.getAccountFe().isEmpty() &&
                (staffDTO.getAccountFe().contains(" ") || !staffDTO.getAccountFe().matches("^[a-zA-Z0-9._-]+@fe\\.edu\\.vn$"))) {
            throw new IllegalArgumentException("FE email cannot contain spaces or Vietnamese characters");
        }
    }
}