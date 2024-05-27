package com.keb.kebsmartfarm.entity;

import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.PlantResponseDto;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Plants {
    @OneToMany(mappedBy = "arduinoKit", fetch = FetchType.LAZY)
    private List<Plant> plants = new ArrayList<>();

    public Plant getGrowingPlant() {
        return findGrowingPlant().orElseThrow(() -> new IllegalStateException(Error.PLANT_NOT_REGISTERED));
    }

    private Optional<Plant> findGrowingPlant() {
        return plants.stream()
                .filter(plant -> plant.getStatus() == PlantStatus.GROWING)
                .findFirst();
    }

    public Map<PlantStatus, List<PlantResponseDto>> separateByStatus() {
        return plants.stream()
                .map(PlantResponseDto::of)
                .collect(Collectors.groupingBy(PlantResponseDto::getStatus));
    }

    public void removePlant(Plant plant) {
        if(!plants.contains(plant)){
            throw new IllegalStateException(Error.PLANT_NOT_EXIST);
        }
        plants.remove(plant);
    }

    public boolean hasGrowingPlant() {
        return findGrowingPlant().isPresent();
    }
}
