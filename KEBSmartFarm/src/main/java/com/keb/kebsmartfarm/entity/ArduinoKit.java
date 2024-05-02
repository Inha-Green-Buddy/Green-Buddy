package com.keb.kebsmartfarm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @OneToMany(mappedBy = "arduinoKit", cascade = CascadeType.ALL)
    private List<Plant> PlantList;

    @OneToMany(mappedBy = "kit", cascade = CascadeType.ALL)
    private List<SensorData> sensorDataList;
    @Transient
    // activePlant
    private Plant plant;

    /**
     * 키트에 현재 키우고 있는 식물을 반환하는 함수
     * 반환값 사용 시 식물 등록
     * 성장완료 메소드의 경우 -> ifPresent로 오류 처리
     * 삭제 메소드에서는 -> orElseThrow로 오류 처리
     *
     * @return Optional<plant> 현재 키우고 있는 식물
     */
    public Optional<Plant> getActivePlant() {
        return PlantList.stream()
                .filter(candidate -> candidate.getPreviousPlant() == null)
                .findFirst();
    }

    public void setReleasedKit(ReleasedKit releasedKit) {
        this.releasedKit = releasedKit;
    }
}
