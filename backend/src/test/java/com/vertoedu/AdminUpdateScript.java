package com.vertoedu;

import com.vertoedu.entity.Role;
import com.vertoedu.entity.User;
import com.vertoedu.repository.RoleRepository;
import com.vertoedu.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class AdminUpdateScript {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void executeAdminPromotion() {
        String targetEmail = "clashclasher1124@gmail.com";
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        Optional<User> optionalUser = userRepository.findByEmail(targetEmail);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            System.out.println("Found existing user: " + user.getEmail() + " with current role: " + user.getRole().getName());
        } else {
            System.out.println("User not found. Creating placeholder user for OAuth...");
            user = new User();
            user.setEmail(targetEmail);
            user.setFullName("Pending OAuth Admin");
            user.setActive(true);
        }

        user.setRole(adminRole);
        userRepository.save(user);

        // Verify
        User savedUser = userRepository.findByEmail(targetEmail).get();
        System.out.println("\n==========================================");
        System.out.println("VERIFICATION SCRIPT RESULT:");
        System.out.println("User ID: " + savedUser.getId());
        System.out.println("User Email: " + savedUser.getEmail());
        System.out.println("User Role: " + savedUser.getRole().getName());
        System.out.println("==========================================\n");
    }
}
