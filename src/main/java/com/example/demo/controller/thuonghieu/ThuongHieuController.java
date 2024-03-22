package com.example.demo.controller.thuonghieu;

import com.example.demo.entity.DeGiay;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.service.impl.ThuongHieuImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ThuongHieuController {
    @Autowired
    ThuongHieuImp thuongHieuImp;

    @GetMapping("/listthuonghieu")
    public String hienthi(@RequestParam(defaultValue = "0") int p, @ModelAttribute("th") ThuocTinhInfo info,@ModelAttribute("thuonghieu") ThuongHieu thuongHieu, Model model) {
        Pageable pageable = PageRequest.of(p, 10);
        Page<ThuongHieu> page = null;
        if (info.getKey() != null) {
            page = thuongHieuImp.getThuongHieuByTenOrTrangthai(info.getKey(), info.getTrangthai(), pageable);
        } else {
            page = thuongHieuImp.getAll(pageable);
        }
        model.addAttribute("page", page);
        return "admin/qlthuonghieu";
    }

    @PostMapping("/addSaveThuongHieu")
    public String addSave(@ModelAttribute("thuonghieu") ThuongHieu thuongHieu,@ModelAttribute("th") ThuocTinhInfo info) {
        thuongHieuImp.add(thuongHieu);
        return "redirect:/listthuonghieu";
    }

}
