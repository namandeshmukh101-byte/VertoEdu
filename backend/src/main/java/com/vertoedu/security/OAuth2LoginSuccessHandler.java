package com.vertoedu.security;

import com.vertoedu.entity.User;
import com.vertoedu.security.JwtTokenProvider;
import com.vertoedu.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2LoginSuccessHandler — Handles successful Google OAuth login.
 * Creates/updates user in database, generates JWT, sets cookie, and redirects
 * to the appropriate frontend route based on the user's role.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${jwt.cookie-max-age-seconds:86400}")
    private int cookieMaxAge;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");
        String profileImage = oAuth2User.getAttribute("picture");

        // Find or create the user in the database
        User user = userService.findOrCreateUser(googleId, email, fullName, profileImage);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getRole().getName(),
                user.getId()
        );

        // Set JWT as HTTP-only cookie
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // Set to true in production with HTTPS
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(cookieMaxAge);
        response.addCookie(jwtCookie);

        // Redirect to frontend based on role
        String redirectUrl = getRedirectUrlForRole(user.getRole().getName());
        log.info("OAuth2 login success for {} — redirecting to {}", email, redirectUrl);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    /**
     * Determine the frontend redirect URL based on the user's role.
     */
    private String getRedirectUrlForRole(String roleName) {
        return switch (roleName) {
            case "ADMIN" -> frontendUrl + "/admin";
            case "TEACHER" -> frontendUrl + "/teacher";
            case "PARENT" -> frontendUrl + "/parent";
            default -> frontendUrl + "/";
        };
    }
}
