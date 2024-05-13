package com.example.demo.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DangKyController {
    @RequestMapping("/view-dang-ky")
    public String view(){
        return "customer/login";
    }
}
