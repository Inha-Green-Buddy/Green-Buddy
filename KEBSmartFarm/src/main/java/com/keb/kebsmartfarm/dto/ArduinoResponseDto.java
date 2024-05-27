package com.keb.kebsmartfarm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.keb.kebsmartfarm.util.PictureUtils;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.Plant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArduinoResponseDto {
    private Long kitNo;
    private String kitSerialNum;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PlantResponseDto plant;
    private LocalDateTime date;
    private String kitDeviceName;

    public static ArduinoResponseDto of(ArduinoKit arduinoKit) {
        PlantResponseDto responseDto = null;

        if (arduinoKit.hasPlant()) {
            Plant activePlant = arduinoKit.getActivePlant();

            responseDto = PlantResponseDto.of(activePlant);
            responseDto.setProfileImg(PictureUtils.getUrl(Path.of(activePlant.getStoredFilePath())));
        }

        return ArduinoResponseDto.builder()
                .kitNo(arduinoKit.getKitNo())
                .kitSerialNum(arduinoKit.getSerialNum())
                .date(arduinoKit.getDate())
                .plant(responseDto)
                .kitDeviceName(arduinoKit.getDeviceName())
                .build();
    }

    public static List<ArduinoResponseDto> ofList(List<ArduinoKit> arduinoKitList) {
        return arduinoKitList.stream()
                .map(ArduinoResponseDto::of)
                .collect(Collectors.toList());
    }
}
