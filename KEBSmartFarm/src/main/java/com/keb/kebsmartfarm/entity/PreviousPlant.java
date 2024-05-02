package com.keb.kebsmartfarm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreviousPlant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RegNo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userSeqNum", referencedColumnName = "userSeqNum", insertable = false, updatable = false)
    private User user;

    @Column(name = "userSeqNum")
    private Long userSeqNum;

    @OneToOne
    @JoinColumn(name = "plant")
    private Plant plant;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime plantHarvestDate;
}
