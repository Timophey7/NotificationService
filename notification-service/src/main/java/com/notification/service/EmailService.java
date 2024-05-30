package com.notification.service;

import com.notification.model.XlsxInfoResponse;

public interface EmailService {

    void sendEmail(XlsxInfoResponse xlsxInfoResponse);

}
