package com.example.demo.service.impl;

import com.example.demo.entity.ChatLieu;
import com.example.demo.repository.ChatLieuRepository;
import com.example.demo.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ChatLieuImp implements ChatLieuService {
    @Autowired
    ChatLieuRepository chatLieuRepository;

    @Override
    public List<ChatLieu> findAll() {
        return chatLieuRepository.findAll();
    }

    @Override
    public void add(ChatLieu chatLieu) {
        chatLieu.setNguoi_tao("admin");
        chatLieu.setNguoi_cap_nhat("admin");
        chatLieu.setLan_cap_nhat_cuoi(new Date());
        chatLieu.setNgay_tao(new Date());
        chatLieuRepository.save(chatLieu);
    }
}
