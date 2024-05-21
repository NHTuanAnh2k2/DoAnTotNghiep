package com.example.demo.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GioiThieuController {
    @GetMapping("/customer/gioithieu")
    public String gioithieu(){
        return "customer/gioithieu";
    }
}
