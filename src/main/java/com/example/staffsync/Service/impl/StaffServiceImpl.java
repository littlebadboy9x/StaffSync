package com.example.staffsync.Service.impl;

import com.example.staffsync.Service.StaffService;
import com.example.staffsync.dto.StaffDTO;
import com.example.staffsync.Entity.Staff;
import com.example.staffsync.repository.StaffRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public List<StaffDTO> findAll() {
        return staffRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StaffDTO findById(UUID id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + id));
        return convertToDTO(staff);
    }

    @Override
    public StaffDTO save(StaffDTO staffDTO) {
        validateStaffDTO(staffDTO, null);
        Staff staff = new Staff();
        updateStaffFromDTO(staff, staffDTO);
        staff = staffRepository.save(staff);
        return convertToDTO(staff);
    }

    @Override
    public StaffDTO update(UUID id, StaffDTO staffDTO) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + id));
        validateStaffDTO(staffDTO, id);
        updateStaffFromDTO(staff, staffDTO);
        staff = staffRepository.save(staff);
        return convertToDTO(staff);
    }

    @Override
    public StaffDTO toggleStatus(UUID id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + id));
        staff.setStatus(!Boolean.TRUE.equals(staff.getStatus()));
        staff = staffRepository.save(staff);
        return convertToDTO(staff);
    }

    @Override
    public void delete(UUID id) {
        staffRepository.deleteById(id);
    }

    @Override
    public Optional<StaffDTO> findByCode(String code) {
        return staffRepository.findByCode(code)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<StaffDTO> findByFptEmail(String fptEmail) {
        return staffRepository.findByFptEmail(fptEmail)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<StaffDTO> findByFeEmail(String feEmail) {
        return staffRepository.findByFeEmail(feEmail)
                .map(this::convertToDTO);
    }

    private StaffDTO convertToDTO(Staff staff) {
        StaffDTO dto = new StaffDTO();
        dto.setId(staff.getId());
        dto.setCode(staff.getCode());
        dto.setName(staff.getName());
        dto.setFptEmail(staff.getFptEmail());
        dto.setFeEmail(staff.getFeEmail() != null ? staff.getFeEmail().trim() : null);
        dto.setStatus(Boolean.TRUE.equals(staff.getStatus()));
        return dto;
    }

    private void updateStaffFromDTO(Staff staff, StaffDTO dto) {
        if (staff.getId() == null) {
            staff.setId(UUID.randomUUID());
        }
        staff.setCode(dto.getCode());
        staff.setName(dto.getName());
        staff.setFptEmail(dto.getFptEmail());
        staff.setFeEmail(dto.getFeEmail());
        staff.setStatus(dto.isStatus());
        
        if (staff.getCreatedDate() == null) {
            staff.setCreatedDate(System.currentTimeMillis());
        }
        staff.setLastModifiedDate(System.currentTimeMillis());
    }

    // Validate logic for add/update
    private void validateStaffDTO(StaffDTO dto, UUID currentId) {
        // Required fields
        if (dto.getCode() == null || dto.getCode().trim().isEmpty())
            throw new IllegalArgumentException("Code is required");
        if (dto.getName() == null || dto.getName().trim().isEmpty())
            throw new IllegalArgumentException("Name is required");
        if (dto.getFptEmail() == null || dto.getFptEmail().trim().isEmpty())
            throw new IllegalArgumentException("FPT Email is required");
        if (dto.getFeEmail() == null || dto.getFeEmail().trim().isEmpty())
            throw new IllegalArgumentException("FE Email is required");

        // Length constraints
        if (dto.getCode().length() > 15)
            throw new IllegalArgumentException("Code must be less than 15 characters");
        if (dto.getName().length() > 100 || dto.getFptEmail().length() > 100 || dto.getFeEmail().length() > 100)
            throw new IllegalArgumentException("Fields must be less than 100 characters");

        // Duplicate check
        Optional<Staff> staffWithCode = staffRepository.findByCode(dto.getCode());
        staffWithCode.ifPresent(staff -> {
            if (currentId == null || !staff.getId().equals(currentId))
                throw new IllegalArgumentException("Code already exists");
        });

        Optional<Staff> staffWithFptEmail = staffRepository.findByFptEmail(dto.getFptEmail());
        staffWithFptEmail.ifPresent(staff -> {
            if (currentId == null || !staff.getId().equals(currentId))
                throw new IllegalArgumentException("FPT Email already exists");
        });

        Optional<Staff> staffWithFeEmail = staffRepository.findByFeEmail(dto.getFeEmail());
        staffWithFeEmail.ifPresent(staff -> {
            if (currentId == null || !staff.getId().equals(currentId))
                throw new IllegalArgumentException("FE Email already exists");
        });
    }
}
