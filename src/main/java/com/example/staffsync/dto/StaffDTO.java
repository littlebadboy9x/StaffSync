package com.example.staffsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class StaffDTO {
    private UUID id;

    @NotBlank(message = "Mã nhân viên không được để trống")
    @Size(max = 15, message = "Mã nhân viên không được vượt quá 15 ký tự")
    private String code;

    @NotBlank(message = "Tên nhân viên không được để trống")
    @Size(max = 100, message = "Tên nhân viên không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Email FPT không được để trống")
    private String fptEmail;

    @NotBlank(message = "Email FE không được để trống")
    private String feEmail;

    private boolean status;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    // Thêm các trường cho quản lý bộ môn chuyên ngành
    private String facilityName;
    private String departmentName;
    private String majorName;
    private UUID specializationId;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFptEmail() {
        return fptEmail;
    }

    public void setFptEmail(String fptEmail) {
        this.fptEmail = fptEmail;
    }

    public String getFeEmail() {
        return feEmail;
    }

    public void setFeEmail(String feEmail) {
        this.feEmail = feEmail;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}