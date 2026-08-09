package com.vertoedu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentProfileUpdateDto {
    private String phone;
    private String alternateContact;
    private String address;
}
