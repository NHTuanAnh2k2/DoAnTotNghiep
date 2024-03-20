package com.example.demo.service;

import com.example.demo.entity.ChatLieu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatLieuService {
    List<ChatLieu> findAll();
    void add(ChatLieu chatLieu);

    List<ChatLieu> findByTen(String ten, Boolean trangthai);

    void deleteById(int id);

    ChatLieu getById(int id);
}
