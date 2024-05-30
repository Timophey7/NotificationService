package com.parser.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parser.model.XlsxInfoResponse;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class XlsxInfoSerializer implements Serializer<XlsxInfoResponse> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, XlsxInfoResponse userInfo) {
        if (userInfo == null) {
            return null;
        }

        try {
            return mapper.writeValueAsBytes(userInfo);
        }catch (Exception e){
            throw new SerializationException("Exception in serialization of userInfo", e);
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
