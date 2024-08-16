package com.parser.controller;

import com.parser.exceptions.TextIsEmptyException;
import com.parser.service.ParseInfoFromXlsService;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/xlsx/parse/")
public class ParserController {

    private final ParseInfoFromXlsService parseInfoFromXlsService;

    @Timed("homeMethodPoint")
    @GetMapping("/home")
    public String home(Model model,HttpSession session) {
        String message = (String) session.getAttribute("message");
        model.addAttribute("message", message);
        return "home";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file,Model model) {
        try {
            parseInfoFromXlsService.parseXLS(file);
        }catch (Exception e) {
            model.addAttribute("fileError", e.getMessage());
            return "home";
        }
        return "upload-file";
    }

    @Timed("sendMessagePoint")
    @PostMapping("/send")
    public String sendMessages(@RequestParam String text, HttpSession session,Model model){
        try {
            parseInfoFromXlsService.sendMessage(text);
            session.setAttribute("message", "Вы успешно отправили письма!");
            return "redirect:/v1/xlsx/parse/home";
        }catch (TextIsEmptyException exception){
            model.addAttribute("textError", exception.getMessage());
            return "upload-file";
        }
    }


}
