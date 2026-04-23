package com.example.chat.controller;

import com.example.chat.model.ChatMessage;
import com.example.chat.service.OnlineUserService;
import com.example.chat.service.RoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final OnlineUserService onlineUserService;
    private final RoomService roomService;

    public ChatController(SimpMessagingTemplate messagingTemplate,
                          OnlineUserService onlineUserService,
                          RoomService roomService) {
        this.messagingTemplate = messagingTemplate;
        this.onlineUserService = onlineUserService;
        this.roomService = roomService;
    }

    @MessageMapping("/chat/{roomId}/send")
    public void sendMessage(@Payload ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/" + message.getRoom(), message);
    }

    @MessageMapping("/chat/{roomId}/join")
    public void joinRoom(@Payload ChatMessage message,
                         SimpMessageHeaderAccessor headerAccessor) {
        String room = message.getRoom();
        String username = message.getSender();

        headerAccessor.getSessionAttributes().put("username", username);
        headerAccessor.getSessionAttributes().put("room", room);

        onlineUserService.userJoined(room, username);

        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setType(ChatMessage.MessageType.JOIN);
        joinMessage.setSender(username);
        joinMessage.setRoom(room);
        joinMessage.setContent(username + " entrou na sala!");

        messagingTemplate.convertAndSend("/topic/" + room, joinMessage);
        messagingTemplate.convertAndSend("/topic/" + room + "/users",
                Map.of("users", onlineUserService.getUsersInRoom(room)));
    }

    @MessageMapping("/chat/{roomId}/leave")
    public void leaveRoom(@Payload ChatMessage message) {
        String room = message.getRoom();
        String username = message.getSender();

        onlineUserService.userLeft(room, username);

        ChatMessage leaveMessage = new ChatMessage();
        leaveMessage.setType(ChatMessage.MessageType.LEAVE);
        leaveMessage.setSender(username);
        leaveMessage.setRoom(room);
        leaveMessage.setContent(username + " saiu da sala!");

        messagingTemplate.convertAndSend("/topic/" + room, leaveMessage);
        messagingTemplate.convertAndSend("/topic/" + room + "/users",
                Map.of("users", onlineUserService.getUsersInRoom(room)));
    }
}

