package com.example.demo.controller.khachhang;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.*;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.restcontroller.khachhang.KhachHangRestController;
import com.example.demo.restcontroller.khachhang.Province;
import com.example.demo.service.DiaChiService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import jakarta.validation.Valid;
import org.eclipse.tags.shaded.org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.*;

import java.io.IOException;

@Controller
@RequestMapping("khachhang")
public class KhachHangController {
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungService nguoiDungService;
    @Autowired
    DiaChiService diaChiService;
    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    public String display(Model model, @ModelAttribute("khachhang") KhachHang khachHang) {
        model.addAttribute("lstKhachHang", khachHangService.displayKhachHang());
        return "admin/qlkhachhang";
    }

    @GetMapping("/timkiem")
    public String search(@RequestParam(value = "searchInput", required = false) String tenSdtMa,
                         @RequestParam(value = "searchOption", required = false) Boolean trangThai,
                         Model model
    ) {
        List<KhachHangInfo> lstKhachHang = new ArrayList<>();
        if (tenSdtMa != null) {
            if (trangThai == null) {
                lstKhachHang = khachHangService.findByTenSdtMa(tenSdtMa);
            } else {
                // Nếu trangThai không null, tìm kiếm theo giá trị trangThai
                lstKhachHang = khachHangService.findByTenSdtMaTrangThai(tenSdtMa, trangThai);
            }
        }
        model.addAttribute("lstKhachHang", lstKhachHang);
        model.addAttribute("searchInput", tenSdtMa);
        model.addAttribute("searchOption", trangThai);
        return "admin/qlkhachhang";
    }

    @GetMapping("/add")
    public String form(@ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                       @ModelAttribute("diachi") DiaChiKHInfo diachi,
                       Model model
    ) {
        List<KhachHang> lstKh = khachHangService.findAll();
        List<String> lstEmail = new ArrayList<>();
        List<String> lstSdt = new ArrayList<>();
        List<String> lstCccd = new ArrayList<>();
        for (KhachHang kh : lstKh) {
            lstEmail.add(kh.getNguoidung().getEmail());
            lstSdt.add(kh.getNguoidung().getSodienthoai());
            lstCccd.add(kh.getNguoidung().getCccd());
        }

        model.addAttribute("lstEmail", lstEmail);
        model.addAttribute("lstSdt", lstSdt);
        model.addAttribute("lstCccd", lstCccd);
        return "admin/addkhachhang";
    }

    private String taoChuoiNgauNhien(int doDaiChuoi, String kiTu) {
        Random random = new Random();
        StringBuilder chuoiNgauNhien = new StringBuilder(doDaiChuoi);
        for (int i = 0; i < doDaiChuoi; i++) {
            chuoiNgauNhien.append(kiTu.charAt(random.nextInt(kiTu.length())));
        }
        return chuoiNgauNhien.toString();
    }

    public String generateUsername(String fullname) {
        // Chuyển đổi họ tên thành username theo quy ước
        String[] parts = fullname.trim().split("\\s+");
        StringBuilder username = new StringBuilder();

        // Lặp qua từng phần của họ tên từ phần cuối cùng đến phần đầu tiên
        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i];
            if (part.length() > 0) {
                // Lấy chữ cái đầu của từ và chuyển thành chữ thường
                if (i == parts.length - 1) {
                    // Nếu là từ cuối cùng, giữ nguyên
                    username.append(removeAccents(part.toLowerCase()));
                } else {
                    // Nếu là từ đầu tiên hoặc từ trung gian, lấy chữ cái đầu
                    username.append(part.substring(0, 1).toLowerCase());
                }
            }
        }

        return username.toString();
    }

    // Hàm xóa dấu tiếng Việt
    public static String removeAccents(String str) {
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        return temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }


    @PostMapping("/add")
    public String add(@ModelAttribute("khachhang") KhachHang khachhang,
                      @ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                      @ModelAttribute("diachi") DiaChiKHInfo diachi,
                      RedirectAttributes redirectAttributes,
                      Model model
    ) throws IOException {
        LocalDateTime currentDate = LocalDateTime.now();
        int passwordLength = 10;
        String username = generateUsername(nguoidung.getHovaten());
        String password = khachHangService.generateRandomPassword(passwordLength);

        String trimmedTenNguoiDung = (nguoidung.getHovaten() != null)
                ? nguoidung.getHovaten().trim().replaceAll("\\s+", " ")
                : null;

        NguoiDung nd = new NguoiDung();
        nd.setEmail(nguoidung.getEmail().trim());
        nd.setHovaten(trimmedTenNguoiDung);
        nd.setNgaysinh(nguoidung.getNgaysinh());
        nd.setCccd(nguoidung.getCccd().trim());
        nd.setSodienthoai(nguoidung.getSodienthoai().trim());
        nd.setGioitinh(nguoidung.getGioitinh());
        nd.setLancapnhatcuoi(Timestamp.valueOf(currentDate));
        nd.setTaikhoan(username);
        nd.setMatkhau(passwordEncoder.encode(password));
        nd.setNgaytao(Timestamp.valueOf(currentDate));
        nd.setLancapnhatcuoi(Timestamp.valueOf(currentDate));
        nd.setTrangthai(true);
        khachHangService.addNguoiDung(nd);

        DiaChi dc = new DiaChi();
        dc.setTinhthanhpho(diachi.getTinhthanhpho());
        dc.setQuanhuyen(diachi.getQuanhuyen());
        dc.setXaphuong(diachi.getXaphuong());
        dc.setTenduong(diachi.getTenduong().trim());
        dc.setNguoidung(nd);
        dc.setHotennguoinhan(nd.getHovaten());
        dc.setSdtnguoinhan(nd.getSodienthoai());
        dc.setTrangthai(nd.getTrangthai());
        dc.setNgaytao(nd.getNgaytao());
        dc.setLancapnhatcuoi(nd.getLancapnhatcuoi());
        khachHangService.addDiaChi(dc);

        KhachHang kh = new KhachHang();
        String maKH = "KH" + taoChuoiNgauNhien(7, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        kh.setMakhachhang(maKH);
        kh.setNguoidung(nd);
        kh.setTrangthai(nd.getTrangthai());
        kh.setNgaytao(nd.getNgaytao());
        kh.setLancapnhatcuoi(nd.getLancapnhatcuoi());
        khachHangService.addKhachHang(kh);

        khachHangService.sendEmail(nd.getEmail(), nd.getTaikhoan(), password, nd.getHovaten());

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/khachhang";
    }

    @GetMapping("/updatetrangthai/{id}")
    public String updateTrangThai(Model model,
                                  @PathVariable("id") Integer id,
                                  RedirectAttributes redirectAttributes) {
        DiaChi dc = khachHangService.findDiaChiById(id);
        NguoiDung nd = khachHangService.findNguoiDungById(dc.getNguoidung().getId());
        KhachHang kh = khachHangService.findKhachHangByIdNguoiDung(nd.getId());
        nd.setNguoicapnhat("ADMIN");
        nd.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        nd.setTrangthai(!nd.getTrangthai());
        khachHangService.updateNguoiDung(nd);
        kh.setNguoicapnhat("ADMIN");
        kh.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        kh.setTrangthai(!kh.getTrangthai());
        khachHangService.updateKhachHang(kh);

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/khachhang";
    }

    @GetMapping("/update/{id}")
    public String displayUpdate(@ModelAttribute("diachi") DiaChiKHInfo diaChi,
                                @ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                                @PathVariable("id") Integer id,
                                Model model) {

        DiaChi diaChiSelect = khachHangService.findDiaChiById(id);
        NguoiDung nguoiDungSelect = khachHangService.findNguoiDungById(diaChiSelect.getNguoidung().getId());

        List<KhachHang> lstKh = khachHangService.findAll();
        List<String> lstEmail = new ArrayList<>();
        List<String> lstSdt = new ArrayList<>();
        List<String> lstCccd = new ArrayList<>();
        for (KhachHang kh : lstKh) {
            lstEmail.add(kh.getNguoidung().getEmail());
            lstSdt.add(kh.getNguoidung().getSodienthoai());
            lstCccd.add(kh.getNguoidung().getCccd());
        }

        model.addAttribute("diachi", diaChiSelect);
        model.addAttribute("nguoidung", nguoiDungSelect);
        model.addAttribute("lstEmail", lstEmail);
        model.addAttribute("lstSdt", lstSdt);
        model.addAttribute("lstCccd", lstCccd);

        return "admin/updatekhachhang";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                         @ModelAttribute("diachi") DiaChiKHInfo diachi,
                         @PathVariable("id") Integer id,
                         RedirectAttributes redirectAttributes,
                         Model model
    ) {

        String trimmedTenNguoiDung = (nguoidung.getHovaten() != null)
                ? nguoidung.getHovaten().trim().replaceAll("\\s+", " ")
                : null;
        String trimmedTenDuong = (diachi.getTenduong() != null)
                ? diachi.getTenduong().trim().replaceAll("\\s+", " ")
                : null;

        DiaChi dc = khachHangService.findDiaChiById(id);
        dc.setTinhthanhpho(diachi.getTinhthanhpho());
        dc.setQuanhuyen(diachi.getQuanhuyen());
        dc.setXaphuong(diachi.getXaphuong());
        dc.setTenduong(trimmedTenDuong);
        dc.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        khachHangService.updateDiaChi(dc);

        NguoiDung nd = khachHangService.findNguoiDungById(dc.getNguoidung().getId());
        nd.setEmail(nguoidung.getEmail().trim());
        nd.setHovaten(trimmedTenNguoiDung);
        nd.setNgaysinh(nguoidung.getNgaysinh());
        nd.setCccd(nguoidung.getCccd().trim());
        nd.setSodienthoai(nguoidung.getSodienthoai().trim());
        nd.setGioitinh(nguoidung.getGioitinh());
        nd.setLancapnhatcuoi(dc.getLancapnhatcuoi());
        khachHangService.updateNguoiDung(nd);

        KhachHang kh = khachHangService.findKhachHangByIdNguoiDung(nd.getId());
        kh.setLancapnhatcuoi(dc.getLancapnhatcuoi());
        khachHangService.updateKhachHang(kh);

        model.addAttribute("lstKhachHang", khachHangService.displayKhachHang());
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/khachhang";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id,
                         @ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                         @ModelAttribute("khachhang") KhachHangKHInfo khachhang,
                         @ModelAttribute("diachi") DiaChiKHInfo diachi,
                         Model model
    ) {
        DiaChi diachiSelect = khachHangService.findDiaChiById(id);
        NguoiDung nguoidungSelect = khachHangService.findNguoiDungById(diachiSelect.getNguoidung().getId());
        KhachHang khachhangSelect = khachHangService.findKhachHangByIdNguoiDung(nguoidungSelect.getId());
        model.addAttribute("nguoidung", nguoidungSelect);
        model.addAttribute("diachi", diachiSelect);
        model.addAttribute("khachhang", khachhangSelect);

        return "admin/detailkhachhang";
    }

}
