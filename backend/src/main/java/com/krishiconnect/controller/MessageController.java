package com.krishiconnect.controller;

import com.krishiconnect.dto.MessageDto;
import com.krishiconnect.dto.SendMessageRequest;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> sendMessage(@AuthenticationPrincipal UserPrincipal currentUser,
                                                           @Valid @RequestBody SendMessageRequest req) {
        MessageDto msg = messageService.sendMessage(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Message sent successfully");
        res.put("data", msg);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMessages(@AuthenticationPrincipal UserPrincipal currentUser,
                                                           @RequestParam(required = false) Long otherUserId,
                                                           @RequestParam(required = false) Long user1,
                                                           @RequestParam(required = false) Long user2,
                                                           @RequestParam(required = false) Long cropId) {
        Long first = currentUser.getId();
        Long second = otherUserId;

        if (user1 != null && user2 != null) {
            first = user1;
            second = user2;
        }

        List<MessageDto> messages = messageService.getConversation(first, second, cropId);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", messages);
        res.put("messages", messages);
        return ResponseEntity.ok(res);
    }
}
