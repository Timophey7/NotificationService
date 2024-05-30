package com.notification.config.kafka;

import com.notification.model.XlsxInfoResponse;
import com.notification.service.MessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaListenerService {

    private final MessagesService messagesService;

    @KafkaListener(topics = "usersInfo",groupId = "users")
    public void listen(XlsxInfoResponse xlsxInfoResponse){
        messagesService.sendMessages(xlsxInfoResponse);
    }

}
