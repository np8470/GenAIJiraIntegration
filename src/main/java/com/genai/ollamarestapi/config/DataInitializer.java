package com.genai.ollamarestapi.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.genai.ollamarestapi.entity.Role;
import com.genai.ollamarestapi.entity.User;
import com.genai.ollamarestapi.repository.RoleRepository;
import com.genai.ollamarestapi.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        createRoles();

        createAdminUser();

        createNormalUser();

        System.out.println("-------------------------------------------");
        System.out.println(" Default Security Data Initialized");
        System.out.println(" Admin Username : admin");
        System.out.println(" Admin Password : admin123");
        System.out.println(" User Username  : user");
        System.out.println(" User Password  : user123");
        System.out.println("-------------------------------------------");
    }

    /**
     * Create default roles
     */
    private void createRoles() {

        if (!roleRepository.existsByName("ROLE_ADMIN")) {

            Role adminRole = Role.builder()
                    .name("ROLE_ADMIN")
                    .description("System Administrator")
                    .build();

            roleRepository.save(adminRole);

            System.out.println("ROLE_ADMIN created.");
        }

        if (!roleRepository.existsByName("ROLE_USER")) {

            Role userRole = Role.builder()
                    .name("ROLE_USER")
                    .description("Standard User")
                    .build();

            roleRepository.save(userRole);

            System.out.println("ROLE_USER created.");
        }

    }

    /**
     * Create Admin User
     */
    private void createAdminUser() {

        if (userRepository.existsByUsername("admin")) {
            return;
        }

        Role adminRole = roleRepository
                .findByName("ROLE_ADMIN")
                .orElseThrow(() ->
                        new RuntimeException("ROLE_ADMIN not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        User admin = User.builder()
                .username("admin")
                .email("admin@testpilot.ai")
                .password(passwordEncoder.encode("admin123"))
                .enabled(true)
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .roles(roles)
                .build();

        userRepository.save(admin);

        System.out.println("Admin user created.");
    }

    /**
     * Create Standard User
     */
    private void createNormalUser() {

        if (userRepository.existsByUsername("user")) {
            return;
        }

        Role userRole = roleRepository
                .findByName("ROLE_USER")
                .orElseThrow(() ->
                        new RuntimeException("ROLE_USER not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .username("user")
                .email("user@testpilot.ai")
                .password(passwordEncoder.encode("user123"))
                .enabled(true)
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .roles(roles)
                .build();

        userRepository.save(user);

        System.out.println("Standard user created.");
    }

}