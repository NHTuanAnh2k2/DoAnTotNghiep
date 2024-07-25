package com.example.demo.controller.customer;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamCustomerInfo;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.repository.AnhRepository;
import com.example.demo.repository.KichCoRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepositoty;
import com.example.demo.repository.customer.TrangChuRepository;
import com.example.demo.repository.giohang.GioHangChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.repository.giohang.KhachHangGioHangRepository;
import com.example.demo.repository.giohang.NguoiDungGioHangRepository;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class TrangChuCustomerController {
    @Autowired
    KhachHangGioHangRepository khachHangGioHangRepository;
    @Autowired
    NguoiDungGioHangRepository nguoiDungGioHangRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    TrangChuRepository trangChuRepository;

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

    public List<TaiKhoanTokenInfo> taiKhoanTokenInfos = new ArrayList<>();

    @GetMapping("/customer/trangchu")
    public String hienthiTrangChu(Model model, HttpSession session) {
        List<GioHangChiTiet> cartItems = new ArrayList<>();
        String token = (String) session.getAttribute("token");
        NguoiDung nguoiDung = null;
        KhachHang khachHang = null;
        GioHang gioHang = null;

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (token != null) {
            // Lấy danh sách token từ session
            List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
            if (taiKhoanTokenInfos != null) {
                // Tìm người dùng từ danh sách token
                for (TaiKhoanTokenInfo tkInfo : taiKhoanTokenInfos) {
                    if (tkInfo.getToken().equals(token)) {
                        Integer userId = tkInfo.getId();
                        nguoiDung = nguoiDungGioHangRepository.findById(userId).orElse(null);
                        break;
                    }
                }

                if (nguoiDung != null) {
                    // Lấy giỏ hàng của người dùng đã đăng nhập
                    khachHang = khachHangGioHangRepository.findByNguoidung(nguoiDung.getId());
                    if (khachHang != null) {
                        gioHang = gioHangRepository.findByKhachhang(khachHang);
                        if (gioHang != null) {
                            cartItems = gioHang.getGioHangChiTietList();
                        }
                    }
                }
            }
        } else {
            // Giỏ hàng của người dùng chưa đăng nhập
            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
        }

        // Tính tổng số lượng sản phẩm và tổng tiền
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
            BigDecimal giatien = sanPhamChiTietRepository.findPriceByProductId(item.getSanphamchitiet().getId());
            totalAmount = totalAmount.add(giatien.multiply(BigDecimal.valueOf(item.getSoluong())));
        }
        // Thêm các thông tin vào model
        model.addAttribute("token", token);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("cartItems", cartItems);


        List<Object[]> topspmoinhattrangchu = trangChuRepository.topspmoinhattrangchu();
        model.addAttribute("topspmoinhattrangchu", topspmoinhattrangchu);
        List<Object[]> topspbanchaynhattrangchu = trangChuRepository.topspbanchaynhat();
        model.addAttribute("topspbanchaynhattrangchu", topspbanchaynhattrangchu);
        return "customer/trangchu";
    }

    @GetMapping("/detailsanphamCustomer/{id}")
    public String detailsanphamCustomer(@PathVariable Integer id, @RequestParam(required = false) String color, @RequestParam(required = false)
            String size, Model model, HttpSession session) {

        List<GioHangChiTiet> cartItems = new ArrayList<>();
        String token = (String) session.getAttribute("token");
        NguoiDung nguoiDung = null;
        KhachHang khachHang = null;
        GioHang gioHang = null;

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (token != null) {
            // Lấy danh sách token từ session
            List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
            if (taiKhoanTokenInfos != null) {
                // Tìm người dùng từ danh sách token
                for (TaiKhoanTokenInfo tkInfo : taiKhoanTokenInfos) {
                    if (tkInfo.getToken().equals(token)) {
                        Integer userId = tkInfo.getId();
                        nguoiDung = nguoiDungGioHangRepository.findById(userId).orElse(null);
                        break;
                    }
                }
                if (nguoiDung != null) {
                    // Lấy giỏ hàng của người dùng đã đăng nhập
                    khachHang = khachHangGioHangRepository.findByNguoidung(nguoiDung.getId());
                    if (khachHang != null) {
                        gioHang = gioHangRepository.findByKhachhang(khachHang);
                        if (gioHang != null) {
                            cartItems = gioHang.getGioHangChiTietList();
                        }
                    }
                }
            }
        } else {
            // Giỏ hàng của người dùng chưa đăng nhập
            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
        }
        // Tính tổng số lượng sản phẩm và tổng tiền
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
            BigDecimal giatien = sanPhamChiTietRepository.findPriceByProductId(item.getSanphamchitiet().getId());
            totalAmount = totalAmount.add(giatien.multiply(BigDecimal.valueOf(item.getSoluong())));
        }
        // Thêm các thông tin vào model

        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("cartItems", cartItems);

        SanPham sanPham = trangChuRepository.findById(id).orElse(null);
        model.addAttribute("sanpham", sanPham);
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id).orElse(null);
//        model.addAttribute("giamgia",sanPhamChiTiet.getGiamGia());
        model.addAttribute("sanphamchitiet", sanPhamChiTiet);
        List<String> danhSachAnh = new ArrayList<>();
        for (SanPhamChiTiet spct : sanPham.getSpct()) {
            for (Anh anh : spct.getAnh()) {
                if (color == null || color.equals(spct.getMausac().getTen())) {
                    danhSachAnh.add(anh.getTenanh());
                }
            }
        }
        model.addAttribute("danhSachAnh", danhSachAnh);
        List<String> sizes = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        BigDecimal selectedPrice = null;
        Integer selectedQuantity = null;
        String selectedThuongHieu = null;
        String selectedChatLieu = null;
        String selectedDeGiay = null;
        String selectedMaSPCT = null;
        String defaultColor = null;
        String defaultSize = null;
        for (SanPhamChiTiet spct : sanPham.getSpct()) {
            if (!sizes.contains(spct.getKichco().getTen())) {
                sizes.add(spct.getKichco().getTen());
            }
            if (!colors.contains(spct.getMausac().getTen())) {
                colors.add(spct.getMausac().getTen());
            }
            if ((color == null || color.equals(spct.getMausac().getTen())) && (size == null || size.equals(spct.getKichco().getTen()))) {
                selectedPrice = spct.getGiatien();
                selectedQuantity = spct.getSoluong();
                selectedThuongHieu = spct.getThuonghieu().getTen();
                selectedChatLieu = spct.getChatlieu().getTen();
                selectedDeGiay = spct.getDegiay().getTen();
                selectedMaSPCT = spct.getMasanphamchitiet();
            }
        }
        if (colors.size() > 0) {
            defaultColor = colors.get(0);
        }
        if (sizes.size() > 0) {
            defaultSize = sizes.get(0);
        }
        model.addAttribute("sizes", sizes);
        model.addAttribute("colors", colors);
        model.addAttribute("selectedPrice", selectedPrice);
        model.addAttribute("selectedQuantity", selectedQuantity);
        model.addAttribute("selectedThuongHieu", selectedThuongHieu);
        model.addAttribute("selectedChatLieu", selectedChatLieu);
        model.addAttribute("selectedDeGiay", selectedDeGiay);
        model.addAttribute("selectedMaSPCT", selectedMaSPCT);
        model.addAttribute("defaultColor", defaultColor);
        model.addAttribute("defaultSize", defaultSize);
        List<Object[]> page = trangChuRepository.topspmoinhatdetail();
        model.addAttribute("page", page);
        List<Object[]> page2 = trangChuRepository.topspnoibatdetail();
        model.addAttribute("page2", page2);
        model.addAttribute("token", token);
        return "customer/product-details";
    }

    @GetMapping("/search-trangchu")
    public String searchTrangChu(Model model,
                                 @ModelAttribute("search") SanPhamCustomerInfo info,
                                 HttpSession session) {
        List<GioHangChiTiet> cartItems = new ArrayList<>();
        String token = (String) session.getAttribute("token");
        NguoiDung nguoiDung = null;
        KhachHang khachHang = null;
        GioHang gioHang = null;

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (token != null) {
            // Lấy danh sách token từ session
            List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
            if (taiKhoanTokenInfos != null) {
                // Tìm người dùng từ danh sách token
                for (TaiKhoanTokenInfo tkInfo : taiKhoanTokenInfos) {
                    if (tkInfo.getToken().equals(token)) {
                        Integer userId = tkInfo.getId();
                        nguoiDung = nguoiDungGioHangRepository.findById(userId).orElse(null);
                        break;
                    }
                }
                if (nguoiDung != null) {
                    // Lấy giỏ hàng của người dùng đã đăng nhập
                    khachHang = khachHangGioHangRepository.findByNguoidung(nguoiDung.getId());
                    if (khachHang != null) {
                        gioHang = gioHangRepository.findByKhachhang(khachHang);
                        if (gioHang != null) {
                            cartItems = gioHang.getGioHangChiTietList();
                        }
                    }
                }
            }
        } else {
            // Giỏ hàng của người dùng chưa đăng nhập
            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
        }
        // Tính tổng số lượng sản phẩm và tổng tiền
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
            BigDecimal giatien = sanPhamChiTietRepository.findPriceByProductId(item.getSanphamchitiet().getId());
            totalAmount = totalAmount.add(giatien.multiply(BigDecimal.valueOf(item.getSoluong())));
        }
        // Thêm các thông tin vào model

        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("cartItems", cartItems);

        List<Object[]> list = null;
        String trimmedKey = (info.getKey() != null) ? info.getKey().trim().replaceAll("\\s+", " ") : null;
        if (trimmedKey == null || trimmedKey.isEmpty()) {
            list = trangChuRepository.searchAll();
        } else {
            list = trangChuRepository.searchTrangChu("%" + trimmedKey + "%", "%" + trimmedKey + "%");
        }
        model.addAttribute("list", list);
        model.addAttribute("fillSearch", trimmedKey);
        return "customer/search-trangchu";
    }

}