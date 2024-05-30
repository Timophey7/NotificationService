package com.parser.service.impl;

import com.parser.model.XlsxInfo;
import com.parser.repository.XlsxInfoRepository;
import com.parser.service.ParseInfoFromXlsService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;


@Service
@RequiredArgsConstructor
public class ParseInfoFromXlsServiceImpl implements ParseInfoFromXlsService {

    private static final Logger log = LoggerFactory.getLogger(ParseInfoFromXlsServiceImpl.class);
    private final XlsxInfoRepository xlsxInfoRepository;


    @Timed("parseXlsx")
    @Override
    public void parseXLS(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", ".xlsx");
        file.transferTo(tempFile);
        FileInputStream fis = new FileInputStream(tempFile);
        XSSFWorkbook xwb = new XSSFWorkbook(fis);
        XSSFSheet sheet = xwb.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            XlsxInfo xlsxInfo = new XlsxInfo();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                if (cellValue.equals("ФИО")){
                                    break;
                                }
                                xlsxInfo.setFio(cellValue);
                                break;
                            case 1:
                                if (cellValue.equals("Номер Телефона")){
                                    break;
                                }
                                xlsxInfo.setPhoneNum(cellValue);
                                break;
                            case 2:
                                if (cellValue.equals("Email")){
                                    break;
                                }
                                xlsxInfo.setEmail(cellValue);
                                break;
                            default:
                        }
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        break;
                    default:
                        System.out.print("Unknown type\t");
                }
            }

            if (xlsxInfo.getFio() != null && xlsxInfo.getPhoneNum() !=null && xlsxInfo.getEmail() != null){
                xlsxInfoRepository.save(xlsxInfo);
                log.info("save xlsx info success");
            }


        }
        xwb.close();
    }





}
