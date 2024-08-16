package com.parser.service;

import com.parser.exceptions.FileIsEmptyException;
import com.parser.exceptions.TextIsEmptyException;
import com.parser.model.XlsxInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ParseInfoFromXlsService {

    void parseXLS(MultipartFile file) throws IOException, FileIsEmptyException;

    void sendMessage(String text) throws TextIsEmptyException;

}
