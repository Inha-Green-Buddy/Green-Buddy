package com.keb.kebsmartfarm.dto;

import com.keb.kebsmartfarm.util.SecurityUtil;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArduinoRequestDto {
    private Long userSeqNum;
    private String serialNum;
    private String deviceName;
    public ArduinoKit toArduinoKit() {
        return ArduinoKit.builder()
                .serialNum(getSerialNum())
                .date(LocalDateTime.now())
                .userSeqNum(SecurityUtil.getCurrentUserId())
                .deviceName(getDeviceName())
                .build();
    }

}
