package com.jobconnect.jobconnect.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jobconnect.jobconnect.Application;
import com.jobconnect.jobconnect.Job;
import com.jobconnect.jobconnect.dto.AdminRoleUpdateRequest;
import com.jobconnect.jobconnect.dto.AdminStatisticsResponse;
import com.jobconnect.jobconnect.dto.AdminUserResponse;
import com.jobconnect.jobconnect.entity.User;
import com.jobconnect.jobconnect.repository.ApplicationRepository;
import com.jobconnect.jobconnect.repository.JobRepository;
import com.jobconnect.jobconnect.repository.UserRepository;
import com.jobconnect.jobconnect.Job;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    private final JobRepository jobRepository;

    private final ApplicationRepository applicationRepository;


    public AdminController(

            UserRepository userRepository,

            JobRepository jobRepository,

            ApplicationRepository applicationRepository) {

        this.userRepository = userRepository;

        this.jobRepository = jobRepository;

        this.applicationRepository = applicationRepository;
    }


    // =========================================
    // ADMIN DASHBOARD STATISTICS
    // ADMIN ONLY
    // =========================================

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(
            Authentication authentication) {

        // Check ADMIN role

        if (!authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"))) {

            return ResponseEntity
                    .status(403)
                    .body("Access denied. Admin only.");
        }


        // Total users

        long totalUsers =
                userRepository.count();


        // Total job seekers

        long totalJobSeekers =
                userRepository.findAll()
                        .stream()
                        .filter(user ->
                                "JOB_SEEKER".equals(
                                        user.getRole()))
                        .count();


        // Total employers

        long totalEmployers =
                userRepository.findAll()
                        .stream()
                        .filter(user ->
                                "EMPLOYER".equals(
                                        user.getRole()))
                        .count();


        // Total jobs

        long totalJobs =
                jobRepository.count();


        // Total applications

        long totalApplications =
                applicationRepository.count();


        // Create response

        AdminStatisticsResponse response =
                new AdminStatisticsResponse(

                        totalUsers,

                        totalJobSeekers,

                        totalEmployers,

                        totalJobs,

                        totalApplications
                );


        return ResponseEntity.ok(response);
    }



    // =========================================
    // ADMIN USER MANAGEMENT
    // GET ALL USERS
    // ADMIN ONLY
    // =========================================

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            Authentication authentication) {

        // Check ADMIN role

        if (!authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"))) {

            return ResponseEntity
                    .status(403)
                    .body("Access denied. Admin only.");
        }


        // Get all users
        // Password is NOT included

        List<AdminUserResponse> users =
                userRepository.findAll()
                        .stream()
                        .map(user ->
                                new AdminUserResponse(

                                        user.getId(),

                                        user.getFullName(),

                                        user.getUsername(),

                                        user.getEmail(),

                                        user.getPhone(),

                                        user.getRole()
                                )
                        )
                        .toList();


        return ResponseEntity.ok(users);
    }



    // =========================================
    // UPDATE USER ROLE
    // ADMIN ONLY
    // =========================================

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(

            @PathVariable Long id,

            @RequestBody AdminRoleUpdateRequest request,

            Authentication authentication) {


        // Check ADMIN role

        if (!authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"))) {

            return ResponseEntity
                    .status(403)
                    .body("Access denied. Admin only.");
        }


        // =====================================
        // FIND USER
        // =====================================

        User user =
                userRepository.findById(id)
                        .orElse(null);


        if (user == null) {

            return ResponseEntity
                    .status(404)
                    .body("User not found.");
        }


        // =====================================
        // VALIDATE ROLE
        // =====================================

        String newRole =
                request.getRole();


        if (newRole == null ||

                (!newRole.equals("JOB_SEEKER")

                && !newRole.equals("EMPLOYER"))) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Invalid role. Use JOB_SEEKER or EMPLOYER."
                    );
        }


        // =====================================
        // PROTECT ADMIN ACCOUNT
        // =====================================

        if ("ADMIN".equals(user.getRole())) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Administrator role cannot be changed."
                    );
        }


        // =====================================
        // UPDATE ROLE
        // =====================================

        user.setRole(newRole);

        userRepository.save(user);


        // =====================================
        // RETURN UPDATED USER
        // =====================================

        AdminUserResponse response =
                new AdminUserResponse(

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
 // ADMIN JOB MANAGEMENT
 // GET ALL JOBS
 // ADMIN ONLY
 // =========================================

 @GetMapping("/jobs")
 public ResponseEntity<?> getAllJobs(
         Authentication authentication) {

     // Check ADMIN role

     if (!authentication.getAuthorities()
             .stream()
             .anyMatch(a ->
                     a.getAuthority().equals("ROLE_ADMIN"))) {

         return ResponseEntity
                 .status(403)
                 .body("Access denied. Admin only.");
     }


     // Get all jobs

     List<Job> jobs =
             jobRepository.findAll();


     return ResponseEntity.ok(jobs);
 }



 // =========================================
 // GET ONE JOB
 // ADMIN ONLY
 // =========================================

 @GetMapping("/jobs/{id}")
 public ResponseEntity<?> getAdminJobById(

         @PathVariable Long id,

         Authentication authentication) {

     // Check ADMIN role

     if (!authentication.getAuthorities()
             .stream()
             .anyMatch(a ->
                     a.getAuthority().equals("ROLE_ADMIN"))) {

         return ResponseEntity
                 .status(403)
                 .body("Access denied. Admin only.");
     }


     // Find job

     return jobRepository
             .findById(id)
             .map(ResponseEntity::ok)
             .orElseGet(() ->
                     ResponseEntity
                             .notFound()
                             .build()
             );
 }



 // =========================================
 // DELETE JOB
 // ADMIN ONLY
 // =========================================

 @DeleteMapping("/jobs/{id}")
 public ResponseEntity<?> deleteAdminJob(

         @PathVariable Long id,

         Authentication authentication) {

     // Check ADMIN role

     if (!authentication.getAuthorities()
             .stream()
             .anyMatch(a ->
                     a.getAuthority().equals("ROLE_ADMIN"))) {

         return ResponseEntity
                 .status(403)
                 .body("Access denied. Admin only.");
     }


     // Find job

     Job job =
             jobRepository.findById(id)
                     .orElse(null);


     if (job == null) {

         return ResponseEntity
                 .status(404)
                 .body("Job not found.");
     }


     // Delete job

     jobRepository.delete(job);


     return ResponseEntity
             .ok("Job deleted successfully.");
 }
//=========================================
//ADMIN APPLICATION MANAGEMENT
//GET ALL APPLICATIONS
//ADMIN ONLY
//=========================================

@GetMapping("/applications")
public ResponseEntity<?> getAllApplications(
      Authentication authentication) {

  if (!authentication.getAuthorities()
          .stream()
          .anyMatch(a ->
                  a.getAuthority().equals("ROLE_ADMIN"))) {

      return ResponseEntity
              .status(403)
              .body("Access denied. Admin only.");
  }

  List<Application> applications =
          applicationRepository.findAll();

  return ResponseEntity.ok(applications);
}


//=========================================
//GET ONE APPLICATION
//ADMIN ONLY
//=========================================

@GetMapping("/applications/{id}")
public ResponseEntity<?> getAdminApplicationById(
      @PathVariable Long id,
      Authentication authentication) {

  if (!authentication.getAuthorities()
          .stream()
          .anyMatch(a ->
                  a.getAuthority().equals("ROLE_ADMIN"))) {

      return ResponseEntity
              .status(403)
              .body("Access denied. Admin only.");
  }

  return applicationRepository
          .findById(id)
          .map(ResponseEntity::ok)
          .orElseGet(() ->
                  ResponseEntity
                          .notFound()
                          .build()
          );
}


//=========================================
//DELETE APPLICATION
//ADMIN ONLY
//=========================================

@DeleteMapping("/applications/{id}")
public ResponseEntity<?> deleteAdminApplication(
      @PathVariable Long id,
      Authentication authentication) {

  if (!authentication.getAuthorities()
          .stream()
          .anyMatch(a ->
                  a.getAuthority().equals("ROLE_ADMIN"))) {

      return ResponseEntity
              .status(403)
              .body("Access denied. Admin only.");
  }

  Application application =
          applicationRepository.findById(id)
                  .orElse(null);

  if (application == null) {

      return ResponseEntity
              .status(404)
              .body("Application not found.");
  }

  applicationRepository.delete(application);

  return ResponseEntity
          .ok("Application deleted successfully.");
}
}