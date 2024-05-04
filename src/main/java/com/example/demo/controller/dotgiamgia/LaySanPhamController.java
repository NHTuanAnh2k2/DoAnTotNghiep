package com.example.demo.controller.dotgiamgia;

import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.entity.SanPhamDotGiam;
import com.example.demo.service.impl.SanPHamDotGiamImp;
import com.example.demo.service.impl.SanPhamChiTietImp;
import com.example.demo.service.impl.SanPhamImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class LaySanPhamController {
    @Autowired
    SanPhamImp sanPhamImp;
    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;
    @Autowired
    SanPHamDotGiamImp sanPHamDotGiamImp;

    @GetMapping("/hien-thi-san-pham")
    public List<SanPham> getProducts() {
        List<SanPham> lstSanPham = sanPhamImp.findAll();
        return lstSanPham;
    }
    @GetMapping("/hien-thi-san-pham-chi-tiet/{Id}")
    public List<SanPhamChiTiet> getProductDetails(@PathVariable int Id) {
        List<SanPhamChiTiet> details = new ArrayList<>();
        List<SanPhamChiTiet> lst= sanPhamChiTietImp.findBySanPhamId(Id);
        for(SanPhamChiTiet sp : lst){
            details.add(sp);
        }
        return details;
    }
    @GetMapping("/san-pham-dot-giam")
    public List<SanPham> sanphamdotgiam(HttpSession session){
        DotGiamGia dotGiamGia = (DotGiamGia) session.getAttribute("dotGG");
        List<SanPhamDotGiam> lstSPDG= sanPHamDotGiamImp.findSanPhamDotGiamByIdDotgiamgia(dotGiamGia.getId());
        List<Integer> lstIdSanPham= new ArrayList<>();
        for(SanPhamDotGiam s: lstSPDG){
            lstIdSanPham.add(s.getSanphamchitiet().getSanpham().getId());
        }
        Set<Integer> uniqueNumbers = new HashSet<>(lstIdSanPham);

        List<Integer> lstIdSanPhamKoLap = new ArrayList<>(uniqueNumbers);
        List<SanPham> lstSP= new ArrayList<>();
        for(Integer id: lstIdSanPhamKoLap){
            SanPham sp= sanPhamImp.findById(id);
            lstSP.add(sp);
        }
        return lstSP;
    }
    @GetMapping("/chi-tiet-san-pham-dot-giam")
    public List<SanPhamChiTiet> sanphamchitietdotgiam(HttpSession session){
        DotGiamGia dotGiamGia = (DotGiamGia) session.getAttribute("dotGG");
        List<SanPhamDotGiam> lstSPDG= sanPHamDotGiamImp.findSanPhamDotGiamByIdDotgiamgia(dotGiamGia.getId());

        List<SanPhamChiTiet> lstSPCT= new ArrayList<>();
        for(SanPhamDotGiam sp: lstSPDG){
            SanPhamChiTiet spct= sanPhamChiTietImp.findById(sp.getSanphamchitiet().getId());
            lstSPCT.add(spct);
        }
        return lstSPCT;
    }
}
