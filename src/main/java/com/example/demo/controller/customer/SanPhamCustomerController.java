package com.example.demo.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanPhamCustomerController {

    @GetMapping("/customer/sanpham")
    public String sanpham(){
        return "customer/sanpham";
    }
}
