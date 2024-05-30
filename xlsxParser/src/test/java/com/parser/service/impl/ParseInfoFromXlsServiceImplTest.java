package com.parser.service.impl;

import com.parser.model.XlsxInfo;
import com.parser.repository.XlsxInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParseInfoFromXlsServiceImplTest {

    @Mock
    XlsxInfoRepository xlsxInfoRepository;

    @InjectMocks
    ParseInfoFromXlsServiceImpl parseInfoFromXlsServiceImpl;

    @Test
    void parseXLS() throws IOException {
        MultipartFile mockFile = createMockXlsxFile();

        parseInfoFromXlsServiceImpl.parseXLS(mockFile);

        verify(xlsxInfoRepository, times(1)).save(any(XlsxInfo.class));
    }

    private MultipartFile createMockXlsxFile() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.xlsx");
        if (inputStream == null) {
            throw new RuntimeException("файл test.xlsx не найден.");
        }

        return new MockMultipartFile("test.xlsx", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);
    }
}