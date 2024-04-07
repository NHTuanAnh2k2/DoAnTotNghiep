package com.example.demo.controller.sanphamchitiet;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamChiTietInfo;
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

    @GetMapping("/allSPCT")
    public String allSPCT(Model model, @ModelAttribute("search") SanPhamChiTietInfo info) {
        List<SanPhamChiTiet> list = null;
        if (info.getKey() == null && info.getIdChatLieu() == null && info.getIdDeGiay() == null && info.getIdKichCo() == null
                && info.getIdMauSac() == null && info.getIdThuongHieu() == null) {
            list = sanPhamChiTietRepository.findAll();
        } else {
            list = sanPhamChiTietRepository.search(
                    "%" + info.getKey() + "%",
                    info.getIdThuongHieu(),
                    info.getIdDeGiay(),
                    info.getIdKichCo(),
                    info.getIdMauSac(),
                    info.getIdChatLieu());
        }
        List<SanPham> listSanPham = sanPhamImp.findAll();
        List<ThuongHieu> listThuongHieu = thuongHieuImp.findAll();
        List<MauSac> listMauSac = mauSacImp.findAll();
        List<KichCo> listKichCo = kichCoImp.findAll();
        List<DeGiay> listDeGiay = deGiayImp.findAll();
        List<ChatLieu> listChatLieu = chatLieuImp.findAll();
        model.addAttribute("sp", listSanPham);
        model.addAttribute("th", listThuongHieu);
        model.addAttribute("ms", listMauSac);
        model.addAttribute("kc", listKichCo);
        model.addAttribute("dg", listDeGiay);
        model.addAttribute("cl", listChatLieu);

        model.addAttribute("listAllCTSP", list);
        return "admin/allchitietsanpham";
    }

    @GetMapping("/deleteCTSP/{id}")
    public String deleteCTSP(@PathVariable Integer id, Model model) {
        sanPhamChiTietImp.deleteSPCT(id);
        // Cập nhật lại danh sách sản phẩm chi tiết
        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findAll();
        model.addAttribute("sanphamchitiet", sanPhamChiTiets);
        return "redirect:/viewaddSP";
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

//    @Transactional
//    @PostMapping("/updateSPCTbyIdSanPham")
//    public String updateSanPhamChiTiet(@RequestParam("id") Integer id,
//                                       @RequestParam("soluong") Integer soluong,
//                                       @RequestParam("giatien") BigDecimal giatien) {
//        try {
//            sanPhamChiTietRepository.update(id, soluong, giatien);
//            return "redirect:/viewaddSP?success=1";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/viewaddSP?error=1";
//        }
//    }
}
