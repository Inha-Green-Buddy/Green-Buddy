package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.MailDto;
import com.keb.kebsmartfarm.entity.User;
import com.keb.kebsmartfarm.jwt.TokenProvider;
import com.keb.kebsmartfarm.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@AllArgsConstructor
public class SendMailService {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final UserRepository userRepository;

    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    private static final String FROM_ADDRESS = "cnxw4570123@gmail.com";


    public MailDto createMailAndChangePassword(String userEmail, String userId) {
        logger.info("메일 보내기");
        String randPw = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(userEmail);
        dto.setTitle(userId + "님의 KEBSmartFarm 임시비밀번호 안내 이메일입니다.");
        dto.setMessage("안녕하세요. KEBSmartFarm 임시비밀번호 안내 관련 이메일입니다.\n" +
                "[" + userId + "]님의 임시비밀번호는 " + randPw + "입니다.");
        updatePassword(randPw, userEmail);
        return dto;
    }

    public String getTempPassword() {
        char[] chars = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
                'J', 'K', 'L', 'M', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '=', '@', '$'
        };
        StringBuilder sb = new StringBuilder();

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (chars.length * Math.random());
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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(SendMailService.FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
        logger.info("메일 전송 완료");
    }
}
