package com.example.demo.controller.banhang;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.NhanVien;
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
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("ban-hang-tai-quay")
public class BanHangController {
    @Autowired
    HoaDonService daoHD;

    @GetMapping("hoa-don-cho")
    @ResponseBody
    public ResponseEntity<?> getLstCho() {
        List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
        return ResponseEntity.ok(lst);
    }

    @GetMapping("tao-don-cho")
    @ResponseBody
    public ResponseEntity<?> createNewHD() {
        HoaDon hoadonCho = new HoaDon();
        hoadonCho.setTrangthai(0);
        hoadonCho.setLoaihoadon(false);
        NhanVien nv = new NhanVien();
        nv.setId(1);
        // fake id nhân viên sau này sẽ lấy thì login xuống
        hoadonCho.setNhanvien(nv);
        HoaDon hdMaGet = daoHD.timBanGhiDuocTaoGanNhat();
        Integer maMoi = Integer.valueOf(hdMaGet.getMahoadon().trim().substring(8)) + 1;
        hoadonCho.setMahoadon("HDFSPORT" + maMoi);
        LocalDateTime currentDateTime = LocalDateTime.now();
        hoadonCho.setNgaytao(Timestamp.valueOf(currentDateTime));
        daoHD.capNhatHD(hoadonCho);
        List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
        return ResponseEntity.ok(lst);
    }

}
