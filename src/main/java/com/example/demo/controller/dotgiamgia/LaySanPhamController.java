package com.example.demo.controller.dotgiamgia;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.impl.SanPhamChiTietImp;
import com.example.demo.service.impl.SanPhamImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LaySanPhamController {
    @Autowired
    SanPhamImp sanPhamImp;
    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;

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
}
