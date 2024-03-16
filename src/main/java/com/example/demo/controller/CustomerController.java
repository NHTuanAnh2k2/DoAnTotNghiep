package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {
    @GetMapping("/trangchu")
    public String trangchu() {
        return "customer/trangchu";
    }

    @GetMapping("/modal")
    public String modal() {
        return "customer/modal";
    }
    @GetMapping("/product/detail")
    public String productdetail() {
        return "customer/product-details";
    }
    @GetMapping("/cart")
    public String cart() {
        return "customer/cart";
    }

    @GetMapping("/quenmatkhau")
    public String quenmatkhau() {
        return "same/quenmatkhau";
    }

    @GetMapping("/hosokhachhang")
    public String hosokhachhang() {
        return "customer/hosokhachhang";
    }


}
