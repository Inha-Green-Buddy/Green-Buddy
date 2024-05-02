package com.keb.kebsmartfarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReleasedKit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ReleaseNum;

    @Column(columnDefinition = "text", unique = true)
    private String kitSerialNum;

    @OneToOne(mappedBy = "releasedKit", cascade = CascadeType.ALL)
    private ArduinoKit arduinoKit;

    public void setArduinoKit(ArduinoKit arduinoKit) {
        this.arduinoKit = arduinoKit;
    }
}
