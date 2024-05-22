package com.example.demo.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LienHeController {
    @GetMapping("/customer/lienhe")
    public String gioithieu(){
        return "customer/lienhe";
    }

    @GetMapping("/customer/vechungtoi")
    public String vechungtoi(){
        return "customer/vechungtoi";
    }

}
