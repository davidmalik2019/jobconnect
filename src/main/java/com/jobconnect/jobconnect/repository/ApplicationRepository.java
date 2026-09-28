package com.jobconnect.jobconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobconnect.jobconnect.Application;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    // =========================================
    // FIND APPLICATIONS BY JOB SEEKER
    // =========================================

    List<Application> findByUsername(String username);


    // =========================================
    // FIND APPLICATIONS BY JOB ID
    // =========================================

    List<Application> findByJobId(Long jobId);
 // =========================================
 // CHECK DUPLICATE APPLICATION
 // =========================================

 boolean existsByUsernameAndJobId(
         String username,
         Long jobId
 );

}