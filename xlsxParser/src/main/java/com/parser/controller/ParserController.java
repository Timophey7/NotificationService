package com.parser.controller;

import com.parser.mappers.MapToXlsxInfoResponse;
import com.parser.model.XlsxInfo;
import com.parser.model.XlsxInfoResponse;
import com.parser.repository.XlsxInfoRepository;
import com.parser.service.ParseInfoFromXlsService;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/xlsx/parse/")
public class ParserController {

    private static final Logger log = LoggerFactory.getLogger(ParserController.class);
    private final ParseInfoFromXlsService parseInfoFromXlsService;
    private final XlsxInfoRepository xlsxInfoRepository;
    private final MapToXlsxInfoResponse mapper;
    private final KafkaTemplate<String, XlsxInfoResponse> kafkaTemplate;

    @Timed("homeMethodPoint")
    @GetMapping("/home")
    public String home(Model model,HttpSession session) {
        log.info("homeMethodPoint");
        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
        }else {
            model.addAttribute("message","");
        }
        return "home";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            parseInfoFromXlsService.parseXLS(file);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return "upload-file";
    }

    @Timed("sendMessagePoint")
    @PostMapping("/send")
    public String  sendMessages(@RequestParam String text, HttpSession session){
        List<XlsxInfo> all = xlsxInfoRepository.findAll();
        for (XlsxInfo xlsxInfo : all){
            XlsxInfoResponse xlsxInfoResponse = mapper.mapToXlsxInfoResponse(xlsxInfo, text);
            kafkaTemplate.send("usersInfo", xlsxInfoResponse);
            log.info("send message to kafka");
        }
        session.setAttribute("message","Вы успешно отправили письма!");
        return "redirect:/v1/xlsx/parse/home";

    }


}
