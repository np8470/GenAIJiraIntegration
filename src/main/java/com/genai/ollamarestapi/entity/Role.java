package com.genai.ollamarestapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Role Name
     * Examples:
     * ROLE_ADMIN
     * ROLE_USER
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Optional Description
     */
    @Column(length = 255)
    private String description;

    /**
     * Users assigned to this role
     */
    @Builder.Default
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    /**
     * Convenience method
     */
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public String toString() {
        return name;
    }

}