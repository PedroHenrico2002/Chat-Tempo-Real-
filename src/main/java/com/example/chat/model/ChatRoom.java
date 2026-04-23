package com.example.chat.model;

import java.util.*;

public class ChatRoom {
    private String id;
    private String name;
    private Set<String> users = new HashSet<>();

    public ChatRoom(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<String> getUsers() { return users; }
    public void addUser(String user) { users.add(user); }
    public void removeUser(String user) { users.remove(user); }
}

