package com.notification.service.impl;

import com.notification.model.XlsxInfoResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    MimeMessageHelper helper;

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    EmailServiceImpl emailService;

    XlsxInfoResponse xlsxInfoResponse;

    @BeforeEach
    void setUp() {
        xlsxInfoResponse = new XlsxInfoResponse();
        xlsxInfoResponse.setEmail("test@test.com");
        xlsxInfoResponse.setMessage("test message");
    }

    @Test
    public void testSendEmailShouldSendEmail() throws MessagingException {

        emailService.sendEmail(xlsxInfoResponse);

        verify(helper).setFrom("timopheyonisenko@gmail.com");
        verify(helper).setTo(xlsxInfoResponse.getEmail());
        verify(helper).setText(xlsxInfoResponse.getMessage());
        verify(helper).setSubject("email from friend");
        verify(mailSender).send(helper.getMimeMessage());

    }


}