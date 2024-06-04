package com.keb.kebsmartfarm.repository;

import com.keb.kebsmartfarm.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}
