package com.example.demo.controller.NguoiDung;

import com.example.demo.service.impl.DiaChiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DiaChiController {
    @Autowired
    DiaChiImpl diaChi;
    @GetMapping("/wards")
    public List<String> getAllWards() {
        return diaChi.TimTinh();
    }

    @GetMapping("/districts")
    public List<String> getAllDistricts() {
        return diaChi.TimHuyen();
    }

    @GetMapping("/provinces")
    public List<String> getAllProvinces() {
        return diaChi.TimXa();
    }
}
