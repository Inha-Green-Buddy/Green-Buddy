package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.dto.PreviousPlantDto;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.Plant;
import com.keb.kebsmartfarm.repository.PreviousPlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreviousPlantService {
    private final PreviousPlantRepository previousPlantRepository;

    public List<PreviousPlantDto> getPlantList(long seqNum) {
        return previousPlantRepository.findAllByUserSeqNum(seqNum)
                .stream().map(PreviousPlantDto::of)
                .collect(Collectors.toList());
    }

    public PreviousPlantDto movePlantToPreviousPlant(ArduinoKit arduinoKit) {
        Plant plant = arduinoKit.getActivePlant()
                // 식물이 없으면 오류 발생
                .orElseThrow(() -> new IllegalStateException("식물이 존재하지 않습니다"));
        // 식물이 있으면 이전 식물로
        return PreviousPlantDto.of(previousPlantRepository.save(PlantService.toPreviousPlant(plant)));
    }


}
