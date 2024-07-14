package com.example.demo.controller.NguoiDung;

import com.example.demo.entity.*;
import com.example.demo.info.DiaChiNVInfo;
import com.example.demo.info.NguoiDungNVInfo;
import com.example.demo.info.NhanVienInfo;
import com.example.demo.info.NhanVienSearch;
import com.example.demo.service.impl.DiaChiImpl;
import com.example.demo.service.impl.NguoiDungImpl1;
import com.example.demo.service.impl.NhanVienImpl;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public String list(Model model,@Valid @ModelAttribute("nds") NhanVienSearch nd,BindingResult ndBindingResult) {
        if(nd.getBatdau() == "" && nd.getKetthuc() == ""){
            List<DiaChi> page = diaChi.searchkey(nd);
            List<NhanVien> listnv = nhanVien.searchKey(nd);
            model.addAttribute("items1", page);
            model.addAttribute("items2", listnv);
            return "admin/qlnhanvien";
        }else if (nd.getBatdau() != "" && nd.getKetthuc() == ""){
            List<DiaChi> page = diaChi.searchStart(nd.getKey(),nd.isTrangThai(),Date.valueOf(nd.getBatdau()));
            List<NhanVien> listnv = nhanVien.searchStart(nd.getKey(),nd.isTrangThai(),Date.valueOf(nd.getBatdau()));
            model.addAttribute("items1", page);
            model.addAttribute("items2", listnv);
            return "admin/qlnhanvien";
        }else if (nd.getKetthuc() != "" && nd.getBatdau() == ""){
            List<DiaChi> page = diaChi.searchEnd(nd.getKey(),nd.isTrangThai(),Date.valueOf(nd.getKetthuc()));
            List<NhanVien> listnv = nhanVien.searchEnd(nd.getKey(),nd.isTrangThai(),Date.valueOf(nd.getKetthuc()));
            model.addAttribute("items1", page);
            model.addAttribute("items2", listnv);
            return "admin/qlnhanvien";
        }else if (nd.getKetthuc() != "" && nd.getBatdau() != "") {
            Date a = Date.valueOf(nd.getBatdau());
            Date b = Date.valueOf(nd.getKetthuc());
            if (!a.before(b)){
                ndBindingResult.rejectValue("ketthuc", "error.ketthuc", "Khoảng ngày không hợp lệ");
            }
            List<DiaChi> page = diaChi.searchND(nd.getKey(),nd.isTrangThai(), Date.valueOf(nd.getBatdau()), Date.valueOf(nd.getKetthuc()));
            List<NhanVien> listnv = nhanVien.searchND(nd.getKey(),nd.isTrangThai(), Date.valueOf(nd.getBatdau()), Date.valueOf(nd.getKetthuc()));
            model.addAttribute("items1", page);
            model.addAttribute("items2", listnv);
            return "admin/qlnhanvien";
        }
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
                          Model model, BindingResult result, Errors errors) {
        nd.setHovaten(nd.getHovaten().trim().replaceAll("\\s+", " "));
        nd.setEmail(nd.getEmail().trim().replaceAll("\\s+", ""));
        nd.setCccd(nd.getCccd().trim().replaceAll("\\s+", ""));
        nd.setSodienthoai(nd.getSodienthoai().trim().replaceAll("\\s+", ""));
        dc.setTenduong(dc.getTenduong().trim().replaceAll("\\s+", " "));
        String file = saveImage(anh);
        if(file != null){
            nd.setAnh(file);
        }
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
        model.addAttribute("nv",nhanVien.search(nd.getId()));
        return "admin/updatenhanvien";
    }
    @PostMapping("/updateNhanVien/{id}")
    public String update(@PathVariable Integer id, Model model,
                         @Valid  @ModelAttribute("nd") NguoiDungNVInfo nd,
                         BindingResult ndBindingResult,
                         @Valid  @ModelAttribute("dc") DiaChiNVInfo dc,
                         BindingResult dcBindingResult,
                         @ModelAttribute("nv") NhanVienInfo nv,
                         BindingResult nvBindingResult) {
//                         @RequestParam(name = "anh") MultipartFile anh) {
        if(nd.getNgaysinh() == null){
            ndBindingResult.rejectValue("ngaysinh", "error.ngaysinh", "Không được để trống ngày sinh");
            return "/admin/addnhanvien";
        }
        if(nd.getNgaysinh() != null){
            Calendar dob = Calendar.getInstance();
            dob.setTime(nd.getNgaysinh());
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (dob.after(today)) {
                ndBindingResult.rejectValue("ngaysinh", "error.ngaysinh", "Ngày sinh không được lớn hơn ngày hiện tại");
            }else if(age < 18 || age > 40){
                ndBindingResult.rejectValue("ngaysinh", "error.ngaysinh", "Nhân viên phải trên 18 tuổi");
            }
        }
        if (ndBindingResult.hasErrors() || dcBindingResult.hasErrors() || nvBindingResult.hasErrors()) {
            return "admin/updatenhanvien";
        }
//        System.out.println(anh);
//        if(nd.getAnh() == null){
//            if(anh != null){
//                String file = saveImage(anh);
//                nd.setAnh(file);
//        }}
        nd.setTrangthai(nv.getTrangthai());
        nguoiDung.update(nd,id);
        nhanVien.update(nv,id);
        diaChi.update(dc,id);
        return "redirect:/admin/qlnhanvien";
    }
}