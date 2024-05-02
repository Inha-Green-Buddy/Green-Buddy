package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.dto.TokenDto;
import com.keb.kebsmartfarm.dto.UserRequestDto;
import com.keb.kebsmartfarm.dto.UserResponseDto;
import com.keb.kebsmartfarm.entity.User;
import com.keb.kebsmartfarm.jwt.TokenProvider;
import com.keb.kebsmartfarm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserResponseDto signup(UserRequestDto requestDto) {
        if (userRepository.existsByUserId(requestDto.getUserId())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        User user = requestDto.toUser(passwordEncoder);
        return UserResponseDto.of(userRepository.save(user));
    }

    public TokenDto login(UserRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authentication);
    }

    public void findPasswordByIdAndEmail(String userEmail, String userId) {
        User user = this.userRepository.findByUserEmail(userEmail).orElseThrow(()-> new RuntimeException(userEmail + "에 해당하는 회원이 없습니다"));
        if(!user.getUserId().equalsIgnoreCase(userId)){
            throw  new RuntimeException(userId + "에 맞는 회원이 없습니다.");
        }
    }

    public Map<String, String> findIdByNameAndEmail(String userEmail, String userName) {
        Map<String, String> ret = new HashMap<>();
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new RuntimeException(userEmail + "에 해당하는 회원이 없습니다."));
        if(user.getUserName().equalsIgnoreCase(userName)){
            ret.put("userId", user.getUserId());
        }
        return ret;
    }
}
