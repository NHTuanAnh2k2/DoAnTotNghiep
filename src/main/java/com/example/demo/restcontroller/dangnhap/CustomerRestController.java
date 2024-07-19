package com.example.demo.restcontroller.dangnhap;

import com.example.demo.entity.HoaDon;
import com.example.demo.repository.hoadon.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired
    HoaDonRepository hoaDonRepository;

    @GetMapping("/huydonhang/{id}")
    public ResponseEntity<?> huydonhang(@PathVariable Integer id) {
        HoaDon hoaDon = hoaDonRepository.findHoaDonById(id);
        hoaDon.setTrangthai(6);
        hoaDon.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        hoaDon.setNgaygiaodukien(null);
        hoaDon.setNgayxacnhan(null);
        hoaDon.setNgayvanchuyen(null);
        hoaDon.setNgayhoanthanh(null);
        hoaDon.setNguoicapnhat("CUSTOMER");
        hoaDonRepository.save(hoaDon);
        return ResponseEntity.ok("Thành công");
    }

}
