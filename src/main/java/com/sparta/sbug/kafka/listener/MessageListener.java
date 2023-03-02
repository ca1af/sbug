package com.sparta.sbug.kafka.listener;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.kafka.configuration.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {

    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listen(ChatResponseDto chatResponseDto) {
        log.info("[KAFKA LISTENER] : " + chatResponseDto.getMessage());
        // receiver
        template.convertAndSend("/topic/chats/rooms/" + chatResponseDto.getRoomId(), chatResponseDto);
    }

}
