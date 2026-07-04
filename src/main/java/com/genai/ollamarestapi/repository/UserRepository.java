package com.genai.ollamarestapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.genai.ollamarestapi.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username.
     * Spring Security uses this during authentication.
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);

    /**
     * Find user by email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check whether username already exists.
     */
    boolean existsByUsername(String username);

    /**
     * Check whether email already exists.
     */
    boolean existsByEmail(String email);

}