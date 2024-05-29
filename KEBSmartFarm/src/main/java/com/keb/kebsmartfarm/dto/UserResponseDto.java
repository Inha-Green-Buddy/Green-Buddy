package com.keb.kebsmartfarm.dto;

import com.keb.kebsmartfarm.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private String userId;
    private String userNickname;
    private List<ArduinoResponseDto> userKitList;
    private LocalDateTime userRegDate;
    public static UserResponseDto of(User user) {
        List<ArduinoResponseDto> arduinoResponseDtoList;
        if(user.getArduinoKitList() == null)
            arduinoResponseDtoList = new ArrayList<>();
        else
            arduinoResponseDtoList = ArduinoResponseDto.ofList(user.getArduinoKitList());

        return UserResponseDto.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userKitList(arduinoResponseDtoList)
                .userRegDate(user.getUserRegDate())
                .build();
    }
}