package com.example.demo.controller.sanpham;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.info.SanPhamInfo;
import com.example.demo.service.impl.SanPhamImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SanPhamController {
    @Autowired
    SanPhamImp sanPhamImp;

    @GetMapping("/listsanpham")
    public String hienthi(@RequestParam(defaultValue = "0") int p, @ModelAttribute("tim") SanPhamInfo info, Model model) {
        Pageable pageable = PageRequest.of(p, 5);
        Page<SanPham> page = null;
        if (info.getKey() != null && info.getTrangthai() != null && info.getSoluong() != null) {
            page = sanPhamImp.findAllByTensanphamAndTrangthaiAndSpctSoluong(info.getKey(), info.getTrangthai(), info.getSoluong(), pageable);
        } else {
            page = sanPhamImp.getAll(pageable);
        }
        model.addAttribute("list", page.getContent());
        return "/admin/qlsanpham";
    }

}
