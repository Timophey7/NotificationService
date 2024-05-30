package com.parser.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ParseInfoFromXlsService {

    void parseXLS(MultipartFile file) throws IOException;

}
