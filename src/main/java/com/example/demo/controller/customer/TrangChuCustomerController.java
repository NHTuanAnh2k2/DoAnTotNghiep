package com.example.demo.controller.customer;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamCustomerInfo;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.repository.*;
import com.example.demo.repository.customer.TrangChuRepository;
import com.example.demo.repository.giohang.GioHangChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.repository.giohang.KhachHangGioHangRepository;
import com.example.demo.repository.giohang.NguoiDungGioHangRepository;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    SanPhamDotGiamRepository sanPhamDotGiamRepository;

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
        if (token != null) {
            List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
            if (taiKhoanTokenInfos != null) {
                for (TaiKhoanTokenInfo tkInfo : taiKhoanTokenInfos) {
                    if (tkInfo.getToken().equals(token)) {
                        Integer userId = tkInfo.getId();
                        nguoiDung = nguoiDungGioHangRepository.findById(userId).orElse(null);
                        break;
                    }
                }
                if (nguoiDung != null) {
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
            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
        }
        int totalQuantity = 0;
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
        }
        model.addAttribute("totalQuantity", totalQuantity);

        SanPham sanPham = trangChuRepository.findById(id).orElse(null);
        model.addAttribute("sanpham", sanPham);
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id).orElse(null);
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
        BigDecimal selectedPriceMoi = null;
        Integer selectedQuantity = null;
        String selectedThuongHieu = null;
        String selectedChatLieu = null;
        String selectedDeGiay = null;
        String selectedMaSPCT = null;
        Integer selectedIdspct = null;
        String defaultColor = null;
        String defaultSize = null;
        Integer selectedDiscount = null; // Biến để lưu giá trị giảm giá đã chọn
        Map<Integer, Integer> sanPhamChiTietGiamGia = new HashMap<>(); // Map để lưu giảm giá cho từng sản phẩm chi tiết
//        for (SanPhamChiTiet spct : sanPham.getSpct()) {
//            int giamGia = 0;
//            for (SanPhamDotGiam spdg : spct.getSanphamdotgiam()) {
//                DotGiamGia dotGiamGia = spdg.getDotgiamgia();
//                if (dotGiamGia != null) {
//                    giamGia = dotGiamGia.getGiatrigiam(); // Giả sử giatrigiam là giá trị giảm
//                }
//            }
//            sanPhamChiTietGiamGia.put(spct.getId(), giamGia);
//        }
        for (SanPhamDotGiam s:sanPhamDotGiamRepository.ListDotGiamDangHD()) {
            sanPhamChiTietGiamGia.put(s.getSanphamchitiet().getId(), s.getDotgiamgia().getGiatrigiam());
        }
        // Lấy giá trị giảm giá cho từng sản phẩm chi tiết
        for (SanPhamChiTiet spct : sanPham.getSpct()) {
            if (!colors.contains(spct.getMausac().getTen())) {
                colors.add(spct.getMausac().getTen());
            }
            if (!sizes.contains(spct.getKichco().getTen())) {
                sizes.add(spct.getKichco().getTen());
            }

            if ((color == null || color.equals(spct.getMausac().getTen())) && (size == null || size.equals(spct.getKichco().getTen()))) {
                selectedPrice = spct.getGiatien();
                selectedQuantity = spct.getSoluong();
                selectedThuongHieu = spct.getThuonghieu().getTen();
                selectedChatLieu = spct.getChatlieu().getTen();
                selectedDeGiay = spct.getDegiay().getTen();
                selectedMaSPCT = spct.getMasanphamchitiet();
                selectedIdspct = spct.getId();
                selectedDiscount = sanPhamChiTietGiamGia.get(selectedIdspct); // Lấy giá trị giảm giá
                if(selectedDiscount!=null){
                    selectedPriceMoi = selectedPrice.subtract(selectedPrice.multiply(BigDecimal.valueOf(selectedDiscount)).divide(BigDecimal.valueOf(100)));
                }else {
                    selectedPriceMoi=spct.getGiatien();
                }
            }
        }
        if (colors.size() > 0) {
            defaultColor = colors.get(0);
        }
//        if (sizes.size() > 0) {
//            defaultSize = sizes.get(0);
//        }
        List<SanPhamChiTiet> lstSizeTheoMau = sanPhamChiTietRepository.listSizeColor(sanPham.getId(), colors.get(0));
        if(lstSizeTheoMau.size()>0){
            defaultSize = lstSizeTheoMau.get(0).getKichco().getTen();
        }

        System.out.println("AAAAAAAAAAAAA");
        System.out.println("BBBBBBBBBB:"+selectedDiscount);
        model.addAttribute("selectedDiscount", selectedDiscount); // Thêm giảm giá đã chọn vào model
        model.addAttribute("selectedPriceMoi", selectedPriceMoi); // Thêm giảm giá đã chọn vào model
        model.addAttribute("sanPhamChiTietGiamGia", sanPhamChiTietGiamGia); // Thêm giảm giá vào model
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
        model.addAttribute("selectedIdspct", selectedIdspct);
        List<Object[]> page = trangChuRepository.topspmoinhatdetail();
        model.addAttribute("page", page);
        List<Object[]> page2 = trangChuRepository.topspnoibatdetail();
        model.addAttribute("page2", page2);
        model.addAttribute("token", token);
        return "customer/product-details";
    }

    //validate input số lượng detail client
    @GetMapping("checkQuantity")
    @ResponseBody
    public ResponseEntity<Boolean> checksl(@RequestParam("id") String lstDataStr, HttpSession session) {
        String[] lstDataArray = lstDataStr.split(",");
        List<String> lstData = Arrays.stream(lstDataArray)
                .map(item -> URLDecoder.decode(item, StandardCharsets.UTF_8))
                .collect(Collectors.toList());

        Integer idspct = Integer.valueOf(lstData.get(0));
        Integer slInput = Integer.valueOf(lstData.get(1));
        Optional<SanPhamChiTiet> optionalSpct = sanPhamChiTietRepository.findById(idspct);
        if (optionalSpct.isPresent()) {
            SanPhamChiTiet sanPhamChiTiet = optionalSpct.get();
            Integer slTrongKho = sanPhamChiTiet.getSoluong();
            // Lấy số lượng sản phẩm hiện tại trong giỏ hàng (đã đăng nhập)
            List<GioHangChiTiet> cartItems = new ArrayList<>();
            String token = (String) session.getAttribute("token");
            if (token != null) {
                List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
                if (taiKhoanTokenInfos != null) {
                    for (TaiKhoanTokenInfo tkInfo : taiKhoanTokenInfos) {
                        if (tkInfo.getToken().equals(token)) {
                            Integer userId = tkInfo.getId();
                            NguoiDung nguoiDung = nguoiDungGioHangRepository.findById(userId).orElse(null);
                            if (nguoiDung != null) {
                                KhachHang khachHang = khachHangGioHangRepository.findByNguoidung(nguoiDung.getId());
                                if (khachHang != null) {
                                    GioHang gioHang = gioHangRepository.findByKhachhang(khachHang);
                                    if (gioHang != null) {
                                        cartItems = gioHang.getGioHangChiTietList();
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Lấy giỏ hàng từ session nếu người dùng chưa đăng nhập
                cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
                if (cartItems == null) {
                    cartItems = new ArrayList<>();
                }
            }
            // Tính tổng số lượng của biến thể sản phẩm hiện tại trong giỏ hàng
            Integer slTrongGio = cartItems.stream()
                    .filter(item -> item.getSanphamchitiet().getId().equals(idspct))
                    .mapToInt(GioHangChiTiet::getSoluong)
                    .sum();
            // Kiểm tra số lượng nhập vào cộng với số lượng trong giỏ hàng có vượt quá số lượng trong kho không
            if (slInput + slTrongGio > slTrongKho) {
                return ResponseEntity.ok(false); // Không hợp lệ
            } else {
                return ResponseEntity.ok(true); // Hợp lệ
            }
        } else {
            return ResponseEntity.ok(false); // Không tìm thấy sản phẩm
        }
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
        if (token != null) {
            List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
            if (taiKhoanTokenInfos != null) {
                for (TaiKhoanTokenInfo tkInfo : taiKhoanTokenInfos) {
                    if (tkInfo.getToken().equals(token)) {
                        Integer userId = tkInfo.getId();
                        nguoiDung = nguoiDungGioHangRepository.findById(userId).orElse(null);
                        break;
                    }
                }
                if (nguoiDung != null) {
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
            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
        }
        int totalQuantity = 0;
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
        }
        model.addAttribute("totalQuantity", totalQuantity);

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