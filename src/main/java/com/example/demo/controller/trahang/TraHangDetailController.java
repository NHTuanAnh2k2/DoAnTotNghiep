package com.example.demo.controller.trahang;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TraHangDetailController {
    @GetMapping("/admin/trahangdetail")
    public String trahangdetail() {
        return "admin/detailqltrahang";
    }
}
