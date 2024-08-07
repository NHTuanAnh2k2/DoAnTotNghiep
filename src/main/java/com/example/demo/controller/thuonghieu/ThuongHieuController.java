package com.example.demo.controller.thuonghieu;

import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.repository.ThuongHieuRepository;
import com.example.demo.service.impl.ThuongHieuImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ThuongHieuController {
    @Autowired
    ThuongHieuRepository thuongHieuRepository;

    @Autowired
    ThuongHieuImp thuongHieuImp;

    @Autowired
    NhanVienRepository nhanvienRPo;

    @Autowired
    NguoiDungRepository daoNguoiDung;

    @GetMapping("/listthuonghieu")
    public String hienthi(@RequestParam(defaultValue = "0") int p, Model model, @ModelAttribute("thuonghieu") ThuongHieu thuongHieu,@ModelAttribute("tim") ThuocTinhInfo info) {
        List<ThuongHieu> list;
        String trimmedKey = (info.getKey() != null) ? info.getKey().trim().replaceAll("\\s+", " ") : null;
        boolean isKeyEmpty = (trimmedKey == null || trimmedKey.isEmpty());
        boolean isTrangthaiNull = (info.getTrangthai() == null);
        if (isKeyEmpty && isTrangthaiNull) {
            list = thuongHieuRepository.findAllByOrderByNgaytaoDesc();
        } else {
            list = thuongHieuRepository.findByTenAndTrangthai("%" + trimmedKey + "%", info.getTrangthai());
        }
        model.addAttribute("page", list);
        model.addAttribute("fillSearch", info.getKey());
        model.addAttribute("fillTrangThai", info.getTrangthai());
        return "admin/qlthuonghieu";
    }

    @PostMapping("/thuonghieu/updateTrangThai/{id}")
    public String updateTrangThaiThuongHieu(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        ThuongHieu existingThuongHieu = thuongHieuRepository.findById(id).orElse(null);
        if (existingThuongHieu != null) {
            existingThuongHieu.setTrangthai(!existingThuongHieu.getTrangthai());
            thuongHieuRepository.save(existingThuongHieu);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        }
        return "redirect:/listthuonghieu";
    }

    @PostMapping("/addSaveThuongHieu")
    @CacheEvict(value = "thuonghieuCache", allEntries = true)
    public String addSave(@ModelAttribute("thuonghieu") ThuongHieu thuongHieu, @ModelAttribute("th") ThuocTinhInfo info, Model model, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        if (thuongHieuRepository.existsByTen(thuongHieu.getTen())) {
            model.addAttribute("errThuongHieu", "Tên thương hiệu trùng!");
            return "admin/qlthuonghieu";
        }
        String trimmedTenThuongHieu = (thuongHieu.getTen() != null)
                ? thuongHieu.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        thuongHieu.setTen(trimmedTenThuongHieu);
        thuongHieu.setTrangthai(true);
        thuongHieu.setNgaytao(currentTime);
        thuongHieu.setLancapnhatcuoi(currentTime);
        thuongHieu.setNguoitao(nv.getNguoidung().getHovaten());
        thuongHieu.setNguoicapnhat(nv.getNguoidung().getHovaten());
        thuongHieuImp.add(thuongHieu);
        return "redirect:/listthuonghieu";
    }


    @PostMapping("/addThuongHieuModal")
    public String addThuongHieuModal(@ModelAttribute("thuonghieu") ThuongHieu thuongHieu, @ModelAttribute("th") ThuocTinhInfo info,HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        String trimmedTenThuongHieu = (thuongHieu.getTen() != null)
                ? thuongHieu.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        thuongHieu.setTen(trimmedTenThuongHieu);
        thuongHieu.setTrangthai(true);
        thuongHieu.setNgaytao(currentTime);
        thuongHieu.setLancapnhatcuoi(currentTime);
        thuongHieu.setNguoitao(nv.getNguoidung().getHovaten());
        thuongHieu.setNguoicapnhat(nv.getNguoidung().getHovaten());
        thuongHieuImp.add(thuongHieu);
        return "redirect:/viewaddSPGET";
    }

    @PostMapping("/addThuongHieuSua")
    public String addThuongHieuSua(@ModelAttribute("thuonghieu") ThuongHieu thuongHieu, @ModelAttribute("th") ThuocTinhInfo info, @RequestParam("spctId") Integer spctId, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        String trimmedTenThuongHieu = (thuongHieu.getTen() != null)
                ? thuongHieu.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        thuongHieu.setTen(trimmedTenThuongHieu);
        thuongHieu.setTrangthai(true);
        thuongHieu.setNgaytao(currentTime);
        thuongHieu.setLancapnhatcuoi(currentTime);
        thuongHieu.setNguoitao(nv.getNguoidung().getHovaten());
        thuongHieu.setNguoicapnhat(nv.getNguoidung().getHovaten());
        thuongHieuImp.add(thuongHieu);
        return "redirect:/updateCTSP/" + spctId;
    }
}
