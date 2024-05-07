package com.keb.kebsmartfarm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeqNum;

    @Column(length = 3)
    private String userName;

    @Column(length = 10, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPassword;
    @Column(length = 13)
    private String userPhoneNum;

    @Column(nullable = false, unique = true)
    private String userEmail;

    private String userNickname;

    private LocalDateTime userRegDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ArduinoKit> arduinoKitList;

    @Enumerated(EnumType.STRING)
    private Authority authority;
    // userPassword와 userNickname은 변경 가능
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
