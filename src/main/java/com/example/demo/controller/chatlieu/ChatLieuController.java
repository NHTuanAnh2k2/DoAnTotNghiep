package com.example.demo.controller.chatlieu;

import com.example.demo.entity.ChatLieu;
import com.example.demo.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChatLieuController {
    @Autowired
    ChatLieuService chatLieuService;

    @GetMapping("/chatlieu")
    public String display(Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu) {
        model.addAttribute("lstChatLieu", chatLieuService.findAll());
        return "admin/qlchatlieu";
    }

    @PostMapping("/add")
    public String add(Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu) {
        chatLieuService.add(chatLieu);
        return "redirect:/chatlieu";
    }

    @PostMapping("/addChatLieuModal")
    public String addChatLieuModal(Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu) {
        chatLieuService.add(chatLieu);
        return "redirect:/viewaddSP";
    }

    @GetMapping("/chatlieu/timkiem")
    public String timKiem(Model model,
                          @RequestParam(required = false) String tensearch,
                          @RequestParam(required = false) Boolean trangthaisearch,
                          @ModelAttribute("chatlieu") ChatLieu chatLieu
    ) {

        List<ChatLieu> lstTimKiem = chatLieuService.findByTen(tensearch, trangthaisearch);
        model.addAttribute("lstChatLieu", lstTimKiem);
        return "admin/qlchatlieu";
    }

    @GetMapping("/chatlieu/delete/{id}")
    public String delete(@PathVariable int id) {
        chatLieuService.deleteById(id);
        return "redirect:/chatlieu";
    }
}
