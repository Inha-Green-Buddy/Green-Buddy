package com.keb.kebsmartfarm.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@Setter
public class MailDto {
    private String address;
    private String title;
    private String message;
}
