package com.parser.mappers;

import com.parser.model.XlsxInfo;
import com.parser.model.XlsxInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MapToXlsxInfoResponseTest {

    @InjectMocks
    private MapToXlsxInfoResponse mapToXlsxInfoResponse;

    XlsxInfo xlsxInfo;
    XlsxInfoResponse xlsxInfoResponse;

    @BeforeEach
    void setUp() {
        xlsxInfo = new XlsxInfo();
        xlsxInfo.setId(1);
        xlsxInfo.setEmail("test@test.com");
        xlsxInfo.setFio("OON");
        xlsxInfoResponse = new XlsxInfoResponse();
        xlsxInfoResponse.setId(1);
        xlsxInfoResponse.setEmail("test@test.com");
        xlsxInfoResponse.setFio("OON");
        xlsxInfoResponse.setMessage("test");
    }

    @Test
    void mapToXlsxInfoResponse() {
        String message = "test";

        mapToXlsxInfoResponse.mapToXlsxInfoResponse(xlsxInfo, message);

        assertEquals(message, xlsxInfoResponse.getMessage());
        assertEquals(xlsxInfo.getEmail(), xlsxInfoResponse.getEmail());
        assertEquals(xlsxInfo.getFio(), xlsxInfoResponse.getFio());


    }
}