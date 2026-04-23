package com.example.chat.service;

import com.example.chat.model.ChatRoom;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomService {
    private final Map<String, ChatRoom> rooms = new HashMap<>();

    public RoomService() {
        rooms.put("geral", new ChatRoom("geral", "Geral"));
        rooms.put("tecnologia", new ChatRoom("tecnologia", "Tecnologia"));
        rooms.put("jogos", new ChatRoom("jogos", "Jogos"));
    }

    public List<ChatRoom> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public ChatRoom getRoom(String id) {
        return rooms.get(id);
    }

    public ChatRoom createRoom(String name) {
        String id = name.toLowerCase().replace(" ", "-");
        ChatRoom room = new ChatRoom(id, name);
        rooms.put(id, room);
        return room;
    }
}

