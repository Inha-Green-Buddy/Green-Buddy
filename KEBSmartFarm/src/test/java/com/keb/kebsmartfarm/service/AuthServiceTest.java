package com.keb.kebsmartfarm.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.UserRequestDto;
import com.keb.kebsmartfarm.dto.UserResponseDto;
import com.keb.kebsmartfarm.entity.User;
import com.keb.kebsmartfarm.jwt.TokenProvider;
import com.keb.kebsmartfarm.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("local")
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {AuthService.class})
@MockBeans({@MockBean(TokenProvider.class), @MockBean(AuthenticationManagerBuilder.class)})
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    @Autowired
    AuthService authService;

    @Test
    void 중복_회원이_없으면() {
        //given
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .userEmail("qqq@naver.com")
                .userId("test")
                .userName("테스트")
                .userPassword("test01")
                .build();
        User user = userRequestDto.toUser(passwordEncoder);

        // 중복 ID가 없음
        when(userRepository.existsByUserId(any(String.class))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn("TestEncoded");
        //when
        UserResponseDto savedUser = authService.signup(userRequestDto);

        // then
        assertThat(userRequestDto.getUserId()).isEqualTo(savedUser.getUserId());
    }

    @Test
    void 중복_회원이_있을_경우() {
        //given
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .userEmail("qqq@naver.com")
                .userId("test")
                .userName("테스트")
                .userPassword("test01")
                .build();
        User user = userRequestDto.toUser(passwordEncoder);
        // when
        // 중복 아이디가 있다
        when(userRepository.existsByUserId(any(String.class))).thenReturn(true);

        // then
        assertThatThrownBy(() -> authService.signup(userRequestDto))
                .hasMessage(Error.ALREADY_EXIST_ID)
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 비밀번호_찾기() {
        //given
        String userId = "test", userEmail = "test@test.com";
        User user1 = User.builder().userId("text").build();
        User correct = User.builder().userId("test").build();

        // when
        // 차례대로 해당 이메일 없는 경우와 이메일로 찾는 경우, 일치하는 유저
        when(userRepository.findByUserEmail(userEmail)).thenReturn(Optional.empty(), Optional.of(user1),
                Optional.of(correct));

        //then
        assertAll(
                // 해당 이메일로 조회가 불가능한 경우
                () -> assertThatThrownBy(() -> authService.findPasswordByIdAndEmail(userEmail, userId))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining(String.format(Error.USER_DOES_NOT_MACTH, userEmail)),
                // 이메일로 조회는 가능하나 아이디가 일치하지 않는 경우
                () -> assertThatThrownBy(() -> authService.findPasswordByIdAndEmail(userEmail, userId))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining(String.format(Error.USER_DOES_NOT_MACTH, userId)),
                // 정상적으로 찾음
                () -> assertDoesNotThrow(() -> authService.findPasswordByIdAndEmail(userEmail, userId))
        );

    }

    @Test
    void 아이디_찾기() {
        // given
        String userEmail = "test@test.com", userName = "테스트", userId = "test";
        User correct = User.builder().userName(userName).userId(userId).build();

        // when
        when(userRepository.findByUserEmail(any())).
                thenReturn(Optional.empty(), Optional.of(correct));

        // then
        assertAll(
                () -> assertThatThrownBy(() -> authService.findIdByNameAndEmail(userEmail, userName))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining(String.format(Error.USER_DOES_NOT_MACTH, userEmail)),
                () -> assertEquals(authService.findIdByNameAndEmail(userEmail, userName), Collections.singletonMap("userId", userId)));
    }

    @Test
    void 아이디_중복_O() {
        // given
        String userId = "test";

        // when
        when(userRepository.existsByUserId(userId)).thenReturn(true);

        //then
        assertThatThrownBy(() -> authService.validateDuplicateUserId(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(Error.ALREADY_EXIST_ID);
    }

    @Test
    void 아이디_중복_X() {
        // given
        String userId = "notDuplicated";
        when(userRepository.existsByUserId(userId)).thenReturn(false);

        // then
        assertDoesNotThrow(() -> authService.validateDuplicateUserId(userId));
    }

    @Test
    void 이메일_중복_O(){
        // given
        String userEmail = "test@test.com";
        when(userRepository.existsByUserEmail(userEmail)).thenReturn(true);

        // then
        assertThatThrownBy(() -> authService.validateDuplicateUserEmail(userEmail))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(Error.ALREADY_EXIST_EMAIL);
    }
    @Test
    void 이메일_중복_X() {
        // given
        String userEmail = "test01@test.com";
        when(userRepository.existsByUserEmail(userEmail)).thenReturn(false);

        // then
        assertDoesNotThrow(() -> authService.validateDuplicateUserEmail(userEmail));
    }
}