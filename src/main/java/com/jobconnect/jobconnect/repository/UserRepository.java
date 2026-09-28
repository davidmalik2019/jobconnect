
package com.jobconnect.jobconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobconnect.jobconnect.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username
    User findByUsername(String username);

    // Find user by email
    User findByEmail(String email);
}

