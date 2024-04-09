package com.example.demo.controller.chatlieu;

import com.example.demo.entity.ChatLieu;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.ChatLieuRepository;
import com.example.demo.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatLieuController {
    @Autowired
    ChatLieuRepository chatLieuRepository;
    @Autowired
    ChatLieuService chatLieuService;

    @GetMapping("/chatlieu")
    public String display( @ModelAttribute("search") ThuocTinhInfo info ,@ModelAttribute("chatlieu") ChatLieu chatLieu,Model model) {
        List<ChatLieu> list = null;
        if (info.getKey() != null) {
            list = chatLieuRepository.getChatLieuByTenOrTrangthai(info.getKey(), info.getTrangthai());
        } else {
            list = chatLieuRepository.findAllByOrderByNgaytaoDesc();
        }
        model.addAttribute("lstChatLieu", list);
        return "admin/qlchatlieu";
    }

    @PostMapping("/add")
    public String add(Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu) {
        LocalDateTime currentTime = LocalDateTime.now();
        chatLieu.setNgaytao(currentTime);
        chatLieuService.add(chatLieu);
        return "redirect:/chatlieu";
    }

    @PostMapping("/addChatLieuModal")
    public String addChatLieuModal(Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu) {
        LocalDateTime currentTime = LocalDateTime.now();
        chatLieu.setNgaytao(currentTime);
        chatLieuService.add(chatLieu);
        return "redirect:/viewaddSP";
    }

    @GetMapping("/chatlieu/delete/{id}")
    public String delete(@PathVariable int id) {
        chatLieuService.deleteById(id);
        return "redirect:/chatlieu";
    }
}
