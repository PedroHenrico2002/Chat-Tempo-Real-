package com.example.chat.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserService {
    private final Map<String, Set<String>> roomUsers = new ConcurrentHashMap<>();
    private final Set<String> globalUsers = ConcurrentHashMap.newKeySet();

    public void userJoined(String room, String username) {
        roomUsers.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(username);
        globalUsers.add(username);
    }

    public void userLeft(String room, String username) {
        Set<String> users = roomUsers.get(room);
        if (users != null) {
            users.remove(username);
        }
    }

    public Set<String> getUsersInRoom(String room) {
        return roomUsers.getOrDefault(room, Collections.emptySet());
    }

    public Set<String> getAllOnlineUsers() {
        return globalUsers;
    }
}

