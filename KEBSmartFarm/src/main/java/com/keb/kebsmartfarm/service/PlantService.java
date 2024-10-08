package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.util.PictureUtils;
import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.PlantRequestDto;
import com.keb.kebsmartfarm.dto.PlantResponseDto;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.Plant;
import com.keb.kebsmartfarm.repository.PlantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
@AllArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;

    @Transactional
    public PlantResponseDto createPlant(ArduinoKit arduinoKit, PlantRequestDto requestDto) {
        // 키트에 이미 키우는 식물이 있으면 오류 반환
        if (arduinoKit.hasPlant()) {
            throw new IllegalStateException(Error.KIT_ALREADY_HAVE_PLANT);
        }
        // 없으면 식물 추가 가능
        MultipartFile file = requestDto.getFile();
        Path destPath = PictureUtils.getDestPath(file);
        try {
            file.transferTo(destPath);
            requestDto.setStoredPath(destPath);
            return PlantResponseDto.of(plantRepository.save(requestDto.toPlant(arduinoKit)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteActivePlant(ArduinoKit arduinoKit) {
        Plant plant = arduinoKit.getActivePlant();
        // 삭제 시 반드시 연관관계 해제가 필요
        arduinoKit.deletePlant(plant);
        log.info(plant.toString());
        plantRepository.delete(plant);
    }

    @Transactional
    public PlantResponseDto cultivatePlant(ArduinoKit arduinoKit) {
        Plant activePlant = arduinoKit.getActivePlant();
        activePlant.endGrowth();
        return PlantResponseDto.of(plantRepository.save(activePlant));
    }
}
