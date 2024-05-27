package com.keb.kebsmartfarm.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Plants {
    @OneToMany(mappedBy = "arduinoKit", fetch = FetchType.LAZY)
    private List<Plant> plants = new ArrayList<>();

    public Optional<Plant> getActivePlant() {
        return plants.stream()
                .filter(plant -> plant.getStatus() == PlantStatus.GROWING)
                .findFirst();

    }
}
