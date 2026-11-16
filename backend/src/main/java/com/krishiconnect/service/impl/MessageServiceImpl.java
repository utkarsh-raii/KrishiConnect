package com.krishiconnect.service.impl;

import com.krishiconnect.dto.MessageDto;
import com.krishiconnect.dto.SendMessageRequest;
import com.krishiconnect.entity.CropListing;
import com.krishiconnect.entity.Message;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.CropListingRepository;
import com.krishiconnect.repository.MessageRepository;
import com.krishiconnect.repository.UserRepository;
import com.krishiconnect.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final CropListingRepository cropListingRepository;

    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    public MessageServiceImpl(MessageRepository messageRepository,
                              UserRepository userRepository,
                              CropListingRepository cropListingRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.cropListingRepository = cropListingRepository;
    }

    @Override
    @Transactional
    public MessageDto sendMessage(Long senderId, SendMessageRequest req) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        User receiver = userRepository.findById(req.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        CropListing crop = null;
        if (req.getCropId() != null) {
            crop = cropListingRepository.findById(req.getCropId()).orElse(null);
        }

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setCrop(crop);
        message.setSenderRole(sender.getRole());
        message.setMessage(req.getMessage());

        message = messageRepository.save(message);

        MessageDto dto = MessageDto.fromEntity(message);

        // Real-time WebSocket dispatch to recipient's private destination
        if (messagingTemplate != null) {
            try {
                messagingTemplate.convertAndSend("/topic/messages/" + receiver.getId(), dto);
            } catch (Exception ignored) {}
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getConversation(Long user1, Long user2, Long cropId) {
        return messageRepository.findConversation(user1, user2, cropId)
                .stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }
}
