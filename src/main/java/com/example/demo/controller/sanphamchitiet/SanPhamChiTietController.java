package com.example.demo.controller.sanphamchitiet;

import com.example.demo.entity.*;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
public class SanPhamChiTietController {
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;
    @Autowired
    SanPhamImp sanPhamImp;
    @Autowired
    ThuongHieuImp thuongHieuImp;

    @Autowired
    MauSacImp mauSacImp;

    @Autowired
    KichCoImp kichCoImp;

    @Autowired
    DeGiayImp deGiayImp;

    @Autowired
    ChatLieuImp chatLieuImp;

    @Autowired
    AnhImp anhImp;

    @GetMapping("/deleteCTSP/{id}")
    public String deleteCTSP(@PathVariable Integer id, Model model) {
        sanPhamChiTietImp.deleteSPCT(id);
        SanPham sanPham = new SanPham();
        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findBySanPhamId(sanPham.getId());
        model.addAttribute("sanphamchitiet", sanPhamChiTiets);
        return "forward:/viewaddSP";
    }


    @GetMapping("/updateCTSP/{id}")
    public String viewupdateCTSP(@PathVariable Integer id, Model model, @RequestParam(defaultValue = "0") int p, @ModelAttribute("thuonghieu") ThuongHieu thuongHieu,
                                 @ModelAttribute("chatlieu") ChatLieu chatLieu,
                                 @ModelAttribute("kichco") KichCo kichCo,
                                 @ModelAttribute("degiay") DeGiay deGiay,
                                 @ModelAttribute("mausac") MauSac mauSac
    ) {
        List<SanPham> listSanPham = sanPhamImp.findAll();
        List<SanPhamChiTiet> listSPCT = sanPhamChiTietImp.findAll();
        List<ThuongHieu> listThuongHieu = thuongHieuImp.findAll();
        List<MauSac> listMauSac = mauSacImp.findAll();
        List<KichCo> listKichCo = kichCoImp.findAll();
        List<DeGiay> listDeGiay = deGiayImp.findAll();
        List<ChatLieu> listChatLieu = chatLieuImp.findAll();
        List<Anh> listAnh = anhImp.findAll();
        model.addAttribute("sp", listSanPham);
        model.addAttribute("spct", listSPCT);
        model.addAttribute("th", listThuongHieu);
        model.addAttribute("ms", listMauSac);
        model.addAttribute("kc", listKichCo);
        model.addAttribute("dg", listDeGiay);
        model.addAttribute("cl", listChatLieu);
        model.addAttribute("a", listAnh);
        Pageable pageable = PageRequest.of(p, 20);
        Page<SanPhamChiTiet> page = sanPhamChiTietImp.finAllPage(pageable);
        model.addAttribute("page", page);
        model.addAttribute("hehe", sanPhamChiTietImp.findById(id));
        return "admin/detailCTSP";
    }

    @PostMapping("/updateCTSP/{id}")
    public String updateCTSP(@PathVariable Integer id,@ModelAttribute("hehe") SanPhamChiTiet sanPhamChiTiet) {
       sanPhamChiTiet.setId(id);
       sanPhamChiTietImp.addSPCT(sanPhamChiTiet);
        return "redirect:/admin/qlchitietsanpham";
    }

}
