package com.keb.kebsmartfarm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantNum;
    private String plantName;
    @Column(unique = true)
    private String plantNickName;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime plantRegDate;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime plantHarvestDate;
    @Enumerated(EnumType.STRING)
    private PlantStatus status;

    @ManyToOne
    @JoinColumn(name = "plantKitNo")
    @JsonIgnore
    private ArduinoKit arduinoKit;

    @JsonIgnore
    @NotEmpty
    private String orgFileName;

    @JsonIgnore
    @NotEmpty
    private String storedFilePath;

    public void endGrowth() {
        status = PlantStatus.GROWN;
        plantHarvestDate = LocalDateTime.now();
    }
}