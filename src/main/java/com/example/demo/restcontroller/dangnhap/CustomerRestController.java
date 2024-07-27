package com.example.demo.restcontroller.dangnhap;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.LichSuHoaDon;
import com.example.demo.repository.LichSuHoaDon.LichSuHoaDonRepository;
import com.example.demo.repository.hoadon.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    LichSuHoaDonRepository lichSuHoaDonRepository;


    @GetMapping("/huydonhang/{id}")
    public ResponseEntity<?> huydonhang(@PathVariable("id") Integer id,
                                        @RequestParam("lydohuy") String lydohuy,
                                        RedirectAttributes redirectAttributes) {
        HoaDon hoaDon = hoaDonRepository.findHoaDonById(id);
        hoaDon.setTrangthai(6);
        hoaDon.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        hoaDon.setNgaygiaodukien(null);
        hoaDon.setNgayxacnhan(null);
        hoaDon.setNgayvanchuyen(null);
        hoaDon.setNgayhoanthanh(null);
        hoaDon.setNguoicapnhat("CUSTOMER");
        hoaDonRepository.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = lichSuHoaDonRepository.findLichSuHoaDonByIdHoaDon(id);
        lichSuHoaDon.setLancapnhatcuoi(hoaDon.getLancapnhatcuoi());
        lichSuHoaDon.setNguoicapnhat("CUSTOMER");
        lichSuHoaDon.setGhichu(lydohuy);
        lichSuHoaDonRepository.save(lichSuHoaDon);
        redirectAttributes.addFlashAttribute("huyhangSuccess", true);
        return ResponseEntity.ok("Thành công");
    }

}
