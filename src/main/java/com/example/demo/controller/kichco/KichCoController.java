package com.example.demo.controller.kichco;

import com.example.demo.entity.KichCo;
import com.example.demo.service.impl.KichCoImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KichCoController {
    @Autowired
    KichCoImp kichCoImp;

    @PostMapping("/addKichCoModal")
    public String addKichCoModal(@ModelAttribute("kichco") KichCo kichCo) {
        kichCoImp.addKichCo(kichCo);
        return "redirect:/viewaddSP";
    }
}
