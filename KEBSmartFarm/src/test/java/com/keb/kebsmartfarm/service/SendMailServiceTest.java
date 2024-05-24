package com.keb.kebsmartfarm.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.MailDto;
import com.keb.kebsmartfarm.entity.User;
import com.keb.kebsmartfarm.entity.VerificationCode;
import com.keb.kebsmartfarm.repository.UserRepository;
import com.keb.kebsmartfarm.repository.VerificationCodeRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith({MockitoExtension.class})
@MockBean(JavaMailSender.class)
class SendMailServiceTest {
    @InjectMocks
    private SendMailService sendMailService;
    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @Test
    void 인증_코드_비정상_확인() {
        // given
        String userEmail = "test@test.com", code = "asdf";
        given(verificationCodeRepository.findById(userEmail)).willReturn(
                Optional.of(VerificationCode.fromUserEmail(userEmail)), // 코드가 일치하지 않음
                Optional.empty() // 해당 이메일을 찾을 수 없음
        );

        //then
        assertAll(
                () -> assertThatThrownBy(() -> sendMailService.verifyEmail(code, userEmail))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining(Error.INVALID_CODE),
                () -> assertThatThrownBy(() -> sendMailService.verifyEmail(code, userEmail))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining(Error.INVALID_EMAIL)
        );

    }

    @Test
    void 인증_코드_정상_확인() {
        // given
        UUID randUUID = UUID.randomUUID();
        String userEmail = "test@test.com", code = randUUID.toString().substring(0, 6);
        try (MockedStatic<UUID> uuid = mockStatic(UUID.class)) {
            given(UUID.randomUUID()).willReturn(randUUID);
            VerificationCode mockCode = VerificationCode.fromUserEmail(userEmail);
            given(verificationCodeRepository.findById(userEmail)).willReturn(
                    Optional.of(mockCode));

            // then
            assertDoesNotThrow(() -> sendMailService.verifyEmail(code, userEmail));
        }
    }

    @Test
    void 메일_DTO_생성_확인() {
        UUID randomUUID = UUID.randomUUID(); // 최초 설정

        try (MockedStatic<UUID> uuid = mockStatic(UUID.class)) {
            // given
            String address = "test@test.com";
            given(UUID.randomUUID()).willReturn(randomUUID);
            VerificationCode mockCode = VerificationCode.fromUserEmail(address);
            given(verificationCodeRepository.save(any(VerificationCode.class)))
                    .willReturn(mockCode);

            // when
            MailDto verificationMail = sendMailService.createVerificationMail(address);

            // then
            assertThat(verificationMail.getAddress()).isEqualTo(address);
        }
    }

    @Test
    void 비밀번호_찾기_이메일_X() {
        // given
        String temp = "123456", userEmail = "test@test.com";
        given(userRepository.findByUserEmail(any(String.class))).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> sendMailService.updatePassword(temp, userEmail))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(Error.USER_DOES_NOT_MACTH.formatted(userEmail));
    }

    @Test
    void 비밀번호_찾기_이메일_O() {
        // given
        given(passwordEncoder.encode(any(String.class))).willReturn("{bcrypt}$2a$qq");
        String temp = "123456", userEmail = "test@test.com";
        User user = User.builder()
                .userPassword(passwordEncoder.encode(temp))
                .build();
        given(userRepository.findByUserEmail(any(String.class))).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);

        // then
        assertDoesNotThrow(() -> sendMailService.updatePassword(temp, userEmail));
    }

}