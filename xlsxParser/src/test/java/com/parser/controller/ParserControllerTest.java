package com.parser.controller;

import com.parser.exceptions.TextIsEmptyException;
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
import org.springframework.mock.web.MockHttpSession;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        MockHttpSession mockSession = new MockHttpSession();
        String message = "Вы успешно отправили письма!";
        mockSession.setAttribute("message", message);

        ResultActions perform = mockMvc.perform(get("/v1/xlsx/parse/home")
                        .session(mockSession)
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());

        perform.andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("home"));

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
                .andExpect(status().isOk())
                .andExpect(view().name("upload-file"));
    }

    @Test
    void uploadFile_error() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[]{});

        Exception exception = new RuntimeException("Ошибка при обработке файла");
        doThrow(exception).when(parseInfoFromXlsService).parseXLS(any());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/xlsx/parse//uploadFile")
                        .file(file)
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.model().attribute("fileError", exception.getMessage()));

        verify(parseInfoFromXlsService).parseXLS(any());
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

    @Test
    void sendMessages_TextIsEmptyException() throws Exception {
        TextIsEmptyException textIsEmptyException = new TextIsEmptyException("текст пустой");
        doThrow(textIsEmptyException).when(parseInfoFromXlsService).sendMessage("");

        ResultActions perform = mockMvc.perform(post("/v1/xlsx/parse/send")
                .param("text", ""));

        perform.andExpect(view().name("upload-file"))
                .andExpect(model().attribute("textError","текст пустой"));


    }




}