package com.jobconnect.jobconnect.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.jobconnect.jobconnect.Application;
import com.jobconnect.jobconnect.Job;
import com.jobconnect.jobconnect.repository.ApplicationRepository;
import com.jobconnect.jobconnect.repository.JobRepository;

@RestController
@RequestMapping("/applications")
@CrossOrigin
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;


    // =========================================
    // SUBMIT APPLICATION
    // =========================================

    @PostMapping
    public Application submitApplication(
            @RequestBody Application application,
            Authentication authentication) {

        String username = authentication.getName();
        

        // -----------------------------------------
        // GET JOB ID FROM APPLICATION
        // -----------------------------------------

        Long jobId = application.getJobId();
       

        if (jobId == null) {

            throw new RuntimeException(
                "Job ID is required."
            );
        }

        // -----------------------------------------
        // FIND THE JOB
        // -----------------------------------------

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Job not found."
                    )
                    
                );
        

        // -----------------------------------------
        // SAVE JOB INFORMATION
        // -----------------------------------------

        application.setJobId(job.getId());

        application.setJobTitle(
            job.getJobTitle()
        );

     // -----------------------------------------
     // SAVE LOGGED-IN JOB SEEKER
     // -----------------------------------------

     application.setUsername(username);


     // -----------------------------------------
     // CHECK DUPLICATE APPLICATION
     // -----------------------------------------

     boolean alreadyApplied =
             applicationRepository
                     .existsByUsernameAndJobId(
                         username,
                         jobId
                     );
    
     if (alreadyApplied) {
    	    throw new ResponseStatusException(
    	        HttpStatus.CONFLICT,
    	        "You have already applied for this job."
    	    );
    	}


     // -----------------------------------------
     // SET APPLICATION STATUS
     // -----------------------------------------

     application.setStatus("Pending");
        application.setAppliedAt(
            LocalDateTime.now()
        );
        

        Application savedApplication =
                applicationRepository.save(application);

      
        return savedApplication;
        
    }


    // =========================================
    // GET APPLICATIONS
    // =========================================

    @GetMapping
    public List<Application> getAllApplications(
            Authentication authentication) {

        String username = authentication.getName();

        /*
         * Find all jobs belonging to the logged-in
         * employer.
         */
        List<Job> employerJobs =
                jobRepository.findByEmployerUsername(
                    username
                );

        /*
         * Collect applications belonging to
         * those jobs.
         */
        List<Application> employerApplications =
                new ArrayList<>();

        for (Job job : employerJobs) {

            List<Application> applications =
                    applicationRepository.findByJobId(
                        job.getId()
                    );

            employerApplications.addAll(
                applications
            );
        }

        return employerApplications;
    }


    // =========================================
    // GET MY APPLICATIONS
    // =========================================

    @GetMapping("/my")
    public List<Application> getMyApplications(
            Authentication authentication) {

        String username = authentication.getName();

        return applicationRepository.findByUsername(
            username
        );
    }


    // =========================================
    // GET ONE APPLICATION
    // =========================================

    @GetMapping("/{id}")
    public Application getApplicationById(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        Application application =
                applicationRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Application not found"
                    )
                );

        // -----------------------------------------
        // JOB SEEKER CAN ONLY VIEW OWN APPLICATION
        // -----------------------------------------

        if (!application.getUsername()
                .equals(username)) {

            throw new RuntimeException(
                "You are not authorized to view this application"
            );
        }

        return application;
    }
 // =========================================
 // EDIT APPLICATION
 // =========================================

 @PutMapping("/{id}")
 public Application editApplication(
         @PathVariable Long id,
         @RequestBody Application updatedApplication,
         Authentication authentication) {

     String username = authentication.getName();

     // -----------------------------------------
     // FIND APPLICATION
     // -----------------------------------------

     Application application =
             applicationRepository.findById(id)
             .orElseThrow(() ->
                 new RuntimeException(
                     "Application not found."
                 )
             );

     // -----------------------------------------
     // CHECK OWNERSHIP
     // -----------------------------------------

     if (!application.getUsername()
             .equals(username)) {

         throw new RuntimeException(
             "You are not authorized to edit this application."
         );
     }

     // -----------------------------------------
     // ONLY PENDING APPLICATIONS CAN BE EDITED
     // -----------------------------------------

     if (!"Pending".equalsIgnoreCase(
             application.getStatus())) {

         throw new RuntimeException(
             "Only Pending applications can be edited."
         );
     }

     // -----------------------------------------
     // UPDATE ONLY ALLOWED FIELDS
     // -----------------------------------------

     application.setFullName(
         updatedApplication.getFullName()
     );

     application.setEmail(
         updatedApplication.getEmail()
     );

     application.setPhone(
         updatedApplication.getPhone()
     );

     application.setCoverLetter(
         updatedApplication.getCoverLetter()
     );

     // -----------------------------------------
     // SAVE UPDATED APPLICATION
     // -----------------------------------------

     return applicationRepository.save(
         application
     );
 }

//=========================================
//WITHDRAW APPLICATION
//=========================================

@DeleteMapping("/{id}/withdraw")
public void withdrawApplication(
      @PathVariable Long id,
      Authentication authentication) {

  String username = authentication.getName();

  // -----------------------------------------
  // FIND APPLICATION
  // -----------------------------------------

  Application application =
          applicationRepository.findById(id)
          .orElseThrow(() ->
              new RuntimeException(
                  "Application not found."
              )
          );

  // -----------------------------------------
  // CHECK OWNERSHIP
  // -----------------------------------------

  if (!application.getUsername()
          .equals(username)) {

      throw new RuntimeException(
          "You are not authorized to withdraw this application."
      );
  }

  // -----------------------------------------
  // ONLY PENDING APPLICATIONS CAN BE WITHDRAWN
  // -----------------------------------------

  if (!"Pending".equalsIgnoreCase(
          application.getStatus())) {

      throw new RuntimeException(
          "Only Pending applications can be withdrawn."
      );
  }

  // -----------------------------------------
  // DELETE APPLICATION
  // -----------------------------------------

  applicationRepository.delete(application);
}  
 // =========================================
    // ACCEPT APPLICATION
    // =========================================

@PutMapping("/{id}/accept")
public Application acceptApplication(
        @PathVariable Long id,
        Authentication authentication) {

    String username = authentication.getName();

    Application application =
            applicationRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Application not found.")
            );

    Job job =
            jobRepository.findById(application.getJobId())
            .orElseThrow(() ->
                new RuntimeException("Job not found.")
            );

    if (!job.getEmployerUsername().equals(username)) {
        throw new RuntimeException(
            "You are not authorized to accept this application."
        );
    }

    if (!"Pending".equalsIgnoreCase(application.getStatus())) {
        throw new RuntimeException(
            "Only Pending applications can be accepted."
        );
    }

    application.setStatus("Accepted");

    return applicationRepository.save(application);
}
    // =========================================
    // REJECT APPLICATION
    // =========================================

@PutMapping("/{id}/reject")
public Application rejectApplication(
        @PathVariable Long id,
        Authentication authentication) {

    String username = authentication.getName();

    Application application =
            applicationRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Application not found.")
            );

    Job job =
            jobRepository.findById(application.getJobId())
            .orElseThrow(() ->
                new RuntimeException("Job not found.")
            );

    if (!job.getEmployerUsername().equals(username)) {
        throw new RuntimeException(
            "You are not authorized to reject this application."
        );
    }

    if (!"Pending".equalsIgnoreCase(application.getStatus())) {
        throw new RuntimeException(
            "Only Pending applications can be rejected."
        );
    }

    application.setStatus("Rejected");

    return applicationRepository.save(application);
}

}