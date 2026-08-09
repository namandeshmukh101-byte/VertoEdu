package com.vertoedu.service;

import com.vertoedu.dto.UserDto;
import com.vertoedu.entity.Role;
import com.vertoedu.entity.User;
import com.vertoedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * UserService — Business logic for user operations.
 * Handles user creation from Google OAuth and role assignment.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Value("${app.admin-emails:}")
    private String adminEmails;

    @Value("${app.teacher-emails:}")
    private String teacherEmails;

    /**
     * Find or create a user from Google OAuth information.
     * - If user exists by email, update Google info and return.
     * - If user is new, create with appropriate role (ADMIN if in admin-emails, PARENT otherwise).
     */
    @Transactional
    public User findOrCreateUser(String googleId, String email, String fullName, String profileImage) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Update profile info from Google on each login
            user.setGoogleId(googleId);
            user.setFullName(fullName);
            user.setProfileImage(profileImage);
            log.info("Existing user logged in: {} ({})", email, user.getRole().getName());
            return userRepository.save(user);
        }

        // New user — determine role
        Role role = determineRole(email);

        User newUser = User.builder()
                .googleId(googleId)
                .email(email)
                .fullName(fullName)
                .profileImage(profileImage)
                .role(role)
                .active(true)
                .build();

        User saved = userRepository.save(newUser);
        log.info("New user created: {} with role {}", email, role.getName());
        return saved;
    }

    /**
     * Find user by email.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find user by ID.
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * Convert User entity to UserDto.
     */
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .role(user.getRole().getName())
                .active(user.getActive())
                .build();
    }

    /**
     * Determine the role for a new user based on admin-emails configuration.
     * If the user's email is in the admin-emails list, assign ADMIN.
     * Otherwise, assign PARENT (default per PRD).
     */
    private Role determineRole(String email) {
        if (getEmailList(adminEmails).contains(email.toLowerCase().trim())) {
            log.info("Email {} matched admin-emails list — assigning ADMIN role", email);
            return roleService.findByName("ADMIN");
        }
        
        if (getEmailList(teacherEmails).contains(email.toLowerCase().trim())) {
            log.info("Email {} matched teacher-emails list — assigning TEACHER role", email);
            return roleService.findByName("TEACHER");
        }

        return roleService.findByName("PARENT");
    }

    /**
     * Parse the comma-separated emails config into a list.
     */
    private List<String> getEmailList(String emails) {
        if (emails == null || emails.isBlank()) {
            return List.of();
        }
        return Arrays.stream(emails.split(","))
                .map(e -> e.toLowerCase().trim())
                .filter(e -> !e.isEmpty())
                .toList();
    }
}
