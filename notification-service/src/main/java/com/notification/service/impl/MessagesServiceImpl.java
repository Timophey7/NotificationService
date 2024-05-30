package com.notification.service.impl;

import com.notification.model.XlsxInfoResponse;
import com.notification.service.EmailService;
import com.notification.service.MessagesService;
import com.notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagesServiceImpl implements MessagesService {

    private final EmailService emailService;
    private final SmsService smsService;

    @Override
    public void sendMessages(XlsxInfoResponse userInfo) {
        emailService.sendEmail(userInfo);
        smsService.sendSms(userInfo);
    }
}
