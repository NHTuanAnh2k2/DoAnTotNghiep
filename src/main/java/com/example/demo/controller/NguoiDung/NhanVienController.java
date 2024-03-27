package com.example.demo.controller.NguoiDung;

import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.DeGiay;
import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.DiaChiNVInfo;
import com.example.demo.info.NguoiDungNVInfo;
import com.example.demo.info.NhanVienInfo;
import com.example.demo.service.impl.DiaChiImpl;
import com.example.demo.service.impl.NguoiDungImpl1;
import com.example.demo.service.impl.NhanVienImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NhanVienController {
    @Autowired
    NhanVienImpl nhanVien;
    @Autowired
    DiaChiImpl diaChi;
    @Autowired
    NguoiDungImpl1 nguoiDung;


    @GetMapping("/admin/qlnhanvien")
    public String listnv(Model model,@ModelAttribute("nd") NguoiDungNVInfo nd) {
        List<DiaChi> page = diaChi.getAll();
        model.addAttribute("list", page);
        return "admin/qlnhanvien";
    }
    @GetMapping("/timkiem")
    public String list(Model model,@ModelAttribute("nd") NguoiDungNVInfo nd) {
        List<DiaChi> page = new ArrayList<>();
        if (nd.getHovaten()!= null){
        page = diaChi.get(nd.getHovaten(), nd.getSodienthoai());}
        else if (nd.getSodienthoai() != null){
            page = diaChi.get(nd.getHovaten(), nd.getSodienthoai());
        }else if (nd.getTrangthai() != null){
            page = diaChi.getTT(nd.getTrangthai());
        }
        model.addAttribute("list", page);
        return "admin/qlnhanvien";
    }
    @GetMapping("/admin/addnhanvien")
    public String viewAdd(
                          @ModelAttribute("nd") NguoiDungNVInfo nd,
                          @ModelAttribute("dc") DiaChiNVInfo dc,
                          Model model, RedirectAttributes redirectAttributes) {

        return "admin/addnhanvien";
    }
    @PostMapping("/addnv")
    public String addSave(
                           @ModelAttribute("nd") @Valid  NguoiDungNVInfo nd,
                           @ModelAttribute("dc") @Valid DiaChiNVInfo dc,
                          Model model, BindingResult bindingResult,
                          Errors error ) {
        if(bindingResult.hasErrors()){
            System.out.println("lỗi");
            return "/admin/addnhanvien";
        }
        nguoiDung.add(nd);
        NguoiDung n = nguoiDung.search(nd.getEmail());
        NhanVienInfo nv = new NhanVienInfo();
        nv.setIdnguoidung(n);
        nhanVien.add(nv);
        dc.setIdnguoidung(n);
        diaChi.add(dc);
        String to = n.getEmail();
        String subject = "Chúc mừng đã trở thành nhân viên của T&T shop";
        String mailType = "";
        String mailContent = "Mật khẩu của bạn là: " + n.getMatkhau();
        nguoiDung.sendEmail(to, subject, mailType, mailContent);
        return "redirect:/admin/qlnhanvien";
    }
    @GetMapping("/updateNhanVien/{id}")
    public String viewUpdate(@PathVariable Integer id, Model model,
                             @ModelAttribute("nd") NguoiDungNVInfo nd,
                             @ModelAttribute("nv") NhanVienInfo nv,
                             @ModelAttribute("dc") DiaChiNVInfo dc
                             ) {
        model.addAttribute("nd", nguoiDung.findById(id));
        model.addAttribute("dc",diaChi.search(id));
        return "admin/updatenhanvien";
    }
    @PostMapping("/updateNhanVien/{id}")
    public String update(@PathVariable Integer id, Model model,
                         @ModelAttribute("nd") NguoiDungNVInfo nd,
                         @ModelAttribute("nv") NhanVienInfo nv,
                         @ModelAttribute("dc") DiaChiNVInfo dc) {
        nguoiDung.update(nd,id);
        nhanVien.update(nv,id);
        diaChi.update(dc,id);
        return "redirect:/admin/qlnhanvien";
    }
}
