package com.example.demo.controller.giohang;

import com.example.demo.entity.*;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.info.token.UserManager;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.giohang.GioHangChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.repository.giohang.KhachHangGioHangRepository;
import com.example.demo.repository.giohang.NguoiDungGioHangRepository;
import com.example.demo.service.giohang.GioHangService;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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
    GioHangService gioHangService;
    @Autowired
    HttpSession session;

    private static int nextItemId = 1;

    public synchronized int generateUniqueItemId() {
        return nextItemId++;
    }
    public List<GioHangChiTiet> listGioHangKhongTK = new ArrayList<>();

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        List<GioHangChiTiet> cartItems = null;
        List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
        String token = (String) session.getAttribute("token");
        System.out.println("KKKKKK" + token);
        NguoiDung nguoiDung = null;
        KhachHang khachHang = null;

        if (taiKhoanTokenInfos == null || taiKhoanTokenInfos.isEmpty()) {
            System.out.println("EEEEEEEEEEEE");
            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
            session.setAttribute("giohangchitiet", cartItems);
        } else {
            for (TaiKhoanTokenInfo listTK : taiKhoanTokenInfos) {
                if (token.equals(listTK.getToken())) {
                    khachHang = khachHangGioHangRepository.findByNguoidung(listTK.getId());
                    break;
                }
            }
            GioHang gioHang = gioHangRepository.findByIdKhachHang(khachHang.getId());
            cartItems = gioHang.getGioHangChiTietList();
            session.setAttribute("giohangchitiet", cartItems);
            System.out.println("giohang123:" + cartItems.size());
        }

        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Integer, BigDecimal> discountedPrices = new HashMap<>(); // Map để lưu giá sau khi giảm cho từng idspct

        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
            SanPhamChiTiet sanPhamChiTiet = item.getSanphamchitiet();
            BigDecimal giatien = sanPhamChiTiet.getGiatien();
            int discountValue = 0;

            // Lấy giá trị giảm giá nếu có
            for (SanPhamDotGiam spdg : sanPhamChiTiet.getSanphamdotgiam()) {
                DotGiamGia dotGiamGia = spdg.getDotgiamgia();
                if (dotGiamGia != null) {
                    discountValue = dotGiamGia.getGiatrigiam();
                }
            }

            // Tính toán giá tiền sau khi giảm giá
            BigDecimal discountedPrice = giatien.subtract(giatien.multiply(BigDecimal.valueOf(discountValue)).divide(BigDecimal.valueOf(100)));
            discountedPrices.put(sanPhamChiTiet.getId(), discountedPrice); // Lưu giá sau khi giảm với idspct
            totalAmount = totalAmount.add(discountedPrice.multiply(BigDecimal.valueOf(item.getSoluong())));
        }

        // Dùng cho phiếu giảm giá
        List<PhieuGiamGia> lst = phieuGiamGiaImp.findAll();
        List<PhieuGiamGia> lstPGG = new ArrayList<>();
        for (PhieuGiamGia p : lst) {
            if (p.getTrangthai() == 1 && !p.getKieuphieu()) {
                lstPGG.add(p);
            }
        }

        // Thêm các thông tin vào model
        model.addAttribute("token", token);
        model.addAttribute("lstPGG", lstPGG);
        model.addAttribute("discountedPrices", discountedPrices); // Truyền Map vào model
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
            boolean foundInCart = false;
            for (GioHangChiTiet item : gioHangService.getListGioHangKhongTK()) {
                if (item.getSanphamchitiet().equals(sanPhamChiTiet)) {
                    item.setSoluong(item.getSoluong() + quantity);
                    foundInCart = true;
                    break;
                }
            }
            if (!foundInCart) {
                GioHangChiTiet newItem = new GioHangChiTiet();
                newItem.setId(generateUniqueItemId()); // Cấp phát id duy nhất
                newItem.setSanphamchitiet(sanPhamChiTiet);
                newItem.setSoluong(quantity);
                newItem.setNgaytao(new Date());
                newItem.setTrangthai(true);
                gioHangService.addGioHangChiTiet(newItem);
            }
            session.setAttribute("cartItems", gioHangService.getListGioHangKhongTK());
            return "redirect:/detailsanphamCustomer/" + id;
        } else {
            KhachHang khachHang = null;
            for (TaiKhoanTokenInfo listTK : taiKhoanTokenInfos) {
                if (tokenDN != null && tokenDN.equals(listTK.getToken())) {
                    khachHang = khachHangGioHangRepository.findByNguoidung(listTK.getId());
                    break;
                }
            }
            // Kiểm tra xem khách hàng đã có giỏ hàng hay chưa
            GioHang gioHang = gioHangRepository.findByIdKhachHang(khachHang.getId());
            if (gioHang == null) {
                // Nếu không có giỏ hàng, tạo giỏ hàng mới
                LocalDateTime currentTime = LocalDateTime.now();
                GioHang gioHang1 = new GioHang();
                gioHang1.setKhachhang(khachHang);
                gioHang1.setNgaytao(currentTime);
                gioHang1.setTrangthai(true);
                gioHangRepository.save(gioHang1); // Lưu giỏ hàng mới
                // Lấy lại giỏ hàng vừa tạo
                GioHang gioHang2 = gioHangRepository.findByIdKhachHang(khachHang.getId());
                GioHangChiTiet newItem = new GioHangChiTiet();
                newItem.setSanphamchitiet(sanPhamChiTiet);
                newItem.setSoluong(quantity);
                newItem.setNgaytao(new Date());
                newItem.setTrangthai(true);
                newItem.setGiohang(gioHang2);
                gioHangChiTietRepository.save(newItem);
            } else {
                // Nếu đã có giỏ hàng, thêm sản phẩm vào giỏ hàng
                GioHangChiTiet newItem = new GioHangChiTiet();
                newItem.setSanphamchitiet(sanPhamChiTiet);
                newItem.setSoluong(quantity);
                newItem.setNgaytao(new Date());
                newItem.setTrangthai(true);
                newItem.setGiohang(gioHang);

                boolean foundInCart = false;
                List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findGioHangChiTietByGiohang(gioHang.getId());
                for (GioHangChiTiet item : gioHangChiTietList) {
                    if (item.getSanphamchitiet().equals(sanPhamChiTiet)) {
                        item.setSoluong(item.getSoluong() + quantity);
                        gioHangChiTietRepository.save(item);
                        foundInCart = true;
                        break;
                    }
                }
                if (!foundInCart) {
                    gioHangChiTietRepository.save(newItem);
                }
            }
            return "redirect:/detailsanphamCustomer/" + id;
        }
    }

    @PostMapping("/muangay")
    public String muangay(@RequestParam Integer id,
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
            boolean foundInCart = false;
            for (GioHangChiTiet item : gioHangService.getListGioHangKhongTK()) {
                if (item.getSanphamchitiet().equals(sanPhamChiTiet)) {
                    item.setSoluong(item.getSoluong() + quantity);
                    foundInCart = true;
                    break;
                }
            }
            if (!foundInCart) {
                GioHangChiTiet newItem = new GioHangChiTiet();
                newItem.setId(generateUniqueItemId()); // Cấp phát id duy nhất
                newItem.setSanphamchitiet(sanPhamChiTiet);
                newItem.setSoluong(quantity);
                newItem.setNgaytao(new Date());
                newItem.setTrangthai(true);
                gioHangService.addGioHangChiTiet(newItem);
            }
            session.setAttribute("cartItems", gioHangService.getListGioHangKhongTK());
            return "redirect:/view-thanh-toan";
        } else {
            KhachHang khachHang = null;
            for (TaiKhoanTokenInfo listTK : taiKhoanTokenInfos) {
                if (tokenDN != null && tokenDN.equals(listTK.getToken())) {
                    khachHang = khachHangGioHangRepository.findByNguoidung(listTK.getId());
                    break;
                }
            }
            // Kiểm tra xem khách hàng đã có giỏ hàng hay chưa
            GioHang gioHang = gioHangRepository.findByIdKhachHang(khachHang.getId());
            if (gioHang == null) {
                // Nếu không có giỏ hàng, tạo giỏ hàng mới
                LocalDateTime currentTime = LocalDateTime.now();
                GioHang gioHang1 = new GioHang();
                gioHang1.setKhachhang(khachHang);
                gioHang1.setNgaytao(currentTime);
                gioHang1.setTrangthai(true);
                gioHangRepository.save(gioHang1); // Lưu giỏ hàng mới
                // Lấy lại giỏ hàng vừa tạo
                GioHang gioHang2 = gioHangRepository.findByIdKhachHang(khachHang.getId());
                GioHangChiTiet newItem = new GioHangChiTiet();
                newItem.setSanphamchitiet(sanPhamChiTiet);
                newItem.setSoluong(quantity);
                newItem.setNgaytao(new Date());
                newItem.setTrangthai(true);
                newItem.setGiohang(gioHang2);
                gioHangChiTietRepository.save(newItem);
            } else {
                // Nếu đã có giỏ hàng, thêm sản phẩm vào giỏ hàng
                GioHangChiTiet newItem = new GioHangChiTiet();
                newItem.setSanphamchitiet(sanPhamChiTiet);
                newItem.setSoluong(quantity);
                newItem.setNgaytao(new Date());
                newItem.setTrangthai(true);
                newItem.setGiohang(gioHang);

                boolean foundInCart = false;
                List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findGioHangChiTietByGiohang(gioHang.getId());
                for (GioHangChiTiet item : gioHangChiTietList) {
                    if (item.getSanphamchitiet().equals(sanPhamChiTiet)) {
                        item.setSoluong(item.getSoluong() + quantity);
                        gioHangChiTietRepository.save(item);
                        foundInCart = true;
                        break;
                    }
                }
                if (!foundInCart) {
                    gioHangChiTietRepository.save(newItem);
                }
            }
            return "redirect:/view-thanh-toan";
        }
    }

    @GetMapping("/delete/cart/{id}")
    public String deleteCart(@PathVariable("id") Integer id) {
        gioHangChiTietRepository.deleteById(id);
        return "redirect:/cart";

    }

    @PostMapping("/update-cart/{id}")
    public String updateCart(@PathVariable("id") Integer id,
                             @RequestParam("idspct") Integer idspct,
                             Integer quantity,
                             RedirectAttributes redirectAttributes
    ) {
        try {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idspct).orElse(null);
            if (quantity > sanPhamChiTiet.getSoluong()) {
                gioHangChiTietRepository.updateSoLuongById(sanPhamChiTiet.getSoluong(), id);
                redirectAttributes.addFlashAttribute("sussuses", true);
            } else {
                gioHangChiTietRepository.updateSoLuongById(quantity, id);
            }
            return "redirect:/cart";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/delete/cart/guest/{id}")
    public String deleteCartForGuest(@PathVariable("id") Integer id, HttpSession session) {
        List<GioHangChiTiet> cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
        if (cartItems != null) {
            cartItems.removeIf(item -> item.getId().equals(id));
            session.setAttribute("cartItems", cartItems);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update-cart/guest/{id}")
    public String updateCartForGuest(@PathVariable("id") Integer id,
                                     @RequestParam("idspct") Integer idspct,
                                     @RequestParam("quantity") Integer quantity, HttpSession session,
                                     RedirectAttributes redirectAttributes
    ) {
        List<GioHangChiTiet> cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idspct).orElse(null);
        if (cartItems != null) {
            for (GioHangChiTiet item : cartItems) {
                if (item.getId().equals(id)) {
                    if (quantity > sanPhamChiTiet.getSoluong()) {
                        item.setSoluong(sanPhamChiTiet.getSoluong());
                        redirectAttributes.addFlashAttribute("sussuses", true);
                        break;
                    }
                    item.setSoluong(quantity);
                    break;
                }
            }
        }
        session.setAttribute("cartItems", cartItems);
        return "redirect:/cart";
    }

}