package com.keb.kebsmartfarm.dto;

import com.keb.kebsmartfarm.entity.Authority;
import com.keb.kebsmartfarm.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private String userId;
    private String userPassword;
    private String userNickname;
    private String userName;
    private String userEmail;
    private String userPhoneNum;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .userId(userId)
                .userPassword(passwordEncoder.encode(userPassword))
                .userName(userName)
                .userNickname(userNickname)
                .userEmail(userEmail)
                .userPhoneNum(userPhoneNum)
                .authority(Authority.ROLE_USER)
                .userRegDate(LocalDateTime.now())
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userId, userPassword);
    }
}
