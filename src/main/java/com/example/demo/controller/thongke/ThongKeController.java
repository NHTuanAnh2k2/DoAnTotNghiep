package com.example.demo.controller.thongke;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongKeController {
    @GetMapping("/admin/qlthongke")
    public String admin() {
        return "admin/qlthongke";
    }
}
