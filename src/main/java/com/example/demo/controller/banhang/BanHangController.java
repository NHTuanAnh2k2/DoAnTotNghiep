package com.example.demo.controller.banhang;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("ban-hang-tai-quay")
public class BanHangController {
    @Autowired
    HoaDonService daoHD;

    @GetMapping("hoa-don-cho")
    @ResponseBody
    public ResponseEntity<?> getLstCho() {
        List<HoaDon> lst=daoHD.timTheoTrangThaiVaLoai(0,false);
        return ResponseEntity.ok(lst);
    }

    @GetMapping("tao-don-cho")
    @ResponseBody
    public ResponseEntity<?> createNewHD() {
//        HoaDon hoadonCho=new HoaDon();
//        hoadonCho.setTrangthai(0);
//        hoadonCho.setLoaihoadon(false);
        List<HoaDon> lst=daoHD.timTheoTrangThaiVaLoai(0,false);
        return ResponseEntity.ok(lst);
    }

}
