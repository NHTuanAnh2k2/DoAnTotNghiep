package com.example.demo.controller.hoadon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class hoaDonController {
    @GetMapping("/admin/banhangtaiquay")
    public String admin() {
        return "admin/banhangtaiquay";
    }
}
