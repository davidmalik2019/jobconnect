
package com.jobconnect.jobconnect.dto;

public class AdminUserResponse {

    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String role;

    public AdminUserResponse(
            Long id,
            String fullName,
            String username,
            String email,
            String phone,
            String role) {

        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }
}
