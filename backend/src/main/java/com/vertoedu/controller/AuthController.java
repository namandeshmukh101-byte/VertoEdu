package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.UserDto;
import com.vertoedu.entity.User;
import com.vertoedu.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController — REST endpoints for authentication operations.
 * Handles current user info retrieval and logout.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    /**
     * GET /auth/me — Returns the currently authenticated user's information.
     * Used by the frontend to check session and load user data on page refresh.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }

        String email = (String) authentication.getPrincipal();

        return userService.findByEmail(email)
                .map(user -> {
                    UserDto dto = userService.toDto(user);
                    return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", dto));
                })
                .orElse(ResponseEntity.status(401).body(ApiResponse.error("User not found")));
    }

    /**
     * POST /auth/logout — Clears the JWT cookie and ends the session.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        org.springframework.http.ResponseCookie jwtCookie = org.springframework.http.ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true) // Enforce HTTPS
                .path("/")
                .maxAge(0) // Delete cookie immediately
                .sameSite("None") // Allow Cross-Site frontend
                .build();
        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, jwtCookie.toString());

        SecurityContextHolder.clearContext();
        log.info("User logged out successfully");

        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
}
