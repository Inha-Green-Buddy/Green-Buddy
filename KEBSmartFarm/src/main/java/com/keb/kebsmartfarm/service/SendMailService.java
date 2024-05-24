package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.constant.Message;
import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.MailDto;
import com.keb.kebsmartfarm.entity.User;
import com.keb.kebsmartfarm.entity.VerificationCode;
import com.keb.kebsmartfarm.repository.UserRepository;
import com.keb.kebsmartfarm.repository.VerificationCodeRepository;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendMailService {
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final String FROM_ADDRESS;

    public SendMailService(
            UserRepository userRepository, VerificationCodeRepository verificationCodeRepository,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder, @Value("${spring.mail.username}") String FROM_ADDRESS) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.FROM_ADDRESS = FROM_ADDRESS;
    }

    public MailDto createVerificationMail(String userEmail) {
        String randCode = UUID.randomUUID().toString().substring(0, 6);
        VerificationCode verificationCode = new VerificationCode(userEmail, randCode);
        verificationCodeRepository.save(verificationCode);

        return MailDto.builder()
                .address(userEmail)
                .title(Message.VERIFICATION_EMAIL_TITLE)
                .message(Message.VERIFICATION_EMAIL_CONTENT.formatted(randCode))
                .build();
    }

    public void verifyEmail(String code, String userEmail) {
        VerificationCode verificationCode = verificationCodeRepository.findById(userEmail)
                .orElseThrow(() -> new RuntimeException(Error.INVALID_EMAIL));

        if (!verificationCode.matches(code)) {
            throw new RuntimeException(Error.INVALID_CODE);
        }
    }

    public MailDto createTempPasswordEmail(String userEmail, String userId) {
        String randPw = getTempPassword();
        updatePassword(randPw, userEmail);
        return MailDto.builder()
                .address(userEmail)
                .title(Message.TEMP_PASSWORD_EMAIL_TITLE.formatted(userId))
                .message(Message.TEMP_PASSWORD_EMAL_CONTENT.formatted(userEmail, randPw))
                .build();
    }

    public String getTempPassword() {
        char[] chars = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
                'J', 'K', 'L', 'M', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '=', '@', '$'
        };
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int) (chars.length * Math.random());
            sb.append(chars[idx]);
        }
        return sb.toString();
    }

    public void updatePassword(String tempPw, String userEmail) {
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new RuntimeException(String.format(Error.USER_DOES_NOT_MACTH, userEmail))
        );
        user.setUserPassword(passwordEncoder.encode(tempPw));
        userRepository.save(user);
    }

    @Async
    public void mailSend(MailDto mailDto) {
        log.info("메일 전송 시작");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
        log.info("메일 전송 완료");
    }
}
