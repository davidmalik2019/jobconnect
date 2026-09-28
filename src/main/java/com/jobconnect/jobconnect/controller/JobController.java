
package com.jobconnect.jobconnect.controller;

import com.jobconnect.jobconnect.Job;
import com.jobconnect.jobconnect.repository.JobRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }


    // =========================================
    // POST A NEW JOB
    // =========================================

    @PostMapping
    public ResponseEntity<?> createJob(
            @RequestBody Job job,
            Authentication authentication) {

        // Get the username of the logged-in employer
        String employerUsername =
                authentication.getName();

        // Store the employer username
        job.setEmployerUsername(
                employerUsername
        );

        // Store the date and time
        job.setPostedAt(
                LocalDateTime.now()
        );

        // Save job to MySQL
        Job savedJob =
                jobRepository.save(job);

        return ResponseEntity.ok(savedJob);
    }


    // =========================================
    // GET ALL JOBS
    // =========================================

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {

        List<Job> jobs =
                jobRepository.findAll();

        return ResponseEntity.ok(jobs);
    }


    // =========================================
    // GET ONE JOB
    // =========================================

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(
            @PathVariable Long id) {

        return jobRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }


    // =========================================
    // GET JOBS POSTED BY CURRENT EMPLOYER
    // =========================================

    @GetMapping("/my")
    public ResponseEntity<List<Job>> getMyJobs(
            Authentication authentication) {

        String employerUsername =
                authentication.getName();

        List<Job> jobs =
                jobRepository
                    .findByEmployerUsername(
                        employerUsername
                    );

        return ResponseEntity.ok(jobs);
    }


    // =========================================
    // DELETE JOB
    // =========================================
 // ==============================================
 // EDIT JOB
 // ==============================================

 @PutMapping("/{id}")
 public ResponseEntity<?> updateJob(
         @PathVariable Long id,
         @RequestBody Job updatedJob,
         Authentication authentication) {

     String employerUsername =
             authentication.getName();

     return jobRepository
             .findById(id)
             .map(job -> {

                 // Make sure the employer owns this job
                 if (!job.getEmployerUsername()
                         .equals(employerUsername)) {

                     return ResponseEntity
                             .status(403)
                             .body("You are not allowed to edit this job.");
                 }

                 // Update job details
                 job.setJobTitle(
                         updatedJob.getJobTitle()
                 );

                 job.setCompany(
                         updatedJob.getCompany()
                 );

                 job.setLocation(
                         updatedJob.getLocation()
                 );

                 job.setJobType(
                         updatedJob.getJobType()
                 );

                 job.setSalary(
                         updatedJob.getSalary()
                 );

                 job.setDescription(
                         updatedJob.getDescription()
                 );

                 job.setRequirements(
                         updatedJob.getRequirements()
                 );

                 Job savedJob =
                         jobRepository.save(job);

                 return ResponseEntity.ok(savedJob);

             })
             .orElseGet(() ->
                     ResponseEntity
                             .notFound()
                             .build()
             );
 }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(
            @PathVariable Long id,
            Authentication authentication) {

        String employerUsername =
                authentication.getName();

        return jobRepository
                .findById(id)
                .map(job -> {

                    // Make sure employer owns the job
                    if (!job.getEmployerUsername()
                            .equals(employerUsername)) {

                        return ResponseEntity
                                .status(403)
                                .body("You are not allowed to delete this job.");
                    }

                    jobRepository.delete(job);

                    return ResponseEntity
                            .ok("Job deleted successfully.");

                })
                .orElseGet(() ->
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }
}

