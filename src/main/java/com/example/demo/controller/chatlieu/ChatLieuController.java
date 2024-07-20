package com.example.demo.controller.chatlieu;

import com.example.demo.entity.ChatLieu;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.ChatLieuRepository;
import com.example.demo.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatLieuController {
    @Autowired
    ChatLieuRepository chatLieuRepository;
    @Autowired
    ChatLieuService chatLieuService;

    @GetMapping("/chatlieu")
    public String display(@ModelAttribute("search") ThuocTinhInfo info, @ModelAttribute("chatlieu") ChatLieu chatLieu, Model model) {
        List<ChatLieu> list;
        String trimmedKey = (info.getKey() != null) ? info.getKey().trim().replaceAll("\\s+", " ") : null;
        boolean isKeyEmpty = (trimmedKey == null || trimmedKey.trim().isEmpty());
        boolean isTrangthaiNull = (info.getTrangthai() == null);
        if (isKeyEmpty && isTrangthaiNull) {
            list = chatLieuRepository.findAllByOrderByNgaytaoDesc();
        } else {
            list = chatLieuRepository.findByTenAndTrangthai("%" + trimmedKey + "%", info.getTrangthai());
        }
        model.addAttribute("fillSearch", info.getKey());
        model.addAttribute("fillTrangThai", info.getTrangthai());
        model.addAttribute("lstChatLieu", list);
        return "admin/qlchatlieu";
    }

    @PostMapping("/chatlieu/updateTrangThai/{id}")
    public String updateTrangThaiChatLieu(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        ChatLieu existingChatLieu = chatLieuRepository.findById(id).orElse(null);
        if (existingChatLieu != null) {
            existingChatLieu.setTrangthai(!existingChatLieu.getTrangthai());
            chatLieuRepository.save(existingChatLieu);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        }
        return "redirect:/chatlieu";
    }


    @PostMapping("/updateChatLieu/{id}")
    public String updateChatLieu(@PathVariable Integer id) {
        chatLieuRepository.updateTrangThaiToFalseById(id);
        return "redirect:/chatlieu";
    }

    @PostMapping("/add")
    public String add(Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu) {
        String trimmedTenChatLieu = (chatLieu.getTen() != null)
                ? chatLieu.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        chatLieu.setTen(trimmedTenChatLieu);
        chatLieu.setTrangthai(true);
        chatLieu.setNgaytao(currentTime);
        chatLieu.setLancapnhatcuoi(currentTime);
        chatLieuService.add(chatLieu);
        return "redirect:/chatlieu";
    }

    @PostMapping("/addChatLieuModal")
    public String addChatLieuModal(Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu) {
        String trimmedTenChatLieu = (chatLieu.getTen() != null)
                ? chatLieu.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        chatLieu.setTen(trimmedTenChatLieu);
        chatLieu.setTrangthai(true);
        chatLieu.setNgaytao(currentTime);
        chatLieu.setLancapnhatcuoi(currentTime);
        chatLieuService.add(chatLieu);
        return "redirect:/viewaddSPGET";
    }

    @PostMapping("/addChatLieuSua")
    public String addChatLieuSua(Model model, @ModelAttribute("chatlieu") ChatLieu chatLieu, @RequestParam("spctId") Integer spctId) {
        String trimmedTenChatLieu = (chatLieu.getTen() != null)
                ? chatLieu.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        chatLieu.setTen(trimmedTenChatLieu);
        chatLieu.setTrangthai(true);
        chatLieu.setNgaytao(currentTime);
        chatLieu.setLancapnhatcuoi(currentTime);
        chatLieuService.add(chatLieu);
        return "redirect:/updateCTSP/" + spctId;
    }

    @GetMapping("/chatlieu/delete/{id}")
    public String delete(@PathVariable int id) {
        chatLieuService.deleteById(id);
        return "redirect:/chatlieu";
    }
}
