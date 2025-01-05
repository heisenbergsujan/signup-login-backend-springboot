package dev.sujan.signup_login_backend.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String emailFrom;


    @Async
    public void sendActivationEmail(
            String to,
            String username,
            String emailTemplateName,
            String confirmUrl,
            String activationCode,
            String subject
    ) throws MessagingException {

        MimeMessage message= mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        helper.setFrom(emailFrom);
        helper.setTo(to);
        helper.setSubject(subject);

        Map<String,Object> tempProperties = new HashMap<>();
        tempProperties.put("username",username);
        tempProperties.put("confirmation_url",confirmUrl);
        tempProperties.put("activation_code",activationCode);

        Context context=new Context();
        context.setVariables(tempProperties);

       String template= templateEngine.process(emailTemplateName,context);
       helper.setText(template,true);
       mailSender.send(message);
    }
}
