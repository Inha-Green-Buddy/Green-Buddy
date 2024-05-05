package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.config.SecurityUtil;
import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.UserResponseDto;
import com.keb.kebsmartfarm.entity.User;
import com.keb.kebsmartfarm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getMyInfoBySecurity() {
        return userRepository.findById(SecurityUtil.getCurrentUserId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException(Error.NO_LOGIN_USER_INFORMATION));
    }

    @Transactional
    public UserResponseDto changeUserNickname(String userId, String nickname) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException(Error.NO_LOGIN_USER_INFORMATION));
        user.setUserNickname(nickname);
        return UserResponseDto.of(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto changeUserPassword(String exPassword, String newPassword) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new RuntimeException(Error.NO_LOGIN_USER_INFORMATION));
        if (!passwordEncoder.matches(exPassword, user.getUserPassword())) {
            throw new RuntimeException(Error.PASSWORD_DOES_NOT_MATCH);
        }
        user.setUserPassword(passwordEncoder.encode(newPassword));
        return UserResponseDto.of(userRepository.save(user));
    }
}
