package com.example.demo.controller.kichco;

import com.example.demo.entity.KichCo;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.KichCoRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.service.impl.KichCoImp;
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
public class KichCoController {
    @Autowired
    KichCoImp kichCoImp;

    @Autowired
    KichCoRepository kichCoRepository;

    @Autowired
    NhanVienRepository nhanvienRPo;

    @Autowired
    NguoiDungRepository daoNguoiDung;

    @GetMapping("/listKichCo")
    public String listKichCo(Model model, @ModelAttribute("kichco") KichCo kichCo, @ModelAttribute("tim") ThuocTinhInfo info) {
        List<KichCo> list;
        String trimmedKey = (info.getKey() != null) ? info.getKey().trim().replaceAll("\\s+", " ") : null;
        boolean isKeyEmpty = (trimmedKey == null || trimmedKey.isEmpty());
        boolean isTrangthaiNull = (info.getTrangthai() == null);

        if (isKeyEmpty && isTrangthaiNull) {
            list = kichCoRepository.findAllByOrderByNgaytaoDesc();
        } else {
            list = kichCoRepository.findByTenAndTrangthai("%" + trimmedKey + "%", info.getTrangthai());
        }
        model.addAttribute("fillSearch", info.getKey());
        model.addAttribute("fillTrangThai", info.getTrangthai());
        model.addAttribute("list", list);
        return "admin/qlkichco";
    }
    @PostMapping("/kichco/updateTrangThai/{id}")
    public String updateTrangThaiKichCo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        KichCo existingKichCo = kichCoRepository.findById(id).orElse(null);
        if (existingKichCo != null) {
            existingKichCo.setTrangthai(!existingKichCo.getTrangthai());
            kichCoRepository.save(existingKichCo);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        }
        return "redirect:/listKichCo";
    }


    @PostMapping("/updateKichCo/{id}")
    public String updateKichCo(@PathVariable Integer id) {
        kichCoRepository.updateTrangThaiToFalseById(id);
        return "redirect:/listKichCo";
    }

    @PostMapping("/addSaveKichCo")
    @CacheEvict(value = "kichcoCache", allEntries = true)
    public String addSave(@ModelAttribute("kichco") KichCo kichCo, @ModelAttribute("kc") ThuocTinhInfo info, Model model, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        String trimmedTenKichCo = (kichCo.getTen() != null)
                ? kichCo.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        kichCo.setTen(trimmedTenKichCo);
        kichCo.setTrangthai(true);
        kichCo.setNgaytao(currentTime);
        kichCo.setLancapnhatcuoi(currentTime);
        kichCo.setNguoitao(nv.getNguoidung().getHovaten());
        kichCo.setNguoicapnhat(nv.getNguoidung().getHovaten());
        kichCoRepository.save(kichCo);
        return "redirect:/listKichCo";
    }

    @PostMapping("/addKichCoModal")
    public String addKichCoModal(@ModelAttribute("kichco") KichCo kichCo, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        String trimmedTenKichCo = (kichCo.getTen() != null)
                ? kichCo.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        kichCo.setTen(trimmedTenKichCo);
        kichCo.setTrangthai(true);
        kichCo.setNgaytao(currentTime);
        kichCo.setLancapnhatcuoi(currentTime);
        kichCo.setNguoitao(nv.getNguoidung().getHovaten());
        kichCo.setNguoicapnhat(nv.getNguoidung().getHovaten());
        kichCoImp.addKichCo(kichCo);
        return "redirect:/viewaddSPGET";
    }

    @PostMapping("/addKichCoSua")
    public String addKichCoSua(@ModelAttribute("kichco") KichCo kichCo, @RequestParam("spctId") Integer spctId, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        String trimmedTenKichCo = (kichCo.getTen() != null)
                ? kichCo.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        kichCo.setTen(trimmedTenKichCo);
        kichCo.setTrangthai(true);
        kichCo.setNgaytao(currentTime);
        kichCo.setLancapnhatcuoi(currentTime);
        kichCo.setNguoitao(nv.getNguoidung().getHovaten());
        kichCo.setNguoicapnhat(nv.getNguoidung().getHovaten());
        kichCoImp.addKichCo(kichCo);
        return "redirect:/updateCTSP/" + spctId;
    }

    @PostMapping("/addKichCoSuaAll")
    public String addKichCoSuaAll(@ModelAttribute("kichco") KichCo kichCo, @RequestParam("spctId") Integer spctId, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        String trimmedTenKichCo = (kichCo.getTen() != null)
                ? kichCo.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        kichCo.setTen(trimmedTenKichCo);
        kichCo.setTrangthai(true);
        kichCo.setNgaytao(currentTime);
        kichCo.setLancapnhatcuoi(currentTime);
        kichCo.setNguoitao(nv.getNguoidung().getHovaten());
        kichCo.setNguoicapnhat(nv.getNguoidung().getHovaten());
        kichCoImp.addKichCo(kichCo);
        return "redirect:/updateAllCTSP/" + spctId;
    }
}
