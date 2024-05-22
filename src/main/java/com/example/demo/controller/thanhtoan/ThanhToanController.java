package com.example.demo.controller.thanhtoan;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThanhToanController {
    @RequestMapping("/view-thanh-toan")
    public String view(){
        return "customer/thanhtoan";
    }
}
