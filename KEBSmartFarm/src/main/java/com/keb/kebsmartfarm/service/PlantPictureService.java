package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.dto.PlantPictureRequestDto;
import com.keb.kebsmartfarm.dto.PlantPictureResponseDto;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface PlantPictureService {

    void init();

    void store(PlantPictureRequestDto plantPictureDto);

    List<PlantPictureResponseDto> loadAllByPlantNum(Long plantNum);

    Path load(String filename);

    Resource loadAsResource(String filename);
}
