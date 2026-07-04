package com.genai.ollamarestapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.genai.ollamarestapi.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find role by role name.
     *
     * Examples:
     * ROLE_ADMIN
     * ROLE_USER
     */
    Optional<Role> findByName(String name);

    /**
     * Check if role already exists.
     */
    boolean existsByName(String name);

}