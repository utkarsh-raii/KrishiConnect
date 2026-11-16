package com.krishiconnect.service;

import com.krishiconnect.dto.MessageDto;
import com.krishiconnect.dto.SendMessageRequest;

import java.util.List;

public interface MessageService {

    MessageDto sendMessage(Long senderId, SendMessageRequest req);

    List<MessageDto> getConversation(Long user1, Long user2, Long cropId);
}
