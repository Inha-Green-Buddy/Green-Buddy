package com.keb.kebsmartfarm.Controller;

import com.keb.kebsmartfarm.constant.Message;
import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.MailDto;
import com.keb.kebsmartfarm.dto.TokenDto;
import com.keb.kebsmartfarm.dto.UserRequestDto;
import com.keb.kebsmartfarm.dto.UserResponseDto;
import com.keb.kebsmartfarm.jwt.TokenProvider;
import com.keb.kebsmartfarm.service.AuthService;
import com.keb.kebsmartfarm.service.SendMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final SendMailService sendMailService;

    @PostMapping("/validateId")
    public ResponseEntity<String> validateId(@RequestBody Map<String, String> userId) {
        authService.validateDuplicateUserId(userId.get("userId"));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/validateEmail")
    public ResponseEntity<String> validateEmail(@RequestBody Map<String, String> userEmail) {
        authService.validateDuplicateUserEmail(userEmail.get("userEmail"));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(authService.signup(userRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(authService.login(userRequestDto));
    }

    @PostMapping("/findId")
    public ResponseEntity<Map<String , String>> findUserId(@RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(authService.findIdByNameAndEmail(requestDto.getUserEmail(), requestDto.getUserName()));
    }

    @PostMapping("/findPw")
    public ResponseEntity<String> findUserPassword(@RequestBody UserRequestDto request) {
        long befTime = System.currentTimeMillis(), aftTime;
        try{
            authService.findPasswordByIdAndEmail(request.getUserEmail(), request.getUserId());
            MailDto mailDto = sendMailService.createMailAndChangePassword(request.getUserEmail(), request.getUserId());
            sendMailService.mailSend(mailDto);
        }catch (Exception e){
            aftTime = System.currentTimeMillis();
            log.info("걸린 시간 : {}", aftTime - befTime);
            return ResponseEntity.ok(Error.ID_OR_PASSWORD_DOES_NOT_MATCH);
        }
        aftTime = System.currentTimeMillis();
        log.info("걸린 시간 : {}", aftTime - befTime);
        return ResponseEntity.ok(Message.SENT_EMAIL_TO_USER);
    }
}