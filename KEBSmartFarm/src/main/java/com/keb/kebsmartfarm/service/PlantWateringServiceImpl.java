package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.PlantWatering;
import com.keb.kebsmartfarm.repository.PlantWaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantWateringServiceImpl implements PlantWateringService {

    private final PlantWaterRepository plantWaterRepository;

    @Override
    @Transactional
    public PlantWatering supplyWater(ArduinoKit arduinoKit) {
        return plantWaterRepository.save(PlantWatering.builder().arduinoKit(arduinoKit)
                .wateringDate(LocalDateTime.now())
                .build());
    }

    @Override
    public List<PlantWatering> findFiveLatestDates(ArduinoKit arduinoKit) {
        return plantWaterRepository.findAllByArduinoKitOrderByWateringDateDesc(arduinoKit, PageRequest.of(0, 5));
    }
}
