package com.example.demo.controller.NguoiDung;

import com.example.demo.entity.*;
import com.example.demo.info.DiaChiNVInfo;
import com.example.demo.info.NguoiDungNVInfo;
import com.example.demo.info.NhanVienInfo;
import com.example.demo.info.NhanVienSearch;
import com.example.demo.service.impl.DiaChiImpl;
import com.example.demo.service.impl.NguoiDungImpl1;
import com.example.demo.service.impl.NhanVienImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class NhanVienController {
    @Autowired
    NhanVienImpl nhanVien;
    @Autowired
    DiaChiImpl diaChi;
    @Autowired
    NguoiDungImpl1 nguoiDung;


    @GetMapping("/admin/qlnhanvien")
    public String listnv(Model model,@ModelAttribute("nds") NhanVienSearch nd) {
        List<DiaChi> page = diaChi.getAll();
        List<NhanVien> listnv = nhanVien.getAll();
        model.addAttribute("items1", page);
        model.addAttribute("items2", listnv);
        return "admin/qlnhanvien";
    }
    @GetMapping("/admin/timkiem")
    public String list(@RequestParam(value = "searchInput", required = false) String tenSdt,
                       @RequestParam(value = "searchOption", required = false) String trangThai,
                       @RequestParam(value = "batdau", required = false) String batdau,
                       @RequestParam(value = "ketthuc", required = false) String ketthuc,
                       Model model) {
        List<DiaChi> page = new ArrayList<>();
        List<NhanVien> listnv = new ArrayList<>();
        String trimmedTen = (tenSdt != null)
                ? tenSdt.trim().replaceAll("\\s+", " ")
                : null;
        tenSdt = trimmedTen;
        if (tenSdt != null) {
            tenSdt = tenSdt.trim();
        }
        Boolean tt;
        if((trangThai != null && trangThai.equals("true")) || (trangThai != null && trangThai.equals("false"))){
            tt= Boolean.valueOf(trangThai);
        }else{
            tt = null;
        }

        if(batdau != "" && ketthuc != ""){
            if (tt == null){
                page = diaChi.searchNDs(tenSdt,Date.valueOf(batdau), Date.valueOf(ketthuc));
                listnv = nhanVien.searchNDs(tenSdt,Date.valueOf(batdau), Date.valueOf(ketthuc));
            }else {
                page = diaChi.searchND(tenSdt, tt, Date.valueOf(batdau), Date.valueOf(ketthuc));
                listnv = nhanVien.searchND(tenSdt, tt, Date.valueOf(batdau), Date.valueOf(ketthuc));
            }
        }else if(batdau == "" && ketthuc != ""){
            if (tt == null){
                page = diaChi.searchNDs(tenSdt,null, Date.valueOf(ketthuc));
                listnv = nhanVien.searchNDs(tenSdt,null, Date.valueOf(ketthuc));
            }else {
                page = diaChi.searchND(tenSdt, tt, null, Date.valueOf(ketthuc));
                listnv = nhanVien.searchND(tenSdt, tt, null, Date.valueOf(ketthuc));
            }

        }else if(batdau != "" && ketthuc == ""){
            if (tt == null){
                page = diaChi.searchNDs(tenSdt,Date.valueOf(batdau), null);
                listnv = nhanVien.searchNDs(tenSdt,Date.valueOf(batdau), null);
            }else {
                page = diaChi.searchND(tenSdt, tt, Date.valueOf(batdau), null);
                listnv = nhanVien.searchND(tenSdt, tt, Date.valueOf(batdau), null);
            }
        }else if(batdau == "" && ketthuc == ""){
            if (tt == null){
                page = diaChi.searchNDs(tenSdt,null, null);
                listnv = nhanVien.searchNDs(tenSdt,null, null);
            }else {
                page = diaChi.searchND(tenSdt, tt, null, null);
                listnv = nhanVien.searchND(tenSdt, tt, null, null);
            }
        }
        model.addAttribute("items1", page);
        model.addAttribute("items2", listnv);
        model.addAttribute("searchInput", tenSdt);
        model.addAttribute("searchOption", tt);
        model.addAttribute("batdau", batdau);
        model.addAttribute("kethuc", ketthuc);
        return "admin/qlnhanvien";
    }
    @GetMapping("/admin/addnhanvien")
    public String viewAdd(
                          @ModelAttribute("nd") NguoiDungNVInfo nd,
                          @ModelAttribute("dc") DiaChiNVInfo dc,
                          @ModelAttribute("nv") NhanVienInfo nv,
                          Model model, RedirectAttributes redirectAttributes) {
        List<DiaChi> page = diaChi.getAll();
        List<NhanVien> listnv = nhanVien.getAll();
        List<NguoiDung> listND = nguoiDung.getAll();
        model.addAttribute("items1", page);
        model.addAttribute("items2", listnv);
        model.addAttribute("list", listND);
        return "admin/addnhanvien";
    }
    @PostMapping("/admin/addnv")
    public String addSave(
            @Valid  @ModelAttribute("nd") NguoiDungNVInfo nd,
            BindingResult ndBindingResult,
            @Valid  @ModelAttribute("nv") NhanVienInfo nv,
            BindingResult nvBindingResult,
            @Valid  @ModelAttribute("dc") DiaChiNVInfo dc,
            BindingResult dcBindingResult,
            @RequestParam(name = "anh") MultipartFile anh,
            Model model, BindingResult result, Errors errors, HttpSession session) {
        Integer checkthem=0;
        String trimmedTenSanPham = (nd.getHovaten() != null)
                ? nd.getHovaten().trim().replaceAll("\\s+", " ")
                : null;
        nd.setHovaten(trimmedTenSanPham);
        nd.setEmail(nd.getEmail().trim().replaceAll("\\s+", ""));
        nd.setCccd(nd.getCccd().trim().replaceAll("\\s+", ""));
        nd.setSodienthoai(nd.getSodienthoai().trim().replaceAll("\\s+", ""));
        dc.setTenduong(dc.getTenduong().trim().replaceAll("\\s+", " "));
        String trimmedTenDuong = (dc.getTenduong() != null)
                ? dc.getTenduong().trim().replaceAll("\\s+", " ")
                : null;
        dc.setTenduong(trimmedTenDuong);
        String file = saveImage(anh);
        if(file != null){
            nd.setAnh(file);
        }
        List<NguoiDung> l = nguoiDung.getAll();
        String ten = trimmedTenSanPham;
        String[] cacTu = ten.split("\\s+");
        for (int i = 0; i < cacTu.length; i++) {
            cacTu[i] = Normalizer.normalize(cacTu[i], Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            cacTu[i] = cacTu[i].toLowerCase();
        }
        String tenCuoi = cacTu[cacTu.length - 1];
        String[] parts = ten.split("\\s+");
        StringBuilder chuoiMoi = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (!parts[i].isEmpty()) {
                chuoiMoi.append(parts[i].charAt(0));
            }
        }
        int s = l.size() + 1;
        tenCuoi = tenCuoi + chuoiMoi.toString().toLowerCase() + s;
        nd.setTaikhoan(tenCuoi);
        nguoiDung.add(nd);

        NguoiDung n = nguoiDung.search(nd.getSodienthoai());
        nv.setIdnguoidung(n);
        nhanVien.add(nv);
        dc.setIdnguoidung(n);
        diaChi.add(dc);
        String to = n.getEmail();
        String subject = "Chúc mừng đã trở thành nhân viên của T&T shop";
        String mailType = "";
        String mailContent = "Tài khoản của bạn là: " + n.getTaikhoan() +"\nMật khẩu của bạn là: "+ n.getMatkhau();
        nguoiDung.sendEmail(to, subject, mailType, mailContent);
        checkthem=1;
        session.setAttribute("themthanhcong",checkthem);
        return "redirect:/admin/qlnhanvien";
    }
    private String saveImage(MultipartFile file) {
        String uploadDir = "D:\\DATN\\src\\main\\resources\\static\\upload";
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String originalFileName = file.getOriginalFilename();
            String filePath = uploadDir + File.separator + originalFileName;
            File dest = new File(filePath);
            file.transferTo(dest);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/updateNhanVien/{id}")
    public String viewUpdate(@PathVariable Integer id, Model model,
                             @ModelAttribute("nd") NguoiDungNVInfo nd,
                             @ModelAttribute("nv") NhanVienInfo nv,
                             @ModelAttribute("dc") DiaChiNVInfo dc
                             ) {
        model.addAttribute("nd", nguoiDung.findById(id));
        model.addAttribute("dc",diaChi.search(id));
        model.addAttribute("nv",nhanVien.search(id));
        List<DiaChi> page = diaChi.getAll();
        List<NhanVien> listnv = nhanVien.getAll();
        List<NguoiDung> listND = nguoiDung.getAll();
        model.addAttribute("items1", page);
        model.addAttribute("items2", listnv);
        model.addAttribute("list", listND);
        return "admin/updatenhanvien";
    }
    @PostMapping("/updateNhanVien/{id}")
    public String update(@PathVariable Integer id, Model model,
                         @Valid  @ModelAttribute("nd") NguoiDungNVInfo nd,
                         BindingResult ndBindingResult,
                         @Valid  @ModelAttribute("dc") DiaChiNVInfo dc,
                         BindingResult dcBindingResult,
                         @ModelAttribute("nv") NhanVienInfo nv,
                         BindingResult nvBindingResult, HttpSession session) {
//                         @RequestParam(name = "anh") MultipartFile anh) {
        Integer checkcapnhat=0;
        String trimmedTenSanPham = (nd.getHovaten() != null)
                ? nd.getHovaten().trim().replaceAll("\\s+", " ")
                : null;
        nd.setHovaten(trimmedTenSanPham);
        nd.setEmail(nd.getEmail().trim().replaceAll("\\s+", ""));
        nd.setCccd(nd.getCccd().trim().replaceAll("\\s+", ""));
        nd.setSodienthoai(nd.getSodienthoai().trim().replaceAll("\\s+", ""));
        String trimmedTenDuong = (dc.getTenduong() != null)
                ? dc.getTenduong().trim().replaceAll("\\s+", " ")
                : null;
        dc.setTenduong(trimmedTenDuong);
//        System.out.println(anh);
//        if(nd.getAnh() == null){
//            if(anh != null){
//                String file = saveImage(anh);
//                nd.setAnh(file);
//        }}
        nd.setTrangthai(nv.getTrangthai());
        dc.setTrangthai(nv.getTrangthai());
        nguoiDung.update(nd,id);
        nhanVien.update(nv,id);
        diaChi.update(dc,id);
        checkcapnhat=1;
        session.setAttribute("capnhatthanhcong",checkcapnhat);
        return "redirect:/admin/qlnhanvien";
    }
    @GetMapping("/updatetrangthai/{id}")
    public String updateTrangThai(Model model,
                                  @PathVariable("id") Integer id,
                                  RedirectAttributes redirectAttributes) {
        DiaChi dc = diaChi.search(id);
        NguoiDung nd = nguoiDung.findById(id);
        NhanVien nv = nhanVien.search(id);
        nd.setNguoicapnhat("ADMIN");
        nd.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        nd.setTrangthai(!nd.getTrangthai());
        nguoiDung.updateS(nd);
        nv.setNguoicapnhat("ADMIN");
        nv.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        nv.setTrangthai(!nv.getTrangthai());
        nhanVien.updateS(nv);
        dc.setNguoicapnhat("ADMIN");
        dc.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        dc.setTrangthai(!nv.getTrangthai());
        diaChi.updateS(dc);

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/admin/qlnhanvien";
    }
    @GetMapping("/updatevaitro/{id}")
    public String updateVaiTro(Model model,
                                  @PathVariable("id") Integer id,
                                  RedirectAttributes redirectAttributes) {
        DiaChi dc = diaChi.search(id);
        NguoiDung nd = nguoiDung.findById(id);
        NhanVien nv = nhanVien.search(id);
        nd.setNguoicapnhat("ADMIN");
        nd.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        nd.setTrangthai(true);
        nguoiDung.updateS(nd);
        nv.setNguoicapnhat("ADMIN");
        nv.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        nv.setTrangthai(true);
        nv.setVaitro(true);
        nhanVien.updateS(nv);
        dc.setNguoicapnhat("ADMIN");
        dc.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        dc.setTrangthai(true);
        diaChi.updateS(dc);
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/admin/qlnhanvien";
    }
}