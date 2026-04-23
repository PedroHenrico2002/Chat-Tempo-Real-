package com.example.chat.controller;

import com.example.chat.model.ChatRoom;
import com.example.chat.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<ChatRoom> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping
    public ChatRoom createRoom(@RequestParam String name) {
        return roomService.createRoom(name);
    }
}

