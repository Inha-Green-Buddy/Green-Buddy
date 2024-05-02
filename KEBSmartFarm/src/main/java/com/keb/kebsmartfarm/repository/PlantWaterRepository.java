package com.keb.kebsmartfarm.repository;

import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.PlantWatering;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantWaterRepository extends JpaRepository<PlantWatering, Long> {

    public List<PlantWatering> findAllByArduinoKitOrderByWateringDateDesc(ArduinoKit kitNo, PageRequest pageRequest);
}
