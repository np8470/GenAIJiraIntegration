package com.genai.ollamarestapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Login Username
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * User Email
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * BCrypt Password
     */
    @Column(nullable = false)
    private String password;

    /**
     * Display Name
     */
    @Column(length = 100)
    private String fullName;

    /**
     * Account Enabled
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * Account Locked
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean accountLocked = false;

    /**
     * Account Expired
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean accountExpired = false;

    /**
     * Credentials Expired
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean credentialsExpired = false;

    /**
     * Creation Time
     */
    /*
     * @Builder.Default
     * 
     * @Column(nullable = false, updatable = false)
     * private LocalDateTime createdAt = LocalDateTime.now();
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last Updated
     */
    /*
     * @Builder.Default
     * 
     * @Column(nullable = false)
     * private LocalDateTime updatedAt = LocalDateTime.now();
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Last Login Time
     */
    private LocalDateTime lastLogin;

    /**
     * User Roles
     */
        /* @Builder.Default
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Set<Role> roles = new HashSet<>(); */

    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"),
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "role_id"})
    )
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Convenience method
     */
    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream()
            .anyMatch(r -> r.getName().equalsIgnoreCase(roleName));
    }

    @OneToMany(mappedBy = "user")
    private List<GenerationHistory> histories = new ArrayList<>();

}