//package userservice.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.stereotype.Component;
//
//import java.util.Properties;
//
//@Configuration
//public class EmailConfig {
//
//    @Bean
//    public JavaMailSender mailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//        mailSender.setUsername("BOOT@gmail.com");
//        mailSender.setPassword("jenk kcsm pasr mdos");
//
//        Properties properties = new Properties();
//        properties.put("mail.transport.protocol", "smtp");//프로토콜로 smtp 사용
//        properties.put("mail.smtp.auth", "true");//smtp 서버에 인증이 필요
//        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//SSL 소켓 팩토리 클래스 사용
//        properties.put("mail.smtp.starttls.enable", "true");//STARTTLS(TLS를 시작하는 명령)를 사용하여 암호화된 통신을 활성화
//        properties.put("mail.debug", "true");//디버깅 정보 출력
//        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");//smtp 서버의 ssl 인증서를 신뢰
//        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");//사용할 ssl 프로토콜 버젼
//
//        mailSender.setJavaMailProperties(properties);//mailSender에 우리가 만든 properties 넣고
//
//        return mailSender;//빈으로 등록한다.
//    }
//}
