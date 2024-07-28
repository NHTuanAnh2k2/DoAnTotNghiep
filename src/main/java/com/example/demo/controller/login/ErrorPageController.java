package com.example.demo.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController {

    @GetMapping("/errorpage/admin")
    public String hienthi() {
        return "admin/dangnhap/errorpage";
    }
}
