package com.parser.mappers;

import com.parser.model.XlsxInfo;
import com.parser.model.XlsxInfoResponse;
import org.springframework.stereotype.Service;

@Service
public class MapToXlsxInfoResponse {

    public XlsxInfoResponse mapToXlsxInfoResponse(XlsxInfo info, String message){
        XlsxInfoResponse xlsxInfoResponse = new XlsxInfoResponse();

        xlsxInfoResponse.setEmail(info.getEmail());
        xlsxInfoResponse.setFio(info.getFio());
        xlsxInfoResponse.setPhoneNum(info.getPhoneNum());
        xlsxInfoResponse.setId(info.getId());
        xlsxInfoResponse.setMessage(message);

        return xlsxInfoResponse;
    }

}
