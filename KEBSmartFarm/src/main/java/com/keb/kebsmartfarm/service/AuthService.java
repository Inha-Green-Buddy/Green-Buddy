package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.TokenDto;
import com.keb.kebsmartfarm.dto.UserRequestDto;
import com.keb.kebsmartfarm.dto.UserResponseDto;
import com.keb.kebsmartfarm.entity.User;
import com.keb.kebsmartfarm.jwt.TokenProvider;
import com.keb.kebsmartfarm.repository.RefreshTokenRepository;
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
        validateDuplicateUserId(requestDto.getUserId());
        validateDuplicateUserEmail(requestDto.getUserEmail());
        User user = requestDto.toUser(passwordEncoder);
        return UserResponseDto.of(userRepository.save(user));
    }

    public void validateDuplicateUserId(String userId) {
        if (userRepository.existsByUserId(userId)) {
            throw new RuntimeException(Error.ALREADY_EXIST_ID);
        }
    }

    public void validateDuplicateUserEmail(String userEmail){
        if (userRepository.existsByUserEmail(userEmail)) {
            throw new RuntimeException(Error.ALREADY_EXIST_EMAIL);
        }
    }


    public TokenDto login(UserRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authentication);
    }

    public void findPasswordByIdAndEmail(String userEmail, String userId) {
        User user = getUserByEmail(userEmail);
        if(!user.getUserId().equalsIgnoreCase(userId)){
            throw  new RuntimeException(String.format(Error.USER_DOES_NOT_MACTH, userId));
        }
    }

    private User getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException(String.format(Error.USER_DOES_NOT_MACTH, userEmail)));
    }

    public Map<String, String> findIdByNameAndEmail(String userEmail, String userName) {
        Map<String, String> ret = new HashMap<>();
        User user = getUserByEmail(userEmail);
        if(user.getUserName().equalsIgnoreCase(userName)){
            ret.put("userId", user.getUserId());
        }
        return ret;
    }
}
