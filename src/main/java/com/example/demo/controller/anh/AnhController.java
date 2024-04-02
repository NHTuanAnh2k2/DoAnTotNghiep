package com.example.demo.controller.anh;

import com.example.demo.repository.AnhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnhController {
    @Autowired
    AnhRepository anhRepository;

    @GetMapping("/listAnh")
    public String listAnh(Model model) {
        model.addAttribute("listAnh", anhRepository.findAll());
        return "admin/anh";
    }
}
