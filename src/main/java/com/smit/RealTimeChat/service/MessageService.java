package com.smit.RealTimeChat.service;

import com.smit.RealTimeChat.dto.MessageResponse;
import com.smit.RealTimeChat.dto.SendMessageRequest;
import com.smit.RealTimeChat.entity.ChatRoom;
import com.smit.RealTimeChat.entity.Message;
import com.smit.RealTimeChat.entity.User;
import com.smit.RealTimeChat.exception.UserNotFoundException;
import com.smit.RealTimeChat.repository.ChatRoomRepository;
import com.smit.RealTimeChat.repository.MessageRepository;
import com.smit.RealTimeChat.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public MessageService(
            MessageRepository messageRepository,
            ChatRoomRepository chatRoomRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MessageResponse sendMessage(Long roomId, String senderEmail, SendMessageRequest request) {

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + senderEmail));

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + roomId));

        boolean isMember = chatRoom.getUsers().stream()
                .anyMatch(user -> user.getEmail().equals(senderEmail));

        if (!isMember) {
            throw new RuntimeException("User is not a member of this chat room");
        }

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(sender);
        message.setChatRoom(chatRoom);

        Message saved = messageRepository.save(message);

        return mapToResponse(saved);
    }

    public List<MessageResponse> getChatHistory(Long roomId, String currentUserEmail) {

        userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + currentUserEmail));

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + roomId));

        boolean isMember = chatRoom.getUsers().stream()
                .anyMatch(user -> user.getEmail().equals(currentUserEmail));

        if (!isMember) {
            throw new RuntimeException("User is not a member of this chat room");
        }

        List<Message> messages = messageRepository.findByChatRoomOrderByCreatedAtAsc(chatRoom);

        return messages.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse mapToResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getSender().getEmail(),
                message.getSender().getUsername(),
                message.getChatRoom().getId(),
                message.getCreatedAt()
        );
    }
}