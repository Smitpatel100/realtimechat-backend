package com.smit.RealTimeChat.controller;

import com.smit.RealTimeChat.dto.MessageResponse;
import com.smit.RealTimeChat.dto.SendMessageRequest;
import com.smit.RealTimeChat.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/rooms/{roomId}")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable Long roomId,
            @Valid @RequestBody SendMessageRequest request,
            Authentication authentication
    ) {
        String senderEmail = authentication.getName();
        MessageResponse response = messageService.sendMessage(roomId, senderEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<MessageResponse>> getChatHistory(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        String currentUserEmail = authentication.getName();
        List<MessageResponse> messages = messageService.getChatHistory(roomId, currentUserEmail);
        return ResponseEntity.ok(messages);
    }
}