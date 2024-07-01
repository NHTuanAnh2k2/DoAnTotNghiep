package com.example.demo.controller.giohang;

import com.example.demo.entity.*;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.info.token.UserManager;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.giohang.GioHangChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.repository.giohang.KhachHangGioHangRepository;
import com.example.demo.repository.giohang.NguoiDungGioHangRepository;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class GioHangController {

    @Autowired
    KhachHangGioHangRepository khachHangGioHangRepository;
    @Autowired
    NguoiDungGioHangRepository nguoiDungGioHangRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private PhieuGiamGiaImp phieuGiamGiaImp;
    @Autowired
    public UserManager userManager;

    @Autowired
    HttpSession session;

    public List<GioHangChiTiet> listGioHangKhongTK = new ArrayList<>();

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session
    ) {
        List<GioHangChiTiet> cartItems = null;
        List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
        String token = (String) session.getAttribute("token");
        System.out.println("KKKKKK" + token);
        NguoiDung nguoiDung = null;
        KhachHang khachHang = null;
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (taiKhoanTokenInfos == null || taiKhoanTokenInfos.isEmpty()) {
            System.out.println("EEEEEEEEEEEE");
            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");

            session.setAttribute("giohangchitiet", cartItems);

        } else {
            KhachHang khachHang1 = new KhachHang();
            for (TaiKhoanTokenInfo listTK : taiKhoanTokenInfos) {
                if (token.equals(listTK.getToken())) {
                    khachHang = khachHangGioHangRepository.findByNguoidung(listTK.getId());
                    break;
                }
            }
            GioHang gioHang = gioHangRepository.findByIdKhachHang(khachHang.getId());
            cartItems = gioHang.getGioHangChiTietList();
            session.setAttribute("giohangchitiet", cartItems);
        }


//        if (taiKhoanTokenInfos != null) {
//            // Tìm người dùng từ danh sách token
//            for (TaiKhoanTokenInfo tkInfo : taiKhoanTokenInfos) {
//                if (tkInfo.getToken().equals(token)) {
//                    Integer userId = tkInfo.getId();
//                    nguoiDung = nguoiDungGioHangRepository.findById(userId).orElse(null);
//                    break;
//                }
//            }
//            if (nguoiDung != null) {
//                // Lấy giỏ hàng của người dùng đã đăng nhập
////                    khachHang = khachHangGioHangRepository.findByNguoidung(nguoiDung);
//                if (khachHang != null) {
//                    gioHang = gioHangRepository.findByKhachhang(khachHang);
//                    if (gioHang != null) {
//                        cartItems = gioHang.getGioHangChiTietList();
//                    }
//                }
//            }
//        }
//    } else
//
//    {
//        // Giỏ hàng của người dùng chưa đăng nhập
////            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
//        if (cartItems == null) {
//            cartItems = new ArrayList<>();
//        }
//    }

        // Tính tổng số lượng sản phẩm và tổng tiền
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (
                GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
            BigDecimal giatien = sanPhamChiTietRepository.findPriceByProductId(item.getSanphamchitiet().getId());
            totalAmount = totalAmount.add(giatien.multiply(BigDecimal.valueOf(item.getSoluong())));
        }

        // Dùng cho phiếu giảm giá
        List<PhieuGiamGia> lst = phieuGiamGiaImp.findAll();
        List<PhieuGiamGia> lstPGG = new ArrayList<>();
        for (
                PhieuGiamGia p : lst) {
            if (p.getTrangthai() == 1 && !p.getKieuphieu()) {
                lstPGG.add(p);
            }
        }
        // Thêm các thông tin vào model
        model.addAttribute("token", token);
        model.addAttribute("lstPGG", lstPGG);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("cartItems", cartItems);
        return "customer/cart";
    }

    @ResponseBody
    @GetMapping("/giohangchitiet")
    public ResponseEntity<?> giohangchitiet() {
        List<GioHangChiTiet> list = (List<GioHangChiTiet>) session.getAttribute("giohangchitiet");
        return ResponseEntity.ok(list);
    }


    @ResponseBody
    @GetMapping("/findBySanPhamId/{Id}")
    public ResponseEntity<?> getProductDetails(@PathVariable int Id) {
        SanPhamChiTiet lst = sanPhamChiTietRepository.findByIdSPCT(Id);
        return ResponseEntity.ok(lst);
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Integer id,
                            @RequestParam String selectedColor,
                            @RequestParam String selectedSize,
                            @RequestParam Integer quantity,
                            Model model,
                            HttpSession session,
                            @RequestParam(value = "tokenDN") String tokenDN) {
        // Tìm sản phẩm chi tiết dựa trên màu sắc và kích cỡ
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findBySanPhamIdAndColorAndSize(id, selectedColor, selectedSize);
        List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
        if (taiKhoanTokenInfos == null || taiKhoanTokenInfos.isEmpty()) {
            GioHangChiTiet newItem = new GioHangChiTiet();
            newItem.setSanphamchitiet(sanPhamChiTiet);
            newItem.setSoluong(quantity);
            newItem.setNgaytao(new Date());
            newItem.setTrangthai(true);
            listGioHangKhongTK.add(newItem);
            session.setAttribute("cartItems", listGioHangKhongTK);
            return "redirect:/detailsanphamCustomer/" + id;
        } else {
            KhachHang khachHang = null;
            for (TaiKhoanTokenInfo listTK : taiKhoanTokenInfos) {
                System.out.println("tokenfor:" + listTK.getToken());
                System.out.println("tokenDN" + tokenDN);
                if (tokenDN != null && tokenDN.equals(listTK.getToken())) {
                    System.out.println("DDDDDDDDDDDDDDDDDD" + listTK.getId());
                    khachHang = khachHangGioHangRepository.findByNguoidung(listTK.getId());
                    System.out.println("CCCCCCCCCCCC" + khachHang.getId());
                    break;
                }
            }
            GioHang gioHang = gioHangRepository.findByIdKhachHang(khachHang.getId());
            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBB" + gioHang.getId());
            if (gioHang == null) {
                LocalDateTime currentTime = LocalDateTime.now();
                GioHang gioHang1 = new GioHang();
                gioHang1.setKhachhang(khachHang);
                gioHang1.setNgaytao(currentTime);
                gioHang1.setTrangthai(true);
                gioHangRepository.save(gioHang1);
                GioHang gioHang2 = gioHangRepository.findByIdKhachHang(khachHang.getId());
                GioHangChiTiet newItem = new GioHangChiTiet();
                newItem.setSanphamchitiet(sanPhamChiTiet);
                newItem.setSoluong(quantity);
                newItem.setNgaytao(new Date());
                newItem.setTrangthai(true);
                newItem.setGiohang(gioHang2);
                gioHangChiTietRepository.save(newItem);
                return "redirect:/detailsanphamCustomer/" + id;
            } else {
                GioHang gioHang2 = gioHangRepository.findByIdKhachHang(khachHang.getId());
                System.out.println("AAAAAAAAAAAAA:" + gioHang2.getId());
                GioHangChiTiet newItem = new GioHangChiTiet();
                newItem.setSanphamchitiet(sanPhamChiTiet);
                newItem.setSoluong(quantity);
                newItem.setNgaytao(new Date());
                newItem.setTrangthai(true);
                newItem.setGiohang(gioHang2);
                boolean foundInCart = false;
                List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findGioHangChiTietByGiohang(gioHang2.getId());
                if (gioHangChiTietList != null) {
                    for (GioHangChiTiet item : gioHangChiTietList) {
                        if (item.getSanphamchitiet().equals(sanPhamChiTiet)) {
                            item.setSoluong(item.getSoluong() + quantity);
                            gioHangChiTietRepository.save(item);
                            foundInCart = true;
                            break;
                        }
                    }
                }
                if (foundInCart == false) {
                    gioHangChiTietRepository.save(newItem);
                }

                return "redirect:/detailsanphamCustomer/" + id;
            }
        }


//        if (sanPhamChiTiet != null && quantity > 0 && quantity <= sanPhamChiTiet.getSoluong()) {
//            String token = (String) session.getAttribute("token");
//            NguoiDung nguoiDung = null;
//
//            if (token != null) {
//                // Lấy thông tin người dùng từ token
//                List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
//                if (taiKhoanTokenInfos != null) {
//                    for (TaiKhoanTokenInfo tkInfo : taiKhoanTokenInfos) {
//                        if (tkInfo.getToken().equals(token)) {
//                            Integer userId = tkInfo.getId();
//                            nguoiDung = nguoiDungGioHangRepository.findById(userId).orElse(null);
//                            break;
//                        }
//                    }
//                    if (nguoiDung != null) {
//                        KhachHang khachHang = khachHangGioHangRepository.findByNguoidung(nguoiDung);
//
//                        if (khachHang == null) {
//                            LocalDateTime currentTime = LocalDateTime.now();
//                            // Tạo mới KhachHang nếu chưa tồn tại
//                            khachHang = new KhachHang();
//                            khachHang.setMakhachhang("duynvph30146");
//                            khachHang.setNguoitao("DUY");
//                            khachHang.setNguoicapnhat("TÙNG");
//                            khachHang.setTrangthai(false);
//                            khachHang.setNguoidung(nguoiDung);
//                            khachHang = khachHangGioHangRepository.save(khachHang);
//                        }
//                        GioHang gioHang = gioHangRepository.findByKhachhang(khachHang);
//                        if (gioHang == null) {
//                            LocalDateTime currentTime = LocalDateTime.now();
//                            gioHang = new GioHang();
//                            gioHang.setNgaytao(currentTime);
//                            gioHang.setTrangthai(false);
//                            gioHang.setKhachhang(khachHang);
//                            gioHang = gioHangRepository.save(gioHang);
//                        }
//                        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findByGiohangAndSanphamchitiet(gioHang, sanPhamChiTiet);
//
//                        if (gioHangChiTiet != null) {
//                            gioHangChiTiet.setSoluong(gioHangChiTiet.getSoluong() + quantity);
//                        } else {
//                            gioHangChiTiet = new GioHangChiTiet();
//                            gioHangChiTiet.setGiohang(gioHang);
//                            gioHangChiTiet.setSanphamchitiet(sanPhamChiTiet);
//                            gioHangChiTiet.setSoluong(quantity);
//                            gioHangChiTiet.setNgaytao(new Date());
//                            gioHangChiTiet.setTrangthai(true);
//                        }
//                        gioHangChiTietRepository.save(gioHangChiTiet);
//                    }
//                }
//            } else {
//                List<GioHangChiTiet> cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
//                if (cartItems == null) {
//                    cartItems = new ArrayList<>();
//                }
//
//                boolean foundInCart = false;
//                for (GioHangChiTiet item : cartItems) {
//                    if (item.getSanphamchitiet().equals(sanPhamChiTiet)) {
//                        item.setSoluong(item.getSoluong() + quantity);
//                        foundInCart = true;
//                        break;
//                    }
//                }
//                if (!foundInCart) {
//                    GioHangChiTiet newItem = new GioHangChiTiet();
//                    newItem.setSanphamchitiet(sanPhamChiTiet);
//                    newItem.setSoluong(quantity);
//                    newItem.setNgaytao(new Date());
//                    newItem.setTrangthai(true);
//                    cartItems.add(newItem);
//                }
//
//                session.setAttribute("cartItems", cartItems);
//            }
//        }
//        return "redirect:/detailsanphamCustomer/" + id;
    }


    @GetMapping("/delete/cart/{id}")
    public String deleteCart(@PathVariable Integer id) {
        gioHangChiTietRepository.deleteById(id);
        return "redirect:/cart";

    }

    @PostMapping("/update-cart/{id}")
    public String updateCart(@PathVariable Integer id, Integer quantity) {
        try {
            gioHangChiTietRepository.updateSoLuongById(quantity, id);
            return "redirect:/cart";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
}
