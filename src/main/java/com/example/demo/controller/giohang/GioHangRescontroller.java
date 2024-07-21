package com.example.demo.controller.giohang;

import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class GioHangRescontroller {

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping("/giohangchitietsssss")
    public List<GioHangChiTiet> giohangchitietsssss(HttpSession session) {
        List<GioHangChiTiet> list = (List<GioHangChiTiet>) session.getAttribute("giohangchitiet");
        return list;
    }


    @GetMapping("/findBySPCTById/{Id}")
    public SanPhamChiTiet getProductDetails(@PathVariable Integer Id) {
        SanPhamChiTiet lst = sanPhamChiTietRepository.findByIdSPCT(Id);
        return lst;
    }

}
