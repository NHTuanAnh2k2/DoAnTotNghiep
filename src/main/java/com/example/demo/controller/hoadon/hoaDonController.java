package com.example.demo.controller.hoadon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class hoaDonController {
    @GetMapping("qlhoadon")
    public String admin() {
        return "admin/qlhoadon";
    }
}
