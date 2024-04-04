package com.example.demo.controller.sanphamchitiet;

import com.example.demo.entity.*;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

///
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
        // Cập nhật lại danh sách sản phẩm chi tiết
        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findAll();
        model.addAttribute("sanphamchitiet", sanPhamChiTiets);
        return "redirect:/viewaddSP"; // Chuyển hướng đến trang hiển thị danh sách sản phẩm chi tiết
    }


    @GetMapping("/updateCTSP/{id}")
    public String viewupdateCTSP(@PathVariable Integer id, Model model) {
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
        model.addAttribute("hehe", sanPhamChiTietImp.findById(id));
        return "admin/detailCTSP";
    }

    @PostMapping("/updateCTSP/{id}")
    public String updateCTSP(@PathVariable Integer id, @ModelAttribute("hehe") SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTiet.setId(id);
        sanPhamChiTietImp.addSPCT(sanPhamChiTiet);
        return "redirect:/admin/qlchitietsanpham";
    }
    @Transactional
    @PostMapping("/hehe/hihi")
    public String updateSanPhamChiTiet(@RequestParam("idSanPham") Integer idSanPham,
                                       @RequestParam("soLuong") List<Integer> soLuongList,
                                       @RequestParam("giaTien") List<BigDecimal> giaTienList,
                                       RedirectAttributes redirectAttributes) {
        for (int i = 0; i < soLuongList.size(); i++) {
            Integer soLuong = soLuongList.get(i);
            BigDecimal giaTien = giaTienList.get(i);
            sanPhamChiTietRepository.updateByIdSanPham(idSanPham, soLuong, giaTien);
        }
        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");
        return "forward:/viewaddSP";
    }

}
