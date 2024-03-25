package com.example.demo.controller.sanphamchitiet;

import com.example.demo.entity.*;
import com.example.demo.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

///
@Controller
public class SanPhamChiTietController {
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

//    @GetMapping("/listspct")
//    public String hienthi(Model model, @RequestParam(defaultValue = "0") int p) {
//        Pageable pageable= PageRequest.of(p,5);
//        Page<SanPhamChiTiet> page=sanPhamChiTietImp.finAllPage(pageable);
//        model.addAttribute("page",page);
//        return "admin/addsanpham";
//    }

    @GetMapping("/deleteCTSP/{id}")
    public String deleteCTSP(@PathVariable Integer id) {
        sanPhamChiTietImp.deleteSPCT(id);
        return "redirect:/viewaddSP";
    }

//    @PostMapping("/sanphamchitiet/update")
//    public String updateSoLuongVaGiaTien(@RequestBody List<ProductUpdateRequest> updateRequests) {
//        List<Integer> ids = updateRequests.stream().map(ProductUpdateRequest::getId).collect(Collectors.toList());
//        for (ProductUpdateRequest updateRequest : updateRequests) {
//            sanPhamChiTietImp.updateSoLuongVaGiaTien(ids, updateRequest.getSoluong(), updateRequest.getGiatien());
//        }
//        return "redirect:/viewaddSP";
//    }

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

        return "admin/addsanpham";
    }

    @PostMapping("/updateCTSP/{id}")
    public String updateCTSP(@PathVariable Integer id, @RequestParam("soluong") Integer soluong,
                             @RequestParam("giatien") BigDecimal giatien,
                             @ModelAttribute("sanphamchitiet") SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTietImp.update(id, soluong, giatien);
        return "redirect:/viewaddSP";
    }

}
