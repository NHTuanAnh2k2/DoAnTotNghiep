package com.example.demo.controller.sanpham;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamInfo;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

///
@Controller
public class SanPhamController {

    @Autowired
    SanPhamImp sanPhamImp;

    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;

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

    @Autowired
    HttpServletRequest request;


    @GetMapping("/listsanpham")
    public String hienthi(@RequestParam(defaultValue = "0") int p, @ModelAttribute("tim") SanPhamInfo info, Model model) {
        Pageable pageable = PageRequest.of(p, 20);
        Page<SanPham> page = null;
        if (info.getKey() != null) {
            page = sanPhamImp.findAllByTensanphamOrTrangthai(info.getKey(), info.getTrangthai(), pageable);
        } else {
            page = sanPhamImp.getAll(pageable);
        }
        model.addAttribute("page", page);
        return "admin/qlsanpham";
    }

    @GetMapping("/viewaddSP")
    public String viewaddSP(Model model, @RequestParam(defaultValue = "0") int p) {
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
        return "admin/addsanpham";
    }

    @PostMapping("/addProduct")
    public String addProduct(Model model, @RequestParam String tensp,
                             @RequestParam Integer soluong,
                             @RequestParam BigDecimal giatien,
                             @RequestParam String mota,
                             @RequestParam Boolean trangthai,
                             @RequestParam ThuongHieu idThuongHieu,
                             @RequestParam ChatLieu idChatLieu,
                             @RequestParam Boolean gioitinh,
                             @RequestParam List<KichCo> idKichCo,
                             @RequestParam DeGiay idDeGiay,
                             @RequestParam List<MauSac> idMauSac
                          ) {
        SanPham sanPham = new SanPham();
        sanPham.setTensanpham(tensp);
        sanPham.setTrangthai(trangthai);
        sanPhamImp.add(sanPham);
        for (MauSac colorId : idMauSac) {
            for (KichCo sizeId : idKichCo) {
                SanPhamChiTiet spct = new SanPhamChiTiet();
                spct.setSanpham(sanPham);
                spct.setSoluong(soluong);
                spct.setGiatien(giatien);
                spct.setMota(mota);
                spct.setThuonghieu(idThuongHieu);
                spct.setChatlieu(idChatLieu);
                spct.setGioitinh(gioitinh);
                spct.setKichco(sizeId);
                spct.setDegiay(idDeGiay);
                spct.setMausac(colorId);
                sanPhamChiTietImp.addSPCT(spct);
            }
        }
        return "redirect:/viewaddSP";
    }
}
