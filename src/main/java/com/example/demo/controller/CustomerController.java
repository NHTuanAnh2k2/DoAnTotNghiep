package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.info.NguoiDungKHInfo;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.info.hosokhachhang.DoiMatKhauNguoiDung;
import com.example.demo.info.hosokhachhang.UpdateDiaChi;
import com.example.demo.info.hosokhachhang.UpdateKhachHang;
import com.example.demo.info.hosokhachhang.UpdateNguoiDung;
import com.example.demo.info.token.UserManager;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.repository.hoadon.HoaDonChiTietRepository;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.restcontroller.khachhang.Province;
import com.example.demo.security.JWTGenerator;
import com.example.demo.service.KhachHangService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class CustomerController {

    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    DiaChiRepository diaChiRepository;
    @Autowired
    UserManager userManager;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    GioHangRepository gioHangRepository;


    @GetMapping("/trangchu")
    public String trangchu() {
        return "customer/trangchu";
    }

    @GetMapping("/modal")
    public String modal() {
        return "customer/modal";
    }

    @GetMapping("/quenmatkhau")
    public String quenmatkhauview(@ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                                  Model model) {
        return "same/quenmatkhau";
    }
    @GetMapping("/customer/tracuudonhang")
    public String tracuu(Model model) {
        return "customer/tracuudonhang";
    }
    @GetMapping("/hosokhachhang/{username}")
    public String hosokhachhang(Model model,
                                @PathVariable("username") String username,
                                @RequestParam("page") Optional<Integer> pageParam,
                                HttpSession session
    ) {
        String userDangnhap = (String) session.getAttribute("userDangnhap");
        if (username != null) {
            if (userDangnhap != null) {
                NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(userDangnhap);
                DiaChi dc = diaChiRepository.findDiaChiByTrangthai(nd.getId(), true);
                List<DiaChi> lstDiaChi = diaChiRepository.findDiaChiByIdNd(nd.getId());

                List<KhachHang> lstKh = khachHangService.findAll();
                List<String> lstEmail = new ArrayList<>();
                List<String> lstSdt = new ArrayList<>();
                List<String> lstCccd = new ArrayList<>();
                for (KhachHang kh : lstKh) {
                    lstEmail.add(kh.getNguoidung().getEmail());
                    lstSdt.add(kh.getNguoidung().getSodienthoai());
                    lstCccd.add(kh.getNguoidung().getCccd());
                }

                model.addAttribute("lstEmail", lstEmail);
                model.addAttribute("lstSdt", lstSdt);
                model.addAttribute("lstCccd", lstCccd);

                KhachHang khHoaDon = khachHangService.findKhachHangByIdNguoiDung(nd.getId());
                Integer customerId = khHoaDon.getId();
//                int page = pageParam.orElse(0);
//                Pageable p = PageRequest.of(page, 5);
                List<HoaDon> lstHoaDon = hoaDonRepository.findAllByKhachHang(customerId); //tất cả
                List<HoaDon> lstHoaDon0 = hoaDonRepository.findHoaDonByTrangThaiAndKhachhang(0, customerId); //chờ xác nhận
                List<HoaDon> lstHoaDon1 = hoaDonRepository.findHoaDonByTrangThaiAndKhachhang(1, customerId); //đã xác nhận
                List<HoaDon> lstHoaDon2 = hoaDonRepository.findHoaDonByTrangThaiAndKhachhang(2, customerId); //chờ giao hàng
                List<HoaDon> lstHoaDon3 = hoaDonRepository.findHoaDonByTrangThaiAndKhachhang(3, customerId); //đang giao hàng
                List<HoaDon> lstHoaDon4 = hoaDonRepository.findHoaDonByTrangThaiAndKhachhang(4, customerId); //đã thanh toán
                List<HoaDon> lstHoaDon5 = hoaDonRepository.findHoaDonByTrangThaiAndKhachhang(5, customerId); //đã hoàn thành
                List<HoaDon>  lstHoaDon6 = hoaDonRepository.findHoaDonByTrangThaiAndKhachhang(6, customerId); //đã hủy

                model.addAttribute("lstHoaDon", lstHoaDon);
                model.addAttribute("lstHoaDon0", lstHoaDon0);
                model.addAttribute("lstHoaDon1", lstHoaDon1);
                model.addAttribute("lstHoaDon2", lstHoaDon2);
                model.addAttribute("lstHoaDon3", lstHoaDon3);
                model.addAttribute("lstHoaDon4", lstHoaDon4);
                model.addAttribute("lstHoaDon5", lstHoaDon5);
                model.addAttribute("lstHoaDon6", lstHoaDon6);

                model.addAttribute("nguoidung", nd);
                model.addAttribute("diachi", dc);
                model.addAttribute("lstDiaChi", lstDiaChi);
                model.addAttribute("userDangnhap", userDangnhap);
                model.addAttribute("nguoidungdoimatkhau", new DoiMatKhauNguoiDung());
                model.addAttribute("addDiaChi", new UpdateDiaChi());
                model.addAttribute("updateDiaChi", dc);
                System.out.println("Danh sách người dùng đang đăng nhập: " + userManager.getLoggedInUsers());
                return "customer/hosokhachhang";
            } else {
                return "redirect:/account";
            }
        } else {
            return "redirect:/account";
        }
    }

    @PostMapping("/capnhattaikhoan/{username}")
    public String capnhattaikhoan(Model model,
                                  RedirectAttributes redirectAttributes,
                                  @PathVariable("username") String username,
                                  @ModelAttribute("nguoidung") UpdateNguoiDung nguoidung,
                                  @ModelAttribute("diachi") UpdateDiaChi diachi,
                                  @ModelAttribute("khachhang") UpdateKhachHang khachhang

    ) {

        NguoiDung userSelect = khachHangService.findNguoiDungByTaikhoan(username);
        int userId = userSelect.getId();

        NguoiDung nd = khachHangService.findNguoiDungById(userId);
        System.out.println("Nguoi dung: " + nd);
        nd.setHovaten(nguoidung.getHovaten().trim().replaceAll("\\s+", " "));
        nd.setGioitinh(nguoidung.getGioitinh());
        nd.setCccd(nguoidung.getCccd().trim());
        nd.setEmail(nguoidung.getEmail().trim());
        nd.setNgaysinh(nguoidung.getNgaysinh());
        nd.setSodienthoai(nguoidung.getSodienthoai().trim());
        nd.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        nd.setNguoicapnhat("CUSTOMER");
        khachHangService.updateNguoiDung(nd);
        System.out.println("updateND ok");

        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/hosokhachhang/{username}";
    }

    @PostMapping("/themdiachi/{username}")
    public String themdiachi(Model model,
                             @PathVariable("username") String username,
                             @ModelAttribute("addDiaChi") UpdateDiaChi diachi,
                             RedirectAttributes redirectAttributes

    ) {
        NguoiDung userSelect = khachHangService.findNguoiDungByTaikhoan(username);

        DiaChi existingDiaChi = diaChiRepository.findDiaChiByDetails(
                diachi.getHotennguoinhan(),
                diachi.getSdtnguoinhan(),
                diachi.getTenduong(),
                diachi.getTinhthanhpho(),
                diachi.getQuanhuyen(),
                diachi.getXaphuong()
        );

        if (existingDiaChi != null) {
            // Nếu đã tồn tại, thực hiện cập nhật địa chỉ đã có
            existingDiaChi.setTenduong(diachi.getTenduong().trim().replaceAll("\\s+", " "));
            existingDiaChi.setSdtnguoinhan(diachi.getSdtnguoinhan());
            existingDiaChi.setTinhthanhpho(diachi.getTinhthanhpho());
            existingDiaChi.setQuanhuyen(diachi.getQuanhuyen());
            existingDiaChi.setXaphuong(diachi.getXaphuong());
            existingDiaChi.setHotennguoinhan(diachi.getHotennguoinhan().trim().replaceAll("\\s+", " "));
            existingDiaChi.setNguoicapnhat("CUSTOMER");
            existingDiaChi.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));

            khachHangService.addDiaChi(existingDiaChi);
        } else {
            DiaChi dc = new DiaChi();
            dc.setNguoidung(userSelect);
            dc.setTinhthanhpho(diachi.getTinhthanhpho());
            dc.setQuanhuyen(diachi.getQuanhuyen());
            dc.setXaphuong(diachi.getXaphuong());
            dc.setTenduong(diachi.getTenduong().trim().replaceAll("\\s+", " "));
            dc.setSdtnguoinhan(diachi.getSdtnguoinhan());
            dc.setHotennguoinhan(diachi.getHotennguoinhan().trim().replaceAll("\\s+", " "));
            dc.setNguoicapnhat("CUSTOMER");
            dc.setNguoitao("CUSTOMER");
            dc.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
            dc.setNgaytao(Timestamp.valueOf(LocalDateTime.now()));
            dc.setTrangthai(false);
            khachHangService.addDiaChi(dc);
        }

        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/hosokhachhang/{username}";
    }

    @PostMapping("/suadiachi/{username}")
    public String suadiachi(Model model,
                            @PathVariable("username") String username,
                            @Valid @ModelAttribute("updateDiaChi") UpdateDiaChi diachi,
                            BindingResult dcBindingResult,
                            RedirectAttributes redirectAttributes

    ) {
        if (dcBindingResult.hasErrors()) {
            List<ObjectError> lstErrorDc = dcBindingResult.getAllErrors();
            System.out.println("List Error DiaChi: " + lstErrorDc);
            return "customer/hosokhachhang";
        }

        NguoiDung userSelect = khachHangService.findNguoiDungByTaikhoan(username);

        DiaChi dc = diaChiRepository.findDiaChiByTrangthai(userSelect.getId(), true);
        dc.setTinhthanhpho(diachi.getTinhthanhpho());
        dc.setQuanhuyen(diachi.getQuanhuyen());
        dc.setXaphuong(diachi.getXaphuong());
        dc.setTenduong(diachi.getTenduong().trim().replaceAll("\\s+", " "));
        dc.setSdtnguoinhan(diachi.getSdtnguoinhan());
        dc.setHotennguoinhan(diachi.getHotennguoinhan().trim().replaceAll("\\s+", " "));
        dc.setNguoicapnhat("CUSTOMER");
        dc.setNguoitao("CUSTOMER");
        dc.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        khachHangService.addDiaChi(dc);

        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/hosokhachhang/{username}";
    }

    @GetMapping("/thietlapmacdinh")
    public String thietlapmacdinh(Model model,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam("id") Integer id) {

        DiaChi dc = khachHangService.findDiaChiById(id);
        DiaChi dcMacDinh = diaChiRepository.findDiaChiByTrangthai(dc.getNguoidung().getId(), true);
        dcMacDinh.setTrangthai(false);
        dc.setTrangthai(true);
        khachHangService.updateDiaChi(dcMacDinh);
        khachHangService.updateDiaChi(dc);

        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/hosokhachhang/" + dcMacDinh.getNguoidung().getTaikhoan();
    }

    @GetMapping("/xoadiachi")
    public String xoadiachi(Model model,
                            RedirectAttributes redirectAttributes,
                            @RequestParam("id") Integer id) {

        DiaChi dc = khachHangService.findDiaChiById(id);
        diaChiRepository.delete(dc);

        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/hosokhachhang/" + dc.getNguoidung().getTaikhoan();
    }

    @PostMapping("/doimatkhau/{username}")
    public String doimatkhau(@ModelAttribute("nguoidungdoimatkhau") DoiMatKhauNguoiDung nguoidungdoimatkhau,
                             @PathVariable("username") String username,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (nguoidungdoimatkhau.getMatkhau() == "") {
            redirectAttributes.addFlashAttribute("error1", "Mật khẩu không được trống.");
            System.out.println("Lỗi ở so sánh 2 mật khẩu");
            return "redirect:/hosokhachhang/{username}";
        }
        if (nguoidungdoimatkhau.getMatkhaumoi().length() < 8) {
            redirectAttributes.addFlashAttribute("error2", "Mật khẩu mới phải có ít nhất 8 ký tự.");
            return "redirect:/hosokhachhang/{username}";
        }
        if (nguoidungdoimatkhau.getMatkhaumoi().contains(" ")) {
            redirectAttributes.addFlashAttribute("error2", "Mật khẩu không được chứa khoảng trắng.");
            return "redirect:/hosokhachhang/{username}";
        }
        if (!passwordEncoder.matches(nguoidungdoimatkhau.getMatkhau(), nd.getMatkhau())) {
            redirectAttributes.addFlashAttribute("error1", "Mật khẩu hiện tại không đúng.");
            System.out.println("Lỗi ở so sánh 2 mật khẩu");
            return "redirect:/hosokhachhang/{username}";
        }
        if (!nguoidungdoimatkhau.getMatkhaumoi().equals(nguoidungdoimatkhau.getXacnhanmatkhau())) {
            redirectAttributes.addFlashAttribute("error2", "Xác nhận mật khẩu mới không khớp.");
            System.out.println("Lỗi 2 mật khẩu không khớp");
            return "redirect:/hosokhachhang/{username}";
        }


        String matkhaumoi = passwordEncoder.encode(nguoidungdoimatkhau.getMatkhaumoi());
        nguoiDungRepository.updatePassword(username, matkhaumoi);
        System.out.println("Đã update mật khẩu");

        redirectAttributes.addFlashAttribute("success", true);


        return "redirect:/hosokhachhang/{username}";
    }

    @GetMapping("/customer/timkiem")
    public String timkiem(Model model,
                          @RequestParam(value = "searchInput", required = false) String inputsearch
                          ){
        List<HoaDon> lstHoaDon = new ArrayList<>();
        if (inputsearch != null) {
           lstHoaDon = hoaDonRepository.findHoaDonByMaOrSdtOrEmail(inputsearch);
            System.out.println("lstHoaDon: " + lstHoaDon);
        }

        model.addAttribute("lstHoaDon", lstHoaDon);
        return "customer/tracuudonhang";
    }

//    @PostMapping("/mualai")
//    public String addToCart(@RequestParam Integer idHoaDon,
//                            @RequestParam Integer quantity,
//                            Model model,
//                            HttpSession session) {
//        // Tìm sản phẩm chi tiết dựa trên màu sắc và kích cỡ
//        String selectedColor =
//        String selectedSize =
//        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findBySanPhamIdAndColorAndSize(id, selectedColor, selectedSize);
//        List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
//        if (taiKhoanTokenInfos == null || taiKhoanTokenInfos.isEmpty()) {
//            boolean foundInCart = false;
//            for (GioHangChiTiet item : listGioHangKhongTK) {
//                if (item.getSanphamchitiet().equals(sanPhamChiTiet)) {
//                    item.setSoluong(item.getSoluong() + quantity);
//                    foundInCart = true;
//                    break;
//                }
//            }
//            if (!foundInCart) {
//                GioHangChiTiet newItem = new GioHangChiTiet();
//                newItem.setId(generateUniqueItemId()); // Cấp phát id duy nhất
//                newItem.setSanphamchitiet(sanPhamChiTiet);
//                newItem.setSoluong(quantity);
//                newItem.setNgaytao(new Date());
//                newItem.setTrangthai(true);
//                listGioHangKhongTK.add(newItem);
//            }
//            session.setAttribute("cartItems", listGioHangKhongTK);
//            return "redirect:/detailsanphamCustomer/" + id;
//        } else {
//            String username = (String) session.getAttribute("userDangnhap");
//            NguoiDung nguoiDung = khachHangService.findNguoiDungByTaikhoan(username);
//            KhachHang khachHang = khachHangService.findKhachHangByIdNguoiDung(nguoiDung.getId());
//            // Kiểm tra xem khách hàng đã có giỏ hàng hay chưa
//            GioHang gioHang = gioHangRepository.findByIdKhachHang(khachHang.getId());
//                // Nếu đã có giỏ hàng, thêm sản phẩm vào giỏ hàng
//                GioHangChiTiet newItem = new GioHangChiTiet();
//                newItem.setSanphamchitiet(sanPhamChiTiet);
//                newItem.setSoluong(quantity);
//                newItem.setNgaytao(new Date());
//                newItem.setTrangthai(true);
//                newItem.setGiohang(gioHang);
//
//                boolean foundInCart = false;
//                List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findGioHangChiTietByGiohang(gioHang.getId());
//                for (GioHangChiTiet item : gioHangChiTietList) {
//                    if (item.getSanphamchitiet().equals(sanPhamChiTiet)) {
//                        item.setSoluong(item.getSoluong() + quantity);
//                        gioHangChiTietRepository.save(item);
//                        foundInCart = true;
//                        break;
//                    }
//                }
//
//            }
//            return "redirect:/detailsanphamCustomer/" + id;
//        }
//    }

}
