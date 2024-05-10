package userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import userservice.dto.request.EmailAuthRequestDto;
import userservice.service.MailService;

@RequiredArgsConstructor
@RequestMapping("/email-auth")
@RestController
@Slf4j
public class EmailController {

    private final MailService mailService;
    @PostMapping("")
    public String sendEmail(@RequestBody EmailAuthRequestDto emailAuthRequestDto){
        log.info("[Email] 인증할 이메일 주소: " + emailAuthRequestDto.email());
        return mailService.sendEmail(emailAuthRequestDto.email());
    }

    @PostMapping("/check")
    public String AuthNumCheck(@RequestBody EmailAuthRequestDto EmailAuthRequestDto){
        log.info("[Email] 인증번호 검증");
        Boolean authCheck = mailService.checkAuth(EmailAuthRequestDto);
        if(authCheck)
            return "ok";
        else
            return "유효하지 않은 인증번호입니다.";
    }
}
