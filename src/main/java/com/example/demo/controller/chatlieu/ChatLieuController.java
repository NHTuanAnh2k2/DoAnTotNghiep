package com.example.demo.controller.chatlieu;

import com.example.demo.entity.ChatLieu;
import com.example.demo.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChatLieuController {
    @Autowired
    ChatLieuService chatLieuService;
    @GetMapping("/chatlieu")
    public String display(Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu){
        model.addAttribute("lstChatLieu", chatLieuService.findAll());
        return "admin/qlchatlieu";
    }
    @PostMapping("/add")
    public String add (Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu) {
        chatLieuService.add(chatLieu);
        return "redirect:/chatlieu";
    }
}
