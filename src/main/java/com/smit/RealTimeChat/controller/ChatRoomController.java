package com.smit.RealTimeChat.controller;

import com.smit.RealTimeChat.dto.AddMemberRequest;
import com.smit.RealTimeChat.dto.ChatRoomResponse;
import com.smit.RealTimeChat.dto.CreateGroupChatRequest;
import com.smit.RealTimeChat.dto.CreatePrivateChatRequest;
import com.smit.RealTimeChat.service.ChatRoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/private")
    public ResponseEntity<ChatRoomResponse> createPrivateChat(
            @Valid @RequestBody CreatePrivateChatRequest request,
            Authentication authentication
    ) {
        String currentUserEmail = authentication.getName();
        ChatRoomResponse response = chatRoomService.createPrivateChat(currentUserEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/group")
    public ResponseEntity<ChatRoomResponse> createGroupChat(
            @Valid @RequestBody CreateGroupChatRequest request,
            Authentication authentication
    ) {
        String currentUserEmail = authentication.getName();
        ChatRoomResponse response = chatRoomService.createGroupChat(currentUserEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{roomId}/members")
    public ResponseEntity<ChatRoomResponse> addMember(
            @PathVariable Long roomId,
            @Valid @RequestBody AddMemberRequest request
    ) {
        ChatRoomResponse response = chatRoomService.addMember(roomId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-rooms")
    public ResponseEntity<List<ChatRoomResponse>> getUserChatRooms(Authentication authentication) {
        String currentUserEmail = authentication.getName();
        List<ChatRoomResponse> rooms = chatRoomService.getUserChatRooms(currentUserEmail);
        return ResponseEntity.ok(rooms);
    }
}