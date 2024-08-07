package com.example.demo.controller.mausac;

import com.example.demo.entity.MauSac;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.service.impl.MauSacImp;
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
public class MauSacController {
    @Autowired
    MauSacImp mauSacImp;
    @Autowired
    MauSacRepository mauSacRepository;

    @Autowired
    NhanVienRepository nhanvienRPo;

    @Autowired
    NguoiDungRepository daoNguoiDung;

    @GetMapping("/listMauSac")
    public String listMauSac(@RequestParam(defaultValue = "0") int p, @ModelAttribute("mausac") MauSac mauSac, @ModelAttribute("tim") ThuocTinhInfo info, Model model) {
        List<MauSac> page;
        String trimmedKey = (info.getKey() != null) ? info.getKey().trim().replaceAll("\\s+", " ") : null;
        boolean isKeyEmpty = (trimmedKey == null || trimmedKey.isEmpty());
        boolean isTrangthaiNull = (info.getTrangthai() == null);

        if (isKeyEmpty && isTrangthaiNull) {
            page = mauSacRepository.findAllByOrderByNgaytaoDesc();
        } else {
            page = mauSacRepository.findByTenAndTrangthai("%" + trimmedKey + "%", info.getTrangthai());
        }
        model.addAttribute("fillSearch", info.getKey());
        model.addAttribute("fillTrangThai", info.getTrangthai());
        model.addAttribute("list", page);
        return "admin/qlmausac";
    }

    @PostMapping("/mausac/updateTrangThai/{id}")
    public String updateTrangThaiMauSac(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        MauSac existingMauSac = mauSacRepository.findById(id).orElse(null);
        if (existingMauSac != null) {
            existingMauSac.setTrangthai(!existingMauSac.getTrangthai());
            mauSacRepository.save(existingMauSac);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        }
        return "redirect:/listMauSac";
    }

    @PostMapping("/addSaveMauSac")
    @CacheEvict(value = "mausacCache", allEntries = true)
    public String addSave(@ModelAttribute("mausac") MauSac mauSac, @ModelAttribute("ms") ThuocTinhInfo info, Model model, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        String trimmedTenMauSac = (mauSac.getTen() != null)
                ? mauSac.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        mauSac.setTen(trimmedTenMauSac);
        mauSac.setTrangthai(true);
        mauSac.setNgaytao(currentTime);
        mauSac.setLancapnhatcuoi(currentTime);
        mauSac.setNguoitao(nv.getNguoidung().getHovaten());
        mauSac.setNguoicapnhat(nv.getNguoidung().getHovaten());
        mauSacRepository.save(mauSac);
        return "redirect:/listMauSac";
    }

    @PostMapping("/addMauSacModal")
    public String addMauSacModal(@ModelAttribute("mausac") MauSac mauSac, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        String trimmedTenMauSac = (mauSac.getTen() != null)
                ? mauSac.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        mauSac.setTen(trimmedTenMauSac);
        mauSac.setTrangthai(true);
        mauSac.setNgaytao(currentTime);
        mauSac.setLancapnhatcuoi(currentTime);
        mauSac.setNguoitao(nv.getNguoidung().getHovaten());
        mauSac.setNguoicapnhat(nv.getNguoidung().getHovaten());
        mauSacImp.addMauSac(mauSac);
        return "redirect:/viewaddSPGET";
    }

    @PostMapping("/addMauSacSua")
    public String addMauSacSua(@ModelAttribute("mausac") MauSac mauSac, @RequestParam("spctId") Integer spctId, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        String trimmedTenMauSac = (mauSac.getTen() != null)
                ? mauSac.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        mauSac.setTen(trimmedTenMauSac);
        mauSac.setTrangthai(true);
        mauSac.setNgaytao(currentTime);
        mauSac.setLancapnhatcuoi(currentTime);
        mauSac.setNguoitao(nv.getNguoidung().getHovaten());
        mauSac.setNguoicapnhat(nv.getNguoidung().getHovaten());
        mauSacImp.addMauSac(mauSac);
        return "redirect:/updateCTSP/" + spctId;
    }
}
