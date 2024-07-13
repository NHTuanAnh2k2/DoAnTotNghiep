package com.example.demo.controller;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.NguoiDungKHInfo;
import com.example.demo.info.hosokhachhang.DoiMatKhauNguoiDung;
import com.example.demo.info.hosokhachhang.UpdateDiaChi;
import com.example.demo.info.hosokhachhang.UpdateKhachHang;
import com.example.demo.info.hosokhachhang.UpdateNguoiDung;
import com.example.demo.info.token.UserManager;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.restcontroller.khachhang.Province;
import com.example.demo.security.JWTGenerator;
import com.example.demo.service.KhachHangService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Controller
public class CustomerController {

    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    DiaChiRepository diaChiRepository;
    @Autowired
    UserManager userManager;


    @GetMapping("/trangchu")
    public String trangchu() {
        return "customer/trangchu";
    }

    @GetMapping("/modal")
    public String modal() {
        return "customer/modal";
    }

    @GetMapping("/quenmatkhau")
    public String quenmatkhauview(@ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                                  Model model) {

        return "same/quenmatkhau";
    }


    @GetMapping("/hosokhachhang/{username}")
    public String hosokhachhang(Model model,
                                @PathVariable("username") String username,
                                HttpSession session
    ) {
        String userDangnhap = (String) session.getAttribute("userDangnhap");
        if (username != null) {
            if (userDangnhap != null) {
                NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(userDangnhap);
                DiaChi dc = diaChiRepository.findDiaChiByTrangthai(nd.getId(), true);
                List<DiaChi> lstDiaChi = diaChiRepository.findDiaChiByIdNd(nd.getId());
                model.addAttribute("nguoidung", nd);
                model.addAttribute("diachi", dc);
                model.addAttribute("lstDiaChi", lstDiaChi);
                model.addAttribute("userDangnhap", userDangnhap);
                model.addAttribute("nguoidungdoimatkhau", new DoiMatKhauNguoiDung());
                model.addAttribute("addDiaChi", new UpdateDiaChi());
                model.addAttribute("updateDiaChi", dc);
                System.out.println("Danh sách người dùng đang đăng nhập: " + userManager.getLoggedInUsers());
                return "customer/hosokhachhang";
            } else {
                return "redirect:/account";
            }
        } else {
            return "redirect:/account";
        }
    }

    @PostMapping("/capnhattaikhoan/{username}")
    public String capnhattaikhoan(Model model,
                                  @PathVariable("username") String username,
                                  @ModelAttribute("nguoidung") UpdateNguoiDung nguoidung,
                                  @ModelAttribute("diachi") UpdateDiaChi diachi,
                                  @ModelAttribute("khachhang") UpdateKhachHang khachhang

    ) {

        NguoiDung userSelect = khachHangService.findNguoiDungByTaikhoan(username);
        int userId = userSelect.getId();

        NguoiDung nd = khachHangService.findNguoiDungById(userId);
        System.out.println("Nguoi dung: " + nd);
        nd.setHovaten(nguoidung.getHovaten());
        nd.setEmail(nguoidung.getEmail());
        nd.setNgaysinh(nguoidung.getNgaysinh());
        nd.setSodienthoai(nguoidung.getSodienthoai());
        nd.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        nd.setNguoicapnhat("CUSTOMER");
        khachHangService.updateNguoiDung(nd);
        System.out.println("updateND ok");

        return "redirect:/hosokhachhang/{username}";
    }

    @PostMapping("/themdiachi/{username}")
    public String themdiachi(Model model,
                             @PathVariable("username") String username,
                             @Valid @ModelAttribute("addDiaChi") UpdateDiaChi diachi,
                             BindingResult dcBindingResult,
                             RedirectAttributes redirectAttributes

    ) {
        if (dcBindingResult.hasErrors()) {
            List<ObjectError> lstErrorDc = dcBindingResult.getAllErrors();
            System.out.println("List Error DiaChi: " + lstErrorDc);
            return "customer/hosokhachhang";
        }

        NguoiDung userSelect = khachHangService.findNguoiDungByTaikhoan(username);

        DiaChi dc = new DiaChi();
        dc.setNguoidung(userSelect);
        dc.setTinhthanhpho(diachi.getTinhthanhpho());
        dc.setQuanhuyen(diachi.getQuanhuyen());
        dc.setXaphuong(diachi.getXaphuong());
        dc.setTenduong(diachi.getTenduong());
        dc.setSdtnguoinhan(diachi.getSdtnguoinhan());
        dc.setHotennguoinhan(diachi.getHotennguoinhan());
        dc.setNguoicapnhat("CUSTOMER");
        dc.setNguoitao("CUSTOMER");
        dc.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        dc.setNgaytao(Timestamp.valueOf(LocalDateTime.now()));
        dc.setTrangthai(diachi.isTrangthai());
        khachHangService.addDiaChi(dc);

        return "redirect:/hosokhachhang/{username}";
    }

    @PostMapping("/suadiachi/{username}")
    public String suadiachi (Model model,
                             @PathVariable("username") String username,
                             @Valid @ModelAttribute("updateDiaChi") UpdateDiaChi diachi,
                             BindingResult dcBindingResult,
                             RedirectAttributes redirectAttributes

    ) {
        if (dcBindingResult.hasErrors()) {
            List<ObjectError> lstErrorDc = dcBindingResult.getAllErrors();
            System.out.println("List Error DiaChi: " + lstErrorDc);
            return "customer/hosokhachhang";
        }

        NguoiDung userSelect = khachHangService.findNguoiDungByTaikhoan(username);

        DiaChi dc = diaChiRepository.findDiaChiByTrangthai(userSelect.getId(), true);
        dc.setTinhthanhpho(diachi.getTinhthanhpho());
        dc.setQuanhuyen(diachi.getQuanhuyen());
        dc.setXaphuong(diachi.getXaphuong());
        dc.setTenduong(diachi.getTenduong());
        dc.setSdtnguoinhan(diachi.getSdtnguoinhan());
        dc.setHotennguoinhan(diachi.getHotennguoinhan());
        dc.setNguoicapnhat("CUSTOMER");
        dc.setNguoitao("CUSTOMER");
        dc.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        khachHangService.addDiaChi(dc);

        return "redirect:/hosokhachhang/{username}";
    }

    @GetMapping("/thietlapmacdinh/{username}")
    public String thietlapmacdinh(Model model,
                                  @RequestParam("id") Integer id,
                                  @PathVariable("username") String username) {

        DiaChi dc = khachHangService.findDiaChiById(id);
        DiaChi dcMacDinh = diaChiRepository.findDiaChiByTrangthai(dc.getNguoidung().getId(), true);
        dcMacDinh.setTrangthai(false);
        dc.setTrangthai(true);
        khachHangService.updateDiaChi(dcMacDinh);
        khachHangService.updateDiaChi(dc);

        return "redirect:/hosokhachhang/{username}";
    }

    @GetMapping("/xoadiachi/{username}")
    public String xoadiachi(Model model,
                                  @RequestParam("id") Integer id,
                                  @PathVariable("username") String username) {

        DiaChi dc = khachHangService.findDiaChiById(id);
        diaChiRepository.delete(dc);

        return "redirect:/hosokhachhang/{username}";
    }

    @PostMapping("/doimatkhau/{username}")
    public String doimatkhau(@ModelAttribute("nguoidungdoimatkhau") DoiMatKhauNguoiDung nguoidungdoimatkhau,
                             @PathVariable("username") String username,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(nguoidungdoimatkhau.getMatkhau(), nd.getMatkhau())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng.");
            System.out.println("Lỗi ở so sánh 2 mật khẩu");
            return "redirect:/hosokhachhang/{username}";
        }
        if (!nguoidungdoimatkhau.getMatkhaumoi().equals(nguoidungdoimatkhau.getXacnhanmatkhau())) {
            redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu mới không khớp.");
            System.out.println("Lỗi 2 mật khẩu không khớp");
            return "redirect:/hosokhachhang/{username}";
        }

        String matkhaumoi = passwordEncoder.encode(nguoidungdoimatkhau.getMatkhaumoi());
        nguoiDungRepository.updatePassword(username, matkhaumoi);
        System.out.println("Đã update mật khẩu");

//        DiaChi dc = khachHangService.findDiaChiByIdNguoidung(nd.getId());
//        NguoiDung ndCurrent = khachHangService.findNguoiDungByTaikhoan(username);
//        model.addAttribute("nguoidung", ndCurrent);
//        model.addAttribute("diachi", dc);
//        model.addAttribute("nguoidungdoimatkhau", new DoiMatKhauNguoiDung());

        return "redirect:/hosokhachhang/{username}";
    }
}
