package com.keb.kebsmartfarm.repository;

import com.keb.kebsmartfarm.entity.PlantPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantPictureRepository extends JpaRepository<PlantPicture, Long> {
    List<PlantPicture> findAllByPlantNumOrderByDateDesc(Long plantNum);
}
