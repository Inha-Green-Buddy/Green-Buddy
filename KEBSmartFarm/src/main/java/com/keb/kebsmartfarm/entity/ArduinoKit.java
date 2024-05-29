package com.keb.kebsmartfarm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keb.kebsmartfarm.dto.PlantResponseDto;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArduinoKit {
    @Id
    private Long kitNo;

    @OneToOne
    @MapsId // kitNo와 관계된 엔티티의 기본키를 매핑
    @JoinColumn(name = "kitNo")
    private ReleasedKit releasedKit;


    private String deviceName;
    @Column(unique = true)
    private String serialNum;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userSeqNum", referencedColumnName = "userSeqNum", insertable = false, updatable = false)
    private User user;

    @Column(name = "userSeqNum")
    private Long userSeqNum;

    @JsonIgnore
    @Default
    @Embedded
    private Plants plants = new Plants();

    @OneToMany(mappedBy = "kit", cascade = CascadeType.ALL)
    private List<SensorData> sensorDataList = new ArrayList<>();

    public Plant getActivePlant() {
        return plants.getGrowingPlant();
    }

    public Map<PlantStatus, List<PlantResponseDto>> getPlantsByStatus() {
        return plants.separateByStatus();
    }

    public boolean hasPlant() {
        return plants.hasGrowingPlant();
    }

    public void deletePlant(Plant plant) {
        plants.removePlant(plant);
    }

    public void setReleasedKit(ReleasedKit releasedKit) {
        this.releasedKit = releasedKit;
    }
}
