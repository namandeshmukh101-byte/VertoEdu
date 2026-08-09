package com.vertoedu.service;

import com.vertoedu.entity.Role;
import com.vertoedu.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * RoleService — Business logic for role operations.
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * Find a role by its name.
     *
     * @param name the role name (e.g., "ADMIN", "TEACHER", "PARENT")
     * @return the Role entity
     * @throws RuntimeException if role not found
     */
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }

    /**
     * Check if a role exists by name.
     */
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}
