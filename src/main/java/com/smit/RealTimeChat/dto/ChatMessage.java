package com.smit.RealTimeChat.dto;

import java.time.LocalDateTime;

public class ChatMessage {

    private Long id;
    private String content;
    private String senderEmail;
    private String senderUsername;
    private Long roomId;
    private LocalDateTime createdAt;

    public ChatMessage() {
    }

    public ChatMessage(Long id, String content, String senderEmail, String senderUsername, Long roomId, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.senderEmail = senderEmail;
        this.senderUsername = senderUsername;
        this.roomId = roomId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}