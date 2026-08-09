package com.vertoedu.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2LoginFailureHandler — Handles failed Google OAuth login attempts.
 * Redirects to the frontend login page with an error message.
 */
@Component
@Slf4j
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        log.error("OAuth2 login failed: {}", exception.getMessage());

        String errorMessage = URLEncoder.encode("Authentication failed. Please try again.",
                StandardCharsets.UTF_8);

        getRedirectStrategy().sendRedirect(request, response,
                frontendUrl + "/login?error=" + errorMessage);
    }
}
