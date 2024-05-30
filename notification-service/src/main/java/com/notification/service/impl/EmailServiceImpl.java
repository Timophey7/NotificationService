package com.notification.service.impl;

import com.notification.model.XlsxInfoResponse;
import com.notification.service.EmailService;
import io.micrometer.core.annotation.Timed;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final MimeMessageHelper helper;

    @Timed("sendEmailMethod")
    @Override
    public void sendEmail(XlsxInfoResponse xlsxInfoResponse) {
        try{
            helper.setFrom("your email address");
            helper.setTo(xlsxInfoResponse.getEmail());
            helper.setText(xlsxInfoResponse.getMessage());
            helper.setSubject("email from friend");
            javaMailSender.send(helper.getMimeMessage());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
