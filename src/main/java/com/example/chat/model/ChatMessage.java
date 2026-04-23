package com.example.chat.model;

public class ChatMessage {
    private String sender;
    private String content;
    private String room;
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    public ChatMessage() {}

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
}

