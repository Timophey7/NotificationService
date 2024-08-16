package com.parser.service.impl;

import com.parser.exceptions.FileIsEmptyException;
import com.parser.exceptions.TextIsEmptyException;
import com.parser.mappers.MapToXlsxInfoResponse;
import com.parser.model.XlsxInfo;
import com.parser.model.XlsxInfoResponse;
import com.parser.repository.XlsxInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParseInfoFromXlsServiceImplTest {

    @Mock
    XlsxInfoRepository xlsxInfoRepository;

    @Mock
    MapToXlsxInfoResponse mapper;

    @Mock
    KafkaTemplate<String, XlsxInfoResponse> kafkaTemplate;

    @InjectMocks
    ParseInfoFromXlsServiceImpl parseInfoFromXlsServiceImpl;

    @Test
    void parseXLS_Success() throws IOException, FileIsEmptyException {
        MultipartFile mockFile = createMockXlsxFile();

        parseInfoFromXlsServiceImpl.parseXLS(mockFile);

        verify(xlsxInfoRepository, times(1)).save(any(XlsxInfo.class));
    }

    @Test
    void parseXLS_FileIsEmptyException() throws FileIsEmptyException, IOException {
        MultipartFile mockFile = new MockMultipartFile("test.xlsx", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[0]);

        FileIsEmptyException fileIsEmptyException = assertThrows(FileIsEmptyException.class, () -> parseInfoFromXlsServiceImpl.parseXLS(mockFile));

        assertEquals(fileIsEmptyException.getMessage(),"вы не указали файл");
    }

    @Test
    void sendMessage_Success() throws TextIsEmptyException {
        XlsxInfoResponse xlsxInfoResponse = new XlsxInfoResponse();
        xlsxInfoResponse.setId(1);
        xlsxInfoResponse.setEmail("test@gmail.com");
        xlsxInfoResponse.setMessage("hello");
        XlsxInfo xlsxInfo = new XlsxInfo();
        xlsxInfo.setId(1);
        xlsxInfo.setEmail("test@gmail.com");;
        when(xlsxInfoRepository.findAll()).thenReturn(List.of(xlsxInfo));
        when(mapper.mapToXlsxInfoResponse(xlsxInfo,"hello")).thenReturn(xlsxInfoResponse);

        parseInfoFromXlsServiceImpl.sendMessage("hello");

        verify(xlsxInfoRepository,times(1)).findAll();
        verify(mapper,times(1)).mapToXlsxInfoResponse(xlsxInfo,"hello");
        verify(kafkaTemplate,times(1)).send("usersInfo",xlsxInfoResponse);
    }

    @Test
    void sendMessage_TextIsEmptyException() {
        String text = "";

        TextIsEmptyException textIsEmptyException = assertThrows(TextIsEmptyException.class, () -> {
            parseInfoFromXlsServiceImpl.sendMessage(text);
        });

        assertEquals(textIsEmptyException.getMessage(),"введите текс для отправки");

    }

    private MultipartFile createMockXlsxFile() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.xlsx");
        if (inputStream == null) {
            throw new RuntimeException("файл test.xlsx не найден.");
        }

        return new MockMultipartFile("test.xlsx", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);
    }
}