package com.notification.service.impl;

import com.notification.model.XlsxInfoResponse;
import com.notification.service.SmsService;
import com.sinch.xms.ApiConnection;
import com.sinch.xms.SinchSMSApi;
import com.sinch.xms.api.MtBatchTextSmsCreate;
import com.sinch.xms.api.MtBatchTextSmsResult;
import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private static final String SERVICE_PLAN_ID = "your service plan id";
    private static final String TOKEN = "your token";

    @Timed("sendSmsMethod")
    @Override
    public void sendSms(XlsxInfoResponse xlsxInfoResponse) {
        String SENDER = "your phone number";
        String phoneNumber = formatPhoneNumber(xlsxInfoResponse.getPhoneNum());
        String[] RECIPIENTS = { phoneNumber };

        ApiConnection conn = ApiConnection
                .builder()
                .servicePlanId(SERVICE_PLAN_ID)
                .token(TOKEN)
                .start();
        MtBatchTextSmsCreate message = SinchSMSApi
                .batchTextSms()
                .sender(SENDER)
                .addRecipient(RECIPIENTS)
                .body(xlsxInfoResponse.getMessage())
                .build();
        try {
            MtBatchTextSmsResult batch = conn.createBatch(message);
            System.out.println(batch.id());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("you sent:" + message.body());
    }

    public String formatPhoneNumber(String phoneNum){
        String cleanedNumber = phoneNum.replaceAll("[^0-9]", "");

        if (cleanedNumber.length() != 11) {
            throw new IllegalArgumentException("Неверный формат номера телефона");
        }

        StringBuilder formattedNumber = new StringBuilder("+7-");
        formattedNumber.append(cleanedNumber, 1, 4).append("-");
        formattedNumber.append(cleanedNumber, 4, 7).append("-");
        formattedNumber.append(cleanedNumber, 7, 9).append("-");
        formattedNumber.append(cleanedNumber, 9, 11);

        return formattedNumber.toString();
    }
}
