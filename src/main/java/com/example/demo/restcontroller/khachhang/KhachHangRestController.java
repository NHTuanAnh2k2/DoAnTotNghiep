package com.example.demo.restcontroller.khachhang;

import com.example.demo.service.KhachHangService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class KhachHangRestController {
    @Autowired
    KhachHangService khachHangService;

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities() {
        List<String> cities = khachHangService.getCities();
        if (cities != null) {
            return ResponseEntity.ok(cities);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/districts")
    public ResponseEntity<List<String>> getDistrictsByCityId(@RequestParam String cityId) {
        List<String> districts = khachHangService.getDistricts(cityId);
        if (districts != null) {
            return ResponseEntity.ok(districts);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

