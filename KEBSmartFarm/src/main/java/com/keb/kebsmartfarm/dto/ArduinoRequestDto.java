package com.keb.kebsmartfarm.dto;

import com.keb.kebsmartfarm.config.SecurityUtil;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

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
                // 문제
                .date(LocalDateTime.now())
                .userSeqNum(SecurityUtil.getCurrentUserId())
                .deviceName(getDeviceName())
                // 일단 임시로 NPE 생기지 않도록 조치함
                .PlantList(new ArrayList<>())
                .build();
    }

}
