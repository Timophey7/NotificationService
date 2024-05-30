package com.notification.service.impl;

import com.notification.model.XlsxInfoResponse;
import com.sinch.xms.ApiConnection;
import com.sinch.xms.ApiException;
import com.sinch.xms.SinchSMSApi;
import com.sinch.xms.api.MtBatchTextSmsCreate;
import com.sinch.xms.api.MtBatchTextSmsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceImplTest {


    @Mock
    ApiConnection connection;

    @InjectMocks
    SmsServiceImpl smsService;

    XlsxInfoResponse xlsxInfoResponse;

    @BeforeEach
    void setUp() {
        xlsxInfoResponse = new XlsxInfoResponse();
        xlsxInfoResponse.setPhoneNum("+78005553535");
        xlsxInfoResponse.setMessage("test");
    }

    @Test
    void sendSms() throws InterruptedException, ApiException {

        ApiConnection conn = mock(ApiConnection.class);
        MtBatchTextSmsResult result = mock(MtBatchTextSmsResult.class);

        when(conn.createBatch(any(MtBatchTextSmsCreate.class))).thenReturn(result);

        smsService.sendSms(xlsxInfoResponse);

        verify(conn).createBatch(any(MtBatchTextSmsCreate.class));

    }

    @Test
    void formatPhoneNumber() {

        String phoneNumber = "+78005553535";

        String number = smsService.formatPhoneNumber(phoneNumber);

        assertEquals("+7-800-555-35-35", number);
    }
}