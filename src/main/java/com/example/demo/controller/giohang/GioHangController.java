package com.example.demo.controller.giohang;


import com.example.demo.entity.GioHang;
import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.giohang.GioHangChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.service.giohang.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
public class GioHangController {
    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;


    @GetMapping("/cart")
    public String cart() {
        return "customer/cart";
    }


    @GetMapping("/cart2")
    public String cart2(Model model) {
        List<GioHangChiTiet> cartItems = gioHangChiTietRepository.findAll(); // Giả sử bạn có phương thức này để lấy các mục trong giỏ hàng
        int totalQuantity = cartItems.size(); // Đếm số lượng các mục trong giỏ hàng
        // Đưa tổng số lượng vào model để hiển thị trên giao diện
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("cartItems", cartItems);
        return "customer/cart2";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Integer id, @RequestParam Integer quantity, Model model) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id).orElse(null);

        if (sanPhamChiTiet != null) {
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
        return "redirect:/detailsanphamCustomer/" + firstProductId; // Chuyển hướng đến trang giỏ hàng sau khi thêm
    }

    @GetMapping("/delete/cart/{id}")
    public String deleteCart(@PathVariable Integer id) {
        gioHangChiTietRepository.deleteById(id);
        return "redirect:/cart2";

    }
    @PostMapping("/update-cart/{id}")
    public String updateCart(@PathVariable Integer id,  Integer quantity) {
        try {
            gioHangChiTietRepository.updateSoLuongById(quantity, id);
            return "redirect:/cart2"; // Chuyển hướng đến trang giỏ hàng sau khi cập nhật
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu cần thiết
            return "redirect:/error"; // Chuyển hướng đến trang lỗi nếu có lỗi xảy ra
        }
    }

}
