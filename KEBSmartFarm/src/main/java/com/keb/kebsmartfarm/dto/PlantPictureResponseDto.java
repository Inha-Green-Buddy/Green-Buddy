package com.keb.kebsmartfarm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keb.kebsmartfarm.entity.PlantPicture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlantPictureResponseDto {
    private String msg;
    private long plantNo;
    private LocalDateTime date;
    @JsonIgnore
    private Path storedPath;
    private String imageUrl;

    public static PlantPictureResponseDto of(PlantPicture picture) {
        return PlantPictureResponseDto.builder()
                .msg(picture.getMsg())
                .plantNo(picture.getPlantNum())
                .storedPath(Path.of(picture.getStoredFilePath()))
                .date(picture.getDate())
                .build();
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
