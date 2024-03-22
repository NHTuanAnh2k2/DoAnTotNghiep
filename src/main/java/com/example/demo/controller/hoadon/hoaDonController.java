package com.example.demo.controller.hoadon;

import com.example.demo.entity.HoaDon;
import com.example.demo.info.HoaDonCustom;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("hoa-don")
public class hoaDonController {
    @Autowired
    HoaDonService dao;

    @GetMapping("hien-thi")
    public String hienThi(Model model, @RequestParam("page") Optional<Integer> pageParam) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Page<HoaDon> lst = dao.findAll(p);
        model.addAttribute("lst", lst);
        model.addAttribute("pageNo", page);
        return "admin/qlhoadon";
    }

    @GetMapping("loc")
    public String Loc(Model model, @RequestParam("page") Optional<Integer> pageParam, @ModelAttribute("hdtim") HoaDonCustom HDinfo) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Integer trangThai = -1;
        if (HDinfo.getKey().equalsIgnoreCase("chờ xác nhận")) {
            trangThai = 0;
        } else {
            if (HDinfo.getKey().equalsIgnoreCase("đã xác nhận")) {
                trangThai = 1;
            } else {
                if (HDinfo.getKey().equalsIgnoreCase("chờ giao hàng")) {
                    trangThai = 2;
                } else {
                    if (HDinfo.getKey().equalsIgnoreCase("đang giao hàng")) {
                        trangThai = 3;
                    } else {
                        if (HDinfo.getKey().equalsIgnoreCase("đã thanh toán")) {
                            trangThai = 4;
                        } else {
                            if (HDinfo.getKey().equalsIgnoreCase("đã hoàn thành")) {
                                trangThai = 5;
                            } else {
                                if (HDinfo.getKey().equalsIgnoreCase("đã hủy")) {
                                    trangThai = 6;
                                }
                            }
                        }
                    }

                }
            }
        }
        Page<HoaDon> lst = dao.Loc(trangThai,
                HDinfo.getLoaiHD(), HDinfo.getTu(), HDinfo.getDen(), p);
        model.addAttribute("lst", lst);
        return "admin/qlhoadon";
    }
}
