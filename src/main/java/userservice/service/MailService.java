package userservice.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import userservice.dto.request.EmailAuthRequestDto;

import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MailService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender javaMailSender;
    private final MeterRegistry meterRegistry;

    private Counter mailSendCounter;

    @PostConstruct
    public void init() {
        this.mailSendCounter = meterRegistry.counter("mail_send_count");
    }

    private static int authNumber;

    // 랜덤으로 숫자 생성
    public static void createNumber() {
        authNumber = (int) (Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public void sendEmail(String email) {
        createNumber();

        String setFrom = "vodacompanies@gmail.com";
        String title = "[VODA]이메일 인증 코드";
        String content =
                "듣는 블로그 VODA입니다." +    //html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "감사합니다."; //이메일 내용 삽입
        mailSend(setFrom, email, title, content);
        mailSendCounter.increment(); // 이메일 전송 시 메트릭 증가
    }

    private void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            javaMailSender.send(message);
        } catch (javax.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(String.valueOf(authNumber), toMail, 60 * 5L, TimeUnit.SECONDS);
    }

    public Boolean checkAuth(EmailAuthRequestDto emailAuthRequestDto) {
        log.info(emailAuthRequestDto.authNumber());
        String storedEmail = redisTemplate.opsForValue().get(emailAuthRequestDto.authNumber());

        if (storedEmail == null) {
            return false;
        } else if (storedEmail.equals(emailAuthRequestDto.email())) {
            // 값이 맞으면 Redis에서 값을 삭제
            redisTemplate.delete(emailAuthRequestDto.authNumber());
            return true;
        } else {
            return false;
        }
    }
}
