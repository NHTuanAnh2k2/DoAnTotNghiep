package com.example.demo.controller.customer;

import com.example.demo.entity.SanPham;
import com.example.demo.repository.SanPhamRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class JsonTestController {
    @Autowired
    SanPhamRepositoty sanPhamRepositoty;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        List<SanPham> sanPhams = sanPhamRepositoty.findAll();
        return ResponseEntity.ok(sanPhams);
    }
    @GetMapping("/test2")
    public String test2(){
        return "customer/jsontest";
    }
}
