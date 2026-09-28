
package com.jobconnect.jobconnect.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jobconnect.jobconnect.dto.ChangePasswordRequest;


import com.jobconnect.jobconnect.dto.UserResponse;
import com.jobconnect.jobconnect.entity.User;
import com.jobconnect.jobconnect.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.getCurrentPassword() == null ||
                request.getCurrentPassword().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("Current password is required.");
        }

        if (request.getNewPassword() == null ||
                request.getNewPassword().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("New password is required.");
        }

        if (request.getConfirmPassword() == null ||
                request.getConfirmPassword().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("Please confirm your new password.");
        }

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("New passwords do not match.");
        }

        if (request.getNewPassword().length() < 6) {

            return ResponseEntity
                    .badRequest()
                    .body("New password must be at least 6 characters.");
        }

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("Current password is incorrect.");
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("New password must be different from current password.");
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        return ResponseEntity.ok(
                "Password changed successfully."
        );
    }

    // =========================================
    // GET ALL USERS
    // ADMIN ONLY
    // =========================================

    @GetMapping
    public ResponseEntity<?> getAllUsers(Authentication authentication) {

        if (!authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            return ResponseEntity.status(403)
                    .body("Access denied. Admin only.");
        }

        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole()
                ))
                .toList();

        return ResponseEntity.ok(users);
    }

    // =========================================
    // GET USER BY ID
    // ADMIN ONLY
    // =========================================

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable Long id,
            Authentication authentication) {

        if (!authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            return ResponseEntity.status(403)
                    .body("Access denied. Admin only.");
        }

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {

            User u = user.get();

            UserResponse response = new UserResponse(
                    u.getId(),
                    u.getFullName(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getPhone(),
                    u.getRole()
            );

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    // =========================================
    // JOB SEEKER REGISTRATION
    // PUBLIC
    // =========================================

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody User user) {

        // Check username
        User existingUsername =
                userRepository.findByUsername(user.getUsername());

        if (existingUsername != null) {

            return ResponseEntity
                    .badRequest()
                    .body("Username already exists.");
        }

        // Check email
        User existingEmail =
                userRepository.findByEmail(user.getEmail());

        if (existingEmail != null) {

            return ResponseEntity
                    .badRequest()
                    .body("Email already exists.");
        }

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Every public registration is JOB_SEEKER
        user.setRole("JOB_SEEKER");

        // Save user
        User savedUser =
                userRepository.save(user);

        // Safe response
        UserResponse response =
                new UserResponse(
                        savedUser.getId(),
                        savedUser.getFullName(),
                        savedUser.getUsername(),
                        savedUser.getEmail(),
                        savedUser.getPhone(),
                        savedUser.getRole()
                );

        return ResponseEntity.ok(response);
    }
 // =========================================
 // EMPLOYER REGISTRATION
 // PUBLIC
 // =========================================

 @PostMapping("/register-employer")
 public ResponseEntity<?> registerEmployer(
         @RequestBody User user) {

     // Check username
     User existingUsername =
             userRepository.findByUsername(user.getUsername());

     if (existingUsername != null) {
         return ResponseEntity
                 .badRequest()
                 .body("Username already exists.");
     }

     // Check email
     User existingEmail =
             userRepository.findByEmail(user.getEmail());

     if (existingEmail != null) {
         return ResponseEntity
                 .badRequest()
                 .body("Email already exists.");
     }

     // Encrypt password
     user.setPassword(
             passwordEncoder.encode(user.getPassword())
     );

     // Every employer registration is EMPLOYER
     user.setRole("EMPLOYER");

     // Save employer
     User savedUser =
             userRepository.save(user);

     // Safe response
     UserResponse response =
             new UserResponse(
                     savedUser.getId(),
                     savedUser.getFullName(),
                     savedUser.getUsername(),
                     savedUser.getEmail(),
                     savedUser.getPhone(),
                     savedUser.getRole()
             );

     return ResponseEntity.ok(response);
 }
    // =========================================
    // CREATE USER
    // ADMIN ONLY
    // =========================================

    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestBody User user,
            Authentication authentication) {

        if (!authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            return ResponseEntity.status(403)
                    .body("Access denied. Admin only.");
        }

        // Check username
        if (userRepository.findByUsername(user.getUsername()) != null) {

            return ResponseEntity
                    .badRequest()
                    .body("Username already exists.");
        }

        // Check email
        if (userRepository.findByEmail(user.getEmail()) != null) {

            return ResponseEntity
                    .badRequest()
                    .body("Email already exists.");
        }

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Allow ADMIN to create the requested role
        if (user.getRole() == null ||
                user.getRole().isBlank()) {

            user.setRole("JOB_SEEKER");
        }

        User savedUser =
                userRepository.save(user);

        return ResponseEntity.ok(
                new UserResponse(
                        savedUser.getId(),
                        savedUser.getFullName(),
                        savedUser.getUsername(),
                        savedUser.getEmail(),
                        savedUser.getPhone(),
                        savedUser.getRole()
                )
        );
    }

    // =========================================
    // CURRENTLY LOGGED-IN USER
    // ALL AUTHENTICATED USERS
    // =========================================

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication) {

        String username = authentication.getName();

        User user =
                userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse response =
                new UserResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole()
                );

        return ResponseEntity.ok(response);
    }

    // =========================================
    // UPDATE USER
    // ADMIN ONLY
    // =========================================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User userDetails,
            Authentication authentication) {

        if (!authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            return ResponseEntity.status(403)
                    .body("Access denied. Admin only.");
        }

        Optional<User> optionalUser =
                userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        // Check if another user already has username
        User existingUsername =
                userRepository.findByUsername(
                        userDetails.getUsername());

        if (existingUsername != null &&
                !existingUsername.getId().equals(id)) {

            return ResponseEntity
                    .badRequest()
                    .body("Username already exists.");
        }

        // Check if another user already has email
        User existingEmail =
                userRepository.findByEmail(
                        userDetails.getEmail());

        if (existingEmail != null &&
                !existingEmail.getId().equals(id)) {

            return ResponseEntity
                    .badRequest()
                    .body("Email already exists.");
        }

        user.setFullName(userDetails.getFullName());
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());

        // Admin can change role
     // Admin can change role
        if (userDetails.getRole() != null &&
                !userDetails.getRole().isBlank()) {

            String newRole = userDetails.getRole();

            // Only JOB_SEEKER and EMPLOYER can be assigned
            if (!newRole.equals("JOB_SEEKER") &&
                    !newRole.equals("EMPLOYER")) {

                return ResponseEntity
                        .badRequest()
                        .body("Invalid role. Use JOB_SEEKER or EMPLOYER.");
            }

            // Existing ADMIN accounts cannot have their role changed
            if ("ADMIN".equals(user.getRole())) {

                return ResponseEntity
                        .badRequest()
                        .body("Administrator role cannot be changed.");
            }

            user.setRole(newRole);
        }

        // Only change password if one was supplied
        if (userDetails.getPassword() != null &&
                !userDetails.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(
                            userDetails.getPassword()
                    )
            );
        }

        User updatedUser =
                userRepository.save(user);

        UserResponse response =
                new UserResponse(
                        updatedUser.getId(),
                        updatedUser.getFullName(),
                        updatedUser.getUsername(),
                        updatedUser.getEmail(),
                        updatedUser.getPhone(),
                        updatedUser.getRole()
                );

        return ResponseEntity.ok(response);
    }
 // =========================================
 // UPDATE MY PROFILE
 // LOGGED-IN USER ONLY
 // =========================================

 @PutMapping("/me")
 public ResponseEntity<?> updateMyProfile(
         @RequestBody User userDetails,
         Authentication authentication) {

     String username = authentication.getName();

     User user = userRepository.findByUsername(username);

     if (user == null) {
         return ResponseEntity.notFound().build();
     }

     // Check if another user already has this email
     User existingEmail =
             userRepository.findByEmail(userDetails.getEmail());

     if (existingEmail != null &&
             !existingEmail.getId().equals(user.getId())) {

         return ResponseEntity
                 .badRequest()
                 .body("Email already exists.");
     }

     // Update profile information
     user.setFullName(userDetails.getFullName());
     user.setEmail(userDetails.getEmail());
     user.setPhone(userDetails.getPhone());

     User updatedUser =
             userRepository.save(user);

     UserResponse response =
             new UserResponse(
                     updatedUser.getId(),
                     updatedUser.getFullName(),
                     updatedUser.getUsername(),
                     updatedUser.getEmail(),
                     updatedUser.getPhone(),
                     updatedUser.getRole()
             );

     return ResponseEntity.ok(response);
 }
    // =========================================
    // DELETE USER
    // ADMIN ONLY
    // =========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            Authentication authentication) {

        if (!authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            return ResponseEntity.status(403)
                    .body("Access denied. Admin only.");
        }

        if (userRepository.existsById(id)) {

            userRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}

