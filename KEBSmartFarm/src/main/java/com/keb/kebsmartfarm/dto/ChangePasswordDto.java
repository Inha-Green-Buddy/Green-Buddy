package com.keb.kebsmartfarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    private String userId;
    private String exPassword;
    private String newPassword;
}
