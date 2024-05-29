package com.keb.kebsmartfarm.dto;

import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.Plant;
import com.keb.kebsmartfarm.entity.PlantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlantRequestDto {
    private String plantName;
    private String plantNickName;
    private ArduinoKit arduinoKit;
    private MultipartFile file;
    private Path storedPath;
    // 추후 Plant에 파일 이름과 파일 저장 위치 넣어줘야함.

    public Plant toPlant(ArduinoKit arduinoKit) {
        return Plant.builder()
                .plantName(getPlantName())
                .plantNickName(getPlantNickName())
                .arduinoKit(arduinoKit)
                .plantRegDate(LocalDateTime.now())
                .orgFileName(file.getOriginalFilename())
                .storedFilePath(storedPath.toString())
                .status(PlantStatus.GROWING)
                .build();
    }

    // 저장하고 주입
    public void setStoredPath(Path storedPath) {
        this.storedPath = storedPath;
    }

    public static PlantRequestDto of(String plantName, String plantNickName, MultipartFile file) {
        return PlantRequestDto.builder()
                .plantName(plantName)
                .plantNickName(plantNickName)
                .file(file).build();
    }
}
