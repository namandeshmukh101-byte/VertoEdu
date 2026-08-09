package com.vertoedu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JpaConfig — Configuration for Spring Data JPA, enabling auditing features.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
