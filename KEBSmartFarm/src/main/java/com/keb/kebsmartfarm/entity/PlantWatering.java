package com.keb.kebsmartfarm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlantWatering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long wateringNo;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime wateringDate;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kitNo", referencedColumnName = "kitNo")
    private ArduinoKit arduinoKit;
}
