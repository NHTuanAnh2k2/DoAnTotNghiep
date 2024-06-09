package com.example.demo.controller.giohang;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GioHangController {
    @GetMapping("/cart")
    public String cart() {
        return "customer/cart";
    }
}
