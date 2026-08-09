package com.vertoedu.dto;

import lombok.*;

/**
 * UserDto — Data Transfer Object for user information sent to the frontend.
 * Never exposes internal IDs or sensitive data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String fullName;
    private String email;
    private String profileImage;
    private String role;
    private Boolean active;
}
