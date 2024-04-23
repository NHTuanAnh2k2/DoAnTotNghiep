package com.example.demo.restcontroller.khachhang;

import com.example.demo.entity.KhachHang;
import com.example.demo.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class KhachHangRestController {
    @Autowired
    KhachHangService khachHangService;

    @GetMapping("/cities")
    public ResponseEntity<List<Province>> getCities() {
        List<Province> cities = khachHangService.getCities();
        if (cities != null) {
            return ResponseEntity.ok(cities);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/hienthi")
    public ResponseEntity<List<KhachHang>> getKhachHang() {
        return ResponseEntity.ok(khachHangService.findAllKhachHang());
    }

}

