package com.example.demo.controller.giohang;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.entity.SanPhamChiTiet;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
//    @Autowired
//    public UserManager userManager;

    @GetMapping("/cart")
    public String cart2(Model model,
    HttpSession session
    ) {
        List<GioHangChiTiet> cartItems = gioHangChiTietRepository.findAll(); // Giả sử bạn có phương thức này để lấy các mục trong giỏ hàng
        int totalQuantity = 0;
        // Tính tổng số lượng sản phẩm trong giỏ hàng
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (GioHangChiTiet item : cartItems) {
            // Lấy giá của sản phẩm từ bảng sản phẩm chi tiết
            BigDecimal giatien = sanPhamChiTietRepository.findPriceByProductId(item.getSanphamchitiet().getId());
            totalAmount = totalAmount.add(giatien.multiply(BigDecimal.valueOf(item.getSoluong())));
        }
        //dùng cho phiếu giảm giá
        List<PhieuGiamGia> lst = phieuGiamGiaImp.findAll();
        List<PhieuGiamGia> lstPGG = new ArrayList<>();
        for (PhieuGiamGia p : lst) {
            if (p.getTrangthai() == 1 && p.getKieuphieu() == false) {
                lstPGG.add(p);
            }
        }
        List<TaiKhoanTokenInfo> list= (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
        model.addAttribute("lstPGG", lstPGG);
        model.addAttribute("totalAmount", totalAmount);
        // Đưa tổng số lượng vào model để hiển thị trên giao diện
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("cartItems", cartItems);
        return "customer/cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Integer id, @RequestParam String selectedColor, @RequestParam String selectedSize,
                            @RequestParam Integer quantity, Model model) {
//        Map<Integer, String> mapToken = UserManager.class.
        // Tìm sản phẩm chi tiết dựa trên màu sắc và kích cỡ
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findBySanPhamIdAndColorAndSize(id, selectedColor, selectedSize);

        if (sanPhamChiTiet != null && quantity > 0 && quantity <= sanPhamChiTiet.getSoluong()) {
            // Tìm giỏ hàng chi tiết của sản phẩm trong toàn bộ cơ sở dữ liệu
            List<GioHangChiTiet> gioHangChiTiets = gioHangChiTietRepository.findBySanphamchitiet(sanPhamChiTiet);
            boolean foundInCart = false;

            // Duyệt qua danh sách giỏ hàng chi tiết của sản phẩm
            for (GioHangChiTiet item : gioHangChiTiets) {
                // Nếu tìm thấy sản phẩm trong giỏ hàng chi tiết
                if (item.getSanphamchitiet().equals(sanPhamChiTiet)) {
                    // Cộng dồn số lượng mới vào số lượng hiện tại
                    int newQuantity = item.getSoluong() + quantity;
                    item.setSoluong(newQuantity);
                    gioHangChiTietRepository.save(item);
                    foundInCart = true;
                    break; // Dừng vòng lặp khi đã tìm thấy sản phẩm
                }
            }

            // Nếu không tìm thấy sản phẩm trong giỏ hàng chi tiết, thêm mới vào giỏ hàng
            if (!foundInCart) {
                GioHangChiTiet newItem = new GioHangChiTiet();
                GioHang gioHang = new GioHang();
                gioHang.setId(1);
                newItem.setGiohang(gioHang);
                newItem.setSanphamchitiet(sanPhamChiTiet);
                newItem.setSoluong(quantity);
                newItem.setNgaytao(new Date());
                newItem.setTrangthai(true);
                gioHangChiTietRepository.save(newItem);
            }
        }
        Integer firstProductId = sanPhamChiTiet.getSanpham().getId();
        return "redirect:/detailsanphamCustomer/" + firstProductId;
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
