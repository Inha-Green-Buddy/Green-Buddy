package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.PlantWatering;

import java.util.List;

public interface PlantWateringService {

    public PlantWatering supplyWater(ArduinoKit arduinoKit);

    public List<PlantWatering> findFiveLatestDates(ArduinoKit arduinoKit);
}
