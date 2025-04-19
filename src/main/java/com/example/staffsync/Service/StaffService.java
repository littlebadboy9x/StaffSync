package com.example.staffsync.Service;

import com.example.staffsync.dto.StaffDTO;
import java.util.List;
import java.util.UUID;

public interface StaffService {
    List<StaffDTO> findAll();
    StaffDTO findById(UUID id);
    StaffDTO save(StaffDTO staffDTO);
    StaffDTO update(UUID id, StaffDTO staffDTO);
    StaffDTO toggleStatus(UUID id);
    void delete(UUID id);
}