package com.example.demo.service;

import com.example.demo.entity.ChatLieu;

import java.util.List;

public interface ChatLieuService {
    List<ChatLieu> findAll();

    void add(ChatLieu chatLieu);
}
