package com.example.demo.controller.phieugiamgia;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PhieuGiamGiaController {
    @GetMapping("/admin/qldotgiamgia")
    public String qldotgiamgia() {
        return "/admin/qldotgiamgia";
    }
    @GetMapping("/admin/adddotgiamgia")
    public String adddotgiamgia() {
        return "/admin/adddotgiamgia";
    }
    @GetMapping("/admin/qlphieugiamgia")
    public String qlphieugiamgia() {
        return "/admin/qlphieugiamgia";
    }
}
