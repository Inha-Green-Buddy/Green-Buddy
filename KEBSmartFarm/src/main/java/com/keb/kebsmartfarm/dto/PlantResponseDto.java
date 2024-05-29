package com.keb.kebsmartfarm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.Plant;
import com.keb.kebsmartfarm.entity.PlantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlantResponseDto {
    private Long plantNum;
    private String plantName;
    private String plantNickName;
    @JsonIgnore
    private ArduinoKit arduinoKit;
    private LocalDateTime plantRegDate;
    @JsonInclude(Include.NON_NULL)
    private LocalDateTime plantHarvestDate;
    @JsonIgnore
    private PlantStatus status;
    @JsonIgnore
    private Path storedPath;
    private String profileImg;

    public static PlantResponseDto of(Plant plant) {
        return PlantResponseDto.builder()
                .plantName(plant.getPlantName())
                .plantNickName(plant.getPlantNickName())
                .plantNum(plant.getPlantNum())
                .arduinoKit(plant.getArduinoKit())
                .plantRegDate(plant.getPlantRegDate())
                .plantHarvestDate(plant.getPlantHarvestDate())
                .status(plant.getStatus())
                .storedPath(Path.of(plant.getStoredFilePath()))
                .build();
    }


    // serveFile로 경유해서 넣기
    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}