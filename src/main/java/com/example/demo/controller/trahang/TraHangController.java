package com.example.demo.controller.trahang;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TraHangController {
    @GetMapping("/admin/qltrahang")
    public String qltrahang() {
        return "admin/qltrahang";
    }
}
