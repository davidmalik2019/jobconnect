
package com.jobconnect.jobconnect.dto;

public class LoginResponse {

    private String message;

    private String username;

    private String token;

    private String role;

    public LoginResponse(
            String message,
            String username,
            String token,
            String role) {

        this.message = message;

        this.username = username;

        this.token = token;

        this.role = role;
    }

    public String getMessage() {

        return message;
    }

    public String getUsername() {

        return username;
    }

    public String getToken() {

        return token;
    }

    public String getRole() {

        return role;
    }
}

