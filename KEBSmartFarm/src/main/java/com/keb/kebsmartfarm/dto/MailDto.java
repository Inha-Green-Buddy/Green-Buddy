package com.keb.kebsmartfarm.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class MailDto {
    private String address;
    private String title;
    private String message;
}
