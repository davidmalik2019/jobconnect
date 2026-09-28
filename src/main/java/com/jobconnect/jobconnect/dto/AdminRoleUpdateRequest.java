package com.jobconnect.jobconnect.dto;

public class AdminRoleUpdateRequest {

    private String role;

    public AdminRoleUpdateRequest() {
    }

    public AdminRoleUpdateRequest(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}