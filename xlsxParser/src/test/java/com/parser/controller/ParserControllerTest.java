package com.parser.controller;

import com.parser.mappers.MapToXlsxInfoResponse;
import com.parser.model.XlsxInfo;
import com.parser.model.XlsxInfoResponse;
import com.parser.repository.XlsxInfoRepository;
import com.parser.service.ParseInfoFromXlsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { ParserController.class })
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ParserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ParseInfoFromXlsService parseInfoFromXlsService;
    @MockBean
    XlsxInfoRepository xlsxInfoRepository;
    @MockBean
    MapToXlsxInfoResponse mapToXlsxInfoResponse;
    @MockBean
    KafkaTemplate<String, XlsxInfoResponse> kafkaTemplate;

    XlsxInfo xlsxInfo;
    XlsxInfoResponse xlsxInfoResponse;

    @BeforeEach
    public void setUp() {
        xlsxInfo = new XlsxInfo();
        xlsxInfo.setId(1);
        xlsxInfo.setPhoneNum("+78005553535");
        xlsxInfo.setEmail("test@test.com");
        xlsxInfoResponse = new XlsxInfoResponse();
        xlsxInfoResponse.setId(1);
        xlsxInfoResponse.setPhoneNum("+78005553535");
        xlsxInfoResponse.setEmail("test@test.com");
        xlsxInfoResponse.setMessage("test");
    }

    @Test
    void homeShouldReturnStatusIsOk() throws Exception {

        ResultActions perform = mockMvc.perform(get("/v1/xlsx/parse/home"));

        perform.andExpect(status().isOk())
                .andExpect(model().attributeExists("message"));

    }

    @Test
    void uploadFileShouldReturnStatusIsOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.xlsx",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "Excel".getBytes()
        );

        mockMvc.perform(multipart("/v1/xlsx/parse/uploadFile").file(file))
                .andExpect(status().isOk());
    }

    @Test
    void uploadFileShouldReturnError() throws Exception {


        mockMvc.perform(multipart("/v1/xlsx/parse/uploadFile"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void sendMessagesShouldReturnRedirectStatus() throws Exception {
        List<XlsxInfo> xlsxInfoList = List.of(xlsxInfo);
        when(xlsxInfoRepository.findAll()).thenReturn(xlsxInfoList);
        when(mapToXlsxInfoResponse.mapToXlsxInfoResponse(xlsxInfo,"test")).thenReturn(xlsxInfoResponse);

        ResultActions perform = mockMvc.perform(post("/v1/xlsx/parse/send")
                .param("text", "test"));

        perform.andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("message","Вы успешно отправили письма!"));


    }


}