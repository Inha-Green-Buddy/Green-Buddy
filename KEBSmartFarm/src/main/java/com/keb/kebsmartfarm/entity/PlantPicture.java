package com.keb.kebsmartfarm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlantPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plantNum", referencedColumnName = "plantNum", insertable = false, updatable = false)
    private Plant plant;

    @Column(name = "plantNum")
    private Long plantNum;

    @ManyToOne
    @JoinColumn(name = "userSeqNum", referencedColumnName = "userSeqNum", insertable = false, updatable = false)
    private User user;

    @Column(name = "userSeqNum")
    private Long seqNum;

    @NotEmpty
    private String orgFileName;

    @NotEmpty
    private String storedFilePath;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    private String msg;
}
