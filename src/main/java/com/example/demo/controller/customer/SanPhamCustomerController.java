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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
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

    @GetMapping("/customer/searchMaAnhTen/spnam")
    public String searchspnam(Model model, @ModelAttribute("searchspnam") SanPhamCustomerInfo info, @ModelAttribute("search2") SanPhamCustomerInfo info2, @RequestParam(defaultValue = "0") int p) {
        Pageable pageable = PageRequest.of(p, 10);
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
        Page<Object[]> page = null;
        if (info.getKey() == null) {
            page = sanPhamCustomerRepository.findProductsGioiTinh1(pageable);
        } else {
            page = sanPhamCustomerRepository.searchByMaAnhTenSP("%" + info.getKey() + "%", "%" + info.getKey() + "%", pageable);
        }
        model.addAttribute("listnam", page);
        model.addAttribute("fillSearch", info.getKey());
        List<Object[]> page1 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page1);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        int soLuongThuongHieu = sanPhamChiTietRepository.countByThuongHieuTenIsNikeNam();
        model.addAttribute("soLuongThuongHieu", soLuongThuongHieu);
        return "customer/sanphamnam";
    }

    @GetMapping("/customer/sanphamnam")
    public String sanphamnam(Model model, @ModelAttribute("search2") SanPhamCustomerInfo info, @RequestParam(defaultValue = "0") int p) {
        Pageable pageable = PageRequest.of(p, 10);
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
        Page<Object[]> page = null;
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
            page = sanPhamCustomerRepository.findProductsGioiTinh1(pageable);
        } else {
            page = sanPhamCustomerRepository.searchByGender1(info.getIdThuongHieu(), info.getIdKichCo2(), pageable);
        }
        model.addAttribute("listnam", page);
        List<Object[]> page1 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page1);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        int soLuongThuongHieu = sanPhamChiTietRepository.countByThuongHieuTenIsNikeNam();
        model.addAttribute("soLuongThuongHieu", soLuongThuongHieu);
        return "customer/sanphamnam";
    }

//    @GetMapping("/customer/sanphamnam/sap-xep-tangdan")
//    public String tangdan(Model model){
//        Sort sort = Sort.by(Sort.Order.desc("spct.giatien"));
//        List<Object[]> list=sanPhamCustomerRepository.loctangdan(sort);
//        model.addAttribute("listnam", list);
//        return "customer/sanphamnam";
//    }

    @GetMapping("/customer/sanphamnam/sap-xep-tangdan")
    public String tangdannam(Model model, @ModelAttribute("search2") SanPhamCustomerInfo info, @RequestParam(defaultValue = "0") int p) {
        Pageable pageable = PageRequest.of(p, 10);
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
        Page<Object[]> page = null;
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
            page = sanPhamCustomerRepository.loctangdan(pageable);
        } else {
            page = sanPhamCustomerRepository.searchByGender1(info.getIdThuongHieu(), info.getIdKichCo2(), pageable);
        }
        model.addAttribute("listnam", page);
        List<Object[]> page1 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page1);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        int soLuongThuongHieu = sanPhamChiTietRepository.countByThuongHieuTenIsNikeNam();
        model.addAttribute("soLuongThuongHieu", soLuongThuongHieu);
        return "customer/sanphamnam";
    }

    @GetMapping("/customer/sanphamnam/sap-xep-giamdan")
    public String giamdannam(Model model, @ModelAttribute("search2") SanPhamCustomerInfo info, @RequestParam(defaultValue = "0") int p) {
        Pageable pageable = PageRequest.of(p, 10);
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
        Page<Object[]> page = null;
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
            page = sanPhamCustomerRepository.locgiamdan(pageable);
        } else {
            page = sanPhamCustomerRepository.searchByGender1(info.getIdThuongHieu(), info.getIdKichCo2(), pageable);
        }
        model.addAttribute("listnam", page);
        List<Object[]> page1 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page1);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        int soLuongThuongHieu = sanPhamChiTietRepository.countByThuongHieuTenIsNikeNam();
        model.addAttribute("soLuongThuongHieu", soLuongThuongHieu);
        return "customer/sanphamnam";
    }

    @GetMapping("/customer/sanphamnu")
    public String sanphamnu(Model model, @ModelAttribute("search2") SanPhamCustomerInfo info, @RequestParam(defaultValue = "0") int p) {
        Pageable pageable = PageRequest.of(p, 10);
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
        Page<Object[]> page = null;
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
            page = sanPhamCustomerRepository.findProductsGioiTinh0(pageable);
        } else {
            page = sanPhamCustomerRepository.searchByGender0(info.getIdThuongHieu(), info.getIdKichCo2(), pageable);
        }
        model.addAttribute("listnu", page);
        List<Object[]> page1 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page1);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        int soLuongThuongHieu = sanPhamChiTietRepository.countByThuongHieuTenIsNikeNu();
        model.addAttribute("soLuongThuongHieu", soLuongThuongHieu);
        return "customer/sanphamnu";
    }

    // loc tang dan nu theo gia tien
    @GetMapping("/customer/sanphamnu/sap-xep-tangdan")
    public String tangdannu(Model model, @ModelAttribute("search2") SanPhamCustomerInfo info, @RequestParam(defaultValue = "0") int p) {
        Pageable pageable = PageRequest.of(p, 10);
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
        Page<Object[]> page = null;
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
            page = sanPhamCustomerRepository.loctangdannu(pageable);
        } else {
            page = sanPhamCustomerRepository.searchByGender0(info.getIdThuongHieu(), info.getIdKichCo2(), pageable);
        }
        model.addAttribute("listnu", page);
        List<Object[]> page1 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page1);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        int soLuongThuongHieu = sanPhamChiTietRepository.countByThuongHieuTenIsNikeNu();
        model.addAttribute("soLuongThuongHieu", soLuongThuongHieu);
        return "customer/sanphamnu";
    }

    // loc giam dan nu theo gia tien
    @GetMapping("/customer/sanphamnu/sap-xep-giamdan")
    public String giamdannu(Model model, @ModelAttribute("search2") SanPhamCustomerInfo info, @RequestParam(defaultValue = "0") int p) {
        Pageable pageable = PageRequest.of(p, 10);
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
        Page<Object[]> page = null;
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
            page = sanPhamCustomerRepository.locgiamdannu(pageable);
        } else {
            page = sanPhamCustomerRepository.searchByGender0(info.getIdThuongHieu(), info.getIdKichCo2(), pageable);
        }
        model.addAttribute("listnu", page);
        List<Object[]> page1 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page1);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        int soLuongThuongHieu = sanPhamChiTietRepository.countByThuongHieuTenIsNikeNu();
        model.addAttribute("soLuongThuongHieu", soLuongThuongHieu);
        return "customer/sanphamnu";
    }

    @GetMapping("/customer/searchMaAnhTen/spnu")
    public String searchspnu(Model model,@ModelAttribute("searchspnu") SanPhamCustomerInfo info, @ModelAttribute("search2") SanPhamCustomerInfo info2, @RequestParam(defaultValue = "0") int p) {
        Pageable pageable = PageRequest.of(p, 10);
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
        Page<Object[]> page = null;
        if(info.getKey()==null){
            page=sanPhamCustomerRepository.findProductsGioiTinh0(pageable);
        }else{
            page=sanPhamCustomerRepository.searchByMaAnhTenSPNu("%" + info.getKey() + "%", "%" + info.getKey() + "%", pageable);
        }
        model.addAttribute("listnu", page);
        model.addAttribute("fillSearch", info.getKey());
        List<Object[]> page1 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page", page1);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page2", page2);
        int soLuongThuongHieu = sanPhamChiTietRepository.countByThuongHieuTenIsNikeNam();
        model.addAttribute("soLuongThuongHieu", soLuongThuongHieu);
        return "customer/sanphamnu";
    }
}
