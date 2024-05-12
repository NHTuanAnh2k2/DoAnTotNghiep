package com.example.demo.controller.customer;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamCustomerInfo;
import com.example.demo.repository.AnhRepository;
import com.example.demo.repository.KichCoRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepositoty;
import com.example.demo.repository.customer.SanPhamCustomerRepository;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;

@Controller
public class SanPhamCustomerController {
    @Autowired
    SanPhamCustomerRepository sanPhamCustomerRepository;
    @Autowired
    KichCoRepository kichCoRepository;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    SanPhamRepositoty sanPhamRepositoty;

    @Autowired
    SanPhamImp sanPhamImp;

    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;

    @Autowired
    ThuongHieuImp thuongHieuImp;

    @Autowired
    MauSacImp mauSacImp;

    @Autowired
    KichCoImp kichCoImp;

    @Autowired
    DeGiayImp deGiayImp;

    @Autowired
    ChatLieuImp chatLieuImp;

    @Autowired
    AnhImp anhImp;
    @Autowired
    AnhRepository anhRepository;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/customer/sanphamnam")
    public String sanphamnam(Model model,@ModelAttribute("search2") SanPhamCustomerInfo info) {
        List<SanPham> listSanPham = sanPhamImp.findAll();
        List<ThuongHieu> listThuongHieu = thuongHieuImp.findAll();
        List<MauSac> listMauSac = mauSacImp.findAll();
        List<KichCo> listKichCo = kichCoImp.findAll();
        List<DeGiay> listDeGiay = deGiayImp.findAll();
        List<ChatLieu> listChatLieu = chatLieuImp.findAll();
        List<SanPhamChiTiet> listSanPhamChiTiet = sanPhamChiTietRepository.findAll();
        model.addAttribute("sp", listSanPham);
        model.addAttribute("th", listThuongHieu);
        model.addAttribute("ms", listMauSac);
        model.addAttribute("kc", listKichCo);
        model.addAttribute("dg", listDeGiay);
        model.addAttribute("cl", listChatLieu);
        model.addAttribute("spct", listSanPhamChiTiet);
        List<Object[]> list=null;
        boolean filterByGender = (info != null && info.getIdThuongHieu() == null && info.getIdKichCo2() == null);
        boolean allUnchecked = true;
        if (info != null && info.getIdThuongHieu() != null) {
            for (Integer idThuongHieu : info.getIdThuongHieu()) {
                if (idThuongHieu != null) {
                    allUnchecked = false;
                    break;
                }
            }
        }
        if (info != null && info.getIdKichCo2() != null) {
            for (Integer idKichCo2 : info.getIdKichCo2()) {
                if (idKichCo2 != null) {
                    allUnchecked = false;
                    break;
                }
            }
        }
        if (allUnchecked) {
            list = sanPhamCustomerRepository.findProductsGioiTinh0();
        } else {
            list = sanPhamCustomerRepository.searchByGender0(info.getIdThuongHieu(), info.getIdKichCo2());
        }
        model.addAttribute("list0", list);
        List<Object[]> page = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        return "customer/sanphamnam";
    }

    @GetMapping("/customer/sanphamnu")
    public String sanphamnu(Model model,@ModelAttribute("search2") SanPhamCustomerInfo info) {
        List<SanPham> listSanPham = sanPhamImp.findAll();
        List<ThuongHieu> listThuongHieu = thuongHieuImp.findAll();
        List<MauSac> listMauSac = mauSacImp.findAll();
        List<KichCo> listKichCo = kichCoImp.findAll();
        List<DeGiay> listDeGiay = deGiayImp.findAll();
        List<ChatLieu> listChatLieu = chatLieuImp.findAll();
        List<SanPhamChiTiet> listSanPhamChiTiet = sanPhamChiTietRepository.findAll();
        model.addAttribute("sp", listSanPham);
        model.addAttribute("th", listThuongHieu);
        model.addAttribute("ms", listMauSac);
        model.addAttribute("kc", listKichCo);
        model.addAttribute("dg", listDeGiay);
        model.addAttribute("cl", listChatLieu);
        model.addAttribute("spct", listSanPhamChiTiet);
        List<Object[]> list=null;
        boolean filterByGender = (info != null && info.getIdThuongHieu() == null && info.getIdKichCo2() == null);
        boolean allUnchecked = true;
        if (info != null && info.getIdThuongHieu() != null) {
            for (Integer idThuongHieu : info.getIdThuongHieu()) {
                if (idThuongHieu != null) {
                    allUnchecked = false;
                    break;
                }
            }
        }
        if (info != null && info.getIdKichCo2() != null) {
            for (Integer idKichCo2 : info.getIdKichCo2()) {
                if (idKichCo2 != null) {
                    allUnchecked = false;
                    break;
                }
            }
        }
        if (allUnchecked) {
            list = sanPhamCustomerRepository.findProductsGioiTinh1();
        } else {
            list = sanPhamCustomerRepository.searchByGender1(info.getIdThuongHieu(), info.getIdKichCo2());
        }
        model.addAttribute("list1", list);
        List<Object[]> page = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        return "customer/sanphamnu";
    }
}
