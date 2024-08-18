package com.example.demo.controller.thanhtoanmuaonline;

import com.example.demo.entity.*;
import com.example.demo.info.DiaChiThanhToanNoTaiKhoanOnline;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.repository.*;
import com.example.demo.repository.PhieuGiamGiaChiTiet.PhieuGiamChiTietRepository;
import com.example.demo.repository.giohang.GioHangChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.repository.giohang.KhachHangGioHangRepository;
import com.example.demo.repository.giohang.NguoiDungGioHangRepository;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.LichSuHoaDonService;
import com.example.demo.service.PhuongThucThanhToanService;
import com.example.demo.service.giohang.GioHangService;
import com.example.demo.service.impl.HoaDonChiTietImp;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ThanhToanController {
    @Autowired
    KhachHangGioHangRepository khachHangGioHangRepository;
    @Autowired
    NguoiDungGioHangRepository nguoiDungGioHangRepository;

    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private PhieuGiamGiaImp phieuGiamGiaImp;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    HoaDonService daoHD;
    @Autowired
    HoaDonChiTietImp hoaDonChiTietImp;
    @Autowired
    PhieuGiamChiTietRepository daoPGGCT;
    @Autowired
    LichSuHoaDonService daoLSHD;
    @Autowired
    PhuongThucThanhToanService daoPTTT;
    @Autowired
    KhachHangPhieuGiamRepository khachHangPhieuGiamRepository;
    @Autowired
    DiaChiRepository diaChiRepository;
    @Autowired
    HttpSession session;
    @Autowired
    GioHangService gioHangService;
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @Autowired
    SanPhamDotGiamRepository SPdotgiamRepo;

    @RequestMapping("/cam-on-da-dat-hang")
    public String camon(){
        return "customer/camondathang";
    }

    @RequestMapping("/view-thanh-toan")
    public String viewthanhtoan(Model model, @ModelAttribute("diachikotaikhoan") DiaChiThanhToanNoTaiKhoanOnline diachikotaikhoan, HttpSession session) {
        List<GioHangChiTiet> cartItems = new ArrayList<>();
        String token = (String) session.getAttribute("token");
        NguoiDung nguoiDung = null;
        KhachHang khachHang = null;
        GioHang gioHang = null;
        List<PhieuGiamGia> lst = phieuGiamGiaImp.findAll();
        List<PhieuGiamGia> lstPGG = new ArrayList<>();
        for (PhieuGiamGia p : lst) {
            if (p.getTrangthai() == 1 && p.getKieuphieu() == false) {
                lstPGG.add(p);
            }
        }
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
                    model.addAttribute("tenKH",nguoiDung.getHovaten());
                    model.addAttribute("emailKH",nguoiDung.getEmail());
                    List<DiaChi> lstDC = diaChiRepository.findDiaChiByIdNd(nguoiDung.getId());
                    for(DiaChi dc: lstDC){
                        if(dc.getTrangthai()== true){
                            session.setAttribute("diachimacdinh",dc);
                            break;
                        }
                    }
                    if (lstDC.size() > 0) {
                        model.addAttribute("lstDC", lstDC);
                    }
                    // Lấy giỏ hàng của người dùng đã đăng nhập
                    khachHang = khachHangGioHangRepository.findByNguoidung(nguoiDung.getId());
                    if (khachHang != null) {
                        List<KhachHangPhieuGiam> allKHPG = khachHangPhieuGiamRepository.findKhachHangPhieuGiamByIdKhachHang(khachHang.getId());
                        if (allKHPG.size() > 0) {
                            for (KhachHangPhieuGiam j : allKHPG) {
                                PhieuGiamGia p = new PhieuGiamGia();
                                p = phieuGiamGiaImp.findPhieuGiamGiaById(j.getPhieugiamgia().getId());
                                lstPGG.add(p);
                            }
                        }
                        gioHang = gioHangRepository.findByKhachhang(khachHang);
                        if (gioHang != null) {
                            cartItems = gioHang.getGioHangChiTietList();
                        }

                        session.setAttribute("lstPGG", lstPGG);
                        model.addAttribute("lstPGG", lstPGG);
                    }
                }
            }
        } else {
            // Giỏ hàng của người dùng chưa đăng nhập
            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
            session.setAttribute("lstPGG", lstPGG);
            model.addAttribute("lstPGG", lstPGG);
        }
        // Tính tổng số lượng sản phẩm và tổng tiền
        int totalQuantity = 0;
        BigDecimal totalAmount = (BigDecimal) session.getAttribute("totalAmount");
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
//            BigDecimal giatien = sanPhamChiTietRepository.findPriceByProductId(item.getSanphamchitiet().getId());
//            totalAmount = totalAmount.add(giatien.multiply(BigDecimal.valueOf(item.getSoluong())));
        }
        Map<Integer, BigDecimal> discountedPrices = (Map<Integer, BigDecimal>) session.getAttribute("discountedPrices");
        model.addAttribute("token", token);
        session.setAttribute("tokenTT", token);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("discountedPrices", discountedPrices);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("cartItems", cartItems);
        return "customer/thanhtoan";
    }

    public static String convertCurrency(String formattedAmount) {
        // Xóa ký hiệu "₫" và các dấu phân cách
        String numericAmount = formattedAmount.replaceAll("[^\\d]", "");
        return numericAmount;
    }

    @PostMapping("customer/thanh-toan-online-khach-le")
    public String thanhtoanonlinekhachle(@ModelAttribute("diachikotaikhoan") DiaChiThanhToanNoTaiKhoanOnline diachikotaikhoan,
                                         @RequestParam("maCodeGiam") String maCodeGiam,
                                         @RequestParam("phivanchuyen") String phivanchuyen,
                                         @RequestParam("ngaygiaodukien") String ngaygiaodukien,
                                         @RequestParam("tongtien") String tongtien,
                                         @RequestParam("tamtinh") String tamtinh,
                                         @RequestParam("sotiengiam") String sotiengiam,
                                         @RequestParam("laydiachidaluu") String laydiachidaluu,
                                         HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        NguoiDung nguoiDung = null;
        KhachHang khachHang = null;
        GioHang gioHang = null;
        Integer IDHDCamOn=0;
        String MaHDCamOn="";
        if (token != null) {
            // Lấy danh sách token từ session
            List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
            if (taiKhoanTokenInfos.size() > 0) {
                // Tìm người dùng từ danh sách token
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

                        //Hóa đơn
                        HoaDon hoaDon = new HoaDon();

                        //fake nhân viên
                        NhanVien nvfake = new NhanVien();
                        nvfake.setId(5);
                        hoaDon.setNhanvien(nvfake);
                        hoaDon.setHoatoc(false);
                        hoaDon.setSdt(diachikotaikhoan.getSodienthoai());
                        //Tạo mã hóa đươn mới
                        HoaDon hdMaGet = daoHD.timBanGhiDuocTaoGanNhat();
                        Integer maMoi = Integer.valueOf(hdMaGet.getMahoadon().trim().substring(8)) + 1;
                        hoaDon.setMahoadon("HDFSPORT" + maMoi);
                        //set địa chỉ
                        hoaDon.setDiachi(diachikotaikhoan.getDiachicuthe() + ", " + diachikotaikhoan.getPhuongxa() + ", " + diachikotaikhoan.getQuanhuyen() + ", " + diachikotaikhoan.getTinhthanhpho());
                        hoaDon.setEmail(nguoiDung.getEmail());
                        hoaDon.setKhachhang(khachHang);
                        hoaDon.setTongtien(new BigDecimal(convertCurrency(tongtien)));

                        // Định dạng ngày tháng cho chuỗi đầu vào
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            // Chuyển đổi chuỗi ngày tháng sang đối tượng Date
                            Date parsedDate = dateFormat.parse(ngaygiaodukien);

                            // Lấy Unix timestamp từ đối tượng Date
                            long unixTimestamp = parsedDate.getTime() / 1000L; // Chia cho 1000 để lấy giá trị Unix timestamp

                            // Tạo đối tượng Timestamp từ Unix timestamp
                            Timestamp ngaygiaodukienTimestamp = new Timestamp(unixTimestamp * 1000L); // Nhân lại cho 1000 để lấy giá trị milliseconds
                            hoaDon.setNgaygiaodukien(ngaygiaodukienTimestamp);
                            // In ra kết quả
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        hoaDon.setLoaihoadon(true);

                        hoaDon.setPhivanchuyen(new BigDecimal(convertCurrency(phivanchuyen)));
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        hoaDon.setNgaytao(Timestamp.valueOf(currentDateTime));
                        hoaDon.setLancapnhatcuoi(Timestamp.valueOf(currentDateTime));
                        hoaDon.setTennguoinhan(diachikotaikhoan.getHovaten());
                        hoaDon.setTrangthai(0);
                        daoHD.capNhatHD(hoaDon);

                        //Thêm hóa đơn ci tiết
                        gioHang = gioHangRepository.findByKhachhang(khachHang);
                        List<GioHangChiTiet> lstGHCT = gioHang.getGioHangChiTietList();

                        HoaDon hdMoiThem = daoHD.timBanGhiDuocTaoGanNhat();
                        IDHDCamOn = hdMoiThem.getId();
                        MaHDCamOn = hdMoiThem.getMahoadon();
                        for (GioHangChiTiet g : lstGHCT) {
                            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                            hoaDonChiTiet.setHoadon(hdMoiThem);
                            hoaDonChiTiet.setSanphamchitiet(g.getSanphamchitiet());

                            List<SanPhamDotGiam> lst = SPdotgiamRepo.findBySanphamchitiet(g.getSanphamchitiet());
                            Integer discounts = 0;
                            Integer discountbacks = 0;
                            if (lst.size() > 0) {
                                for (SanPhamDotGiam a : lst
                                ) {
                                    if (a.getDotgiamgia().getTrangthai() == 1) {
                                        discounts = a.getDotgiamgia().getGiatrigiam();
                                        discountbacks = 100 - discounts;
                                    }
                                }
                            }
                            if (discounts > 0) {
                                hoaDonChiTiet.setGiasanpham((g.getSanphamchitiet().getGiatien().divide(new BigDecimal("100"))).multiply(new BigDecimal(discountbacks+"")));
                            } else {
                                hoaDonChiTiet.setGiasanpham(g.getSanphamchitiet().getGiatien());
                            }

                            hoaDonChiTiet.setSoluong(g.getSoluong());
                            hoaDonChiTiet.setTrangthai(true);
                            hoaDonChiTietImp.capnhat(hoaDonChiTiet);
                        }
                        //Xóa đối tượng trong list gio hàng
                        gioHangChiTietRepository.deleteGioHangChiTietTT(gioHang.getId());
                        //Xác định khách dùng phiếu giả hay không
                        if (maCodeGiam.equals("khong")) {

                        } else {
                            PhieuGiamGia phieuGiamGia = phieuGiamGiaImp.findPhieuGiamGiaByMaCode(maCodeGiam);
                            phieuGiamGia.setId(phieuGiamGia.getId());
                            //Giam so luong phieu giam
                            phieuGiamGia.setSoluong(phieuGiamGia.getSoluong() - 1);
                            phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
                            //Them phieu giam chi tiet
                            PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = new PhieuGiamGiaChiTiet();
                            phieuGiamGiaChiTiet.setPhieugiamgia(phieuGiamGia);
                            phieuGiamGiaChiTiet.setHoadon(hdMoiThem);
                            phieuGiamGiaChiTiet.setGiabandau(new BigDecimal(convertCurrency(tamtinh)));

                            phieuGiamGiaChiTiet.setGiasauapdung(new BigDecimal(Integer.parseInt(convertCurrency(tamtinh)) - Integer.parseInt(convertCurrency(sotiengiam))));
                            phieuGiamGiaChiTiet.setTiengiam(new BigDecimal(convertCurrency(sotiengiam)));
                            phieuGiamGiaChiTiet.setNgaytao(Timestamp.valueOf(currentDateTime));
                            phieuGiamGiaChiTiet.setNguoitao("Tuan Anh");
                            daoPGGCT.save(phieuGiamGiaChiTiet);
                        }

                        //Them lich su hoa don cho xac nhan
                        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
                        lichSuHoaDon.setNhanvien(nvfake);
                        lichSuHoaDon.setNguoitao("Tuan Anh");
                        lichSuHoaDon.setGhichu("Khách hàng đã đặt đơn hàng");
                        lichSuHoaDon.setHoadon(hdMoiThem);
                        lichSuHoaDon.setNgaytao(Timestamp.valueOf(currentDateTime));
                        lichSuHoaDon.setTrangthai(0);
                        daoLSHD.add(lichSuHoaDon);

                        //Phuong thuc thanh toan
                        PhuongThucThanhToan phuongthuc = new PhuongThucThanhToan();
                        phuongthuc.setTenphuongthuc("Trả sau");
                        phuongthuc.setMota("Tiền mặt");
                        phuongthuc.setHoadon(hdMoiThem);
                        phuongthuc.setNgaytao(Timestamp.valueOf(currentDateTime));
                        phuongthuc.setTongtien(hdMoiThem.getTongtien());
                        phuongthuc.setMagiaodichvnpay("N/A");
                        //fake người tạo
                        phuongthuc.setNguoitao("Tuan Anh");
                        phuongthuc.setTrangthai(false);
                        daoPTTT.add_update(phuongthuc);

                        //Luu dia chi
                        if(Integer.parseInt(laydiachidaluu)==-1){
                            DiaChi diachi = new DiaChi();
                            diachi.setTenduong(diachikotaikhoan.getDiachicuthe());
                            diachi.setXaphuong(diachikotaikhoan.getPhuongxa());
                            diachi.setQuanhuyen(diachikotaikhoan.getQuanhuyen());
                            diachi.setTinhthanhpho(diachikotaikhoan.getTinhthanhpho());
                            diachi.setSdtnguoinhan(diachikotaikhoan.getSodienthoai());
                            diachi.setHotennguoinhan(diachikotaikhoan.getHovaten());
                            diachi.setNgaytao(Timestamp.valueOf(currentDateTime));
                            diachi.setNguoitao(diachikotaikhoan.getHovaten());
                            diachi.setLancapnhatcuoi(Timestamp.valueOf(currentDateTime));
                            diachi.setNguoicapnhat(diachikotaikhoan.getHovaten());
                            diachi.setTrangthai(false);
                            diachi.setNguoidung(nguoiDung);
                            diaChiRepository.save(diachi);

                        }else{
                            DiaChi diachiTim= diaChiRepository.findDiaChiByIdDiaChiTA(Integer.parseInt(laydiachidaluu));
                            diachiTim.setTenduong(diachikotaikhoan.getDiachicuthe());
                            diachiTim.setXaphuong(diachikotaikhoan.getPhuongxa());
                            diachiTim.setQuanhuyen(diachikotaikhoan.getQuanhuyen());
                            diachiTim.setTinhthanhpho(diachikotaikhoan.getTinhthanhpho());
                            diachiTim.setSdtnguoinhan(diachikotaikhoan.getSodienthoai());
                            diachiTim.setHotennguoinhan(diachikotaikhoan.getHovaten());
                            diachiTim.setLancapnhatcuoi(Timestamp.valueOf(currentDateTime));
                            diachiTim.setNguoicapnhat(diachikotaikhoan.getHovaten());
                            diaChiRepository.save(diachiTim);
                        }
                    }
                }
            }
            model.addAttribute("MaHDCamOn",MaHDCamOn);
            model.addAttribute("IDHDCamOn",IDHDCamOn);
            return "customer/camondathang";
        } else {
            //Hóa đơn
            HoaDon hoaDon = new HoaDon();

            //fake nhân viên
            NhanVien nvfake = new NhanVien();
            nvfake.setId(5);
            hoaDon.setNhanvien(nvfake);
            hoaDon.setHoatoc(false);
            hoaDon.setSdt(diachikotaikhoan.getSodienthoai());
            //Tạo mã hóa đươn mới
            HoaDon hdMaGet = daoHD.timBanGhiDuocTaoGanNhat();
            Integer maMoi = Integer.valueOf(hdMaGet.getMahoadon().trim().substring(8)) + 1;
            hoaDon.setMahoadon("HDFSPORT" + maMoi);
            //set địa chỉ
            hoaDon.setDiachi(diachikotaikhoan.getDiachicuthe() + ", " + diachikotaikhoan.getPhuongxa() + ", " + diachikotaikhoan.getQuanhuyen() + ", " + diachikotaikhoan.getTinhthanhpho());
            hoaDon.setEmail(diachikotaikhoan.getEmail());
            hoaDon.setTongtien(new BigDecimal(convertCurrency(tongtien)));

            // Định dạng ngày tháng cho chuỗi đầu vào
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                // Chuyển đổi chuỗi ngày tháng sang đối tượng Date
                Date parsedDate = dateFormat.parse(ngaygiaodukien);

                // Lấy Unix timestamp từ đối tượng Date
                long unixTimestamp = parsedDate.getTime() / 1000L; // Chia cho 1000 để lấy giá trị Unix timestamp

                // Tạo đối tượng Timestamp từ Unix timestamp
                Timestamp ngaygiaodukienTimestamp = new Timestamp(unixTimestamp * 1000L); // Nhân lại cho 1000 để lấy giá trị milliseconds
                hoaDon.setNgaygiaodukien(ngaygiaodukienTimestamp);
                // In ra kết quả
            } catch (ParseException e) {
                e.printStackTrace();
            }


            hoaDon.setLoaihoadon(true);

            hoaDon.setPhivanchuyen(new BigDecimal(convertCurrency(phivanchuyen)));
            LocalDateTime currentDateTime = LocalDateTime.now();
            hoaDon.setNgaytao(Timestamp.valueOf(currentDateTime));
            hoaDon.setLancapnhatcuoi(Timestamp.valueOf(currentDateTime));
            hoaDon.setTennguoinhan(diachikotaikhoan.getHovaten());
            hoaDon.setTrangthai(0);
            daoHD.capNhatHD(hoaDon);

            //Thêm hóa đơn ci tiết
            List<GioHangChiTiet> lstGHCT = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            HoaDon hdMoiThem = daoHD.timBanGhiDuocTaoGanNhat();
            IDHDCamOn = hdMoiThem.getId();
            MaHDCamOn = hdMoiThem.getMahoadon();
            for (GioHangChiTiet g : lstGHCT) {
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                hoaDonChiTiet.setHoadon(hdMoiThem);
                hoaDonChiTiet.setSanphamchitiet(g.getSanphamchitiet());

                List<SanPhamDotGiam> lst = SPdotgiamRepo.findBySanphamchitiet(g.getSanphamchitiet());
                Integer discounts = 0;
                Integer discountbacks = 0;
                if (lst.size() > 0) {
                    for (SanPhamDotGiam a : lst
                    ) {
                        if (a.getDotgiamgia().getTrangthai() == 1) {
                            discounts = a.getDotgiamgia().getGiatrigiam();
                            discountbacks = 100 - discounts;
                        }
                    }
                }
                if (discounts > 0) {
                    hoaDonChiTiet.setGiasanpham((g.getSanphamchitiet().getGiatien().divide(new BigDecimal("100"))).multiply(new BigDecimal(discountbacks+"")));
                } else {
                    hoaDonChiTiet.setGiasanpham(g.getSanphamchitiet().getGiatien());
                }

                hoaDonChiTiet.setSoluong(g.getSoluong());
                hoaDonChiTiet.setTrangthai(true);
                hoaDonChiTietImp.capnhat(hoaDonChiTiet);

            }
            //Xóa đối tượng trong list gio hàng
            gioHangService.clearListGioHangKhongTK();
            //Xác định khách dùng phiếu giả hay không
            if (maCodeGiam.equals("khong")) {

            } else {
                PhieuGiamGia phieuGiamGia = phieuGiamGiaImp.findPhieuGiamGiaByMaCode(maCodeGiam);
                phieuGiamGia.setId(phieuGiamGia.getId());
                //Giam so luong phieu giam
                phieuGiamGia.setSoluong(phieuGiamGia.getSoluong() - 1);
                phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
                //Them phieu giam chi tiet
                PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = new PhieuGiamGiaChiTiet();
                phieuGiamGiaChiTiet.setPhieugiamgia(phieuGiamGia);
                phieuGiamGiaChiTiet.setHoadon(hdMoiThem);
                phieuGiamGiaChiTiet.setGiabandau(new BigDecimal(convertCurrency(tamtinh)));

                phieuGiamGiaChiTiet.setGiasauapdung(new BigDecimal(Integer.parseInt(convertCurrency(tamtinh)) - Integer.parseInt(convertCurrency(sotiengiam))));
                phieuGiamGiaChiTiet.setTiengiam(new BigDecimal(convertCurrency(sotiengiam)));
                phieuGiamGiaChiTiet.setNgaytao(Timestamp.valueOf(currentDateTime));
                phieuGiamGiaChiTiet.setNguoitao("Tuan Anh");
                daoPGGCT.save(phieuGiamGiaChiTiet);
            }

            //Them lich su hoa don cho xac nhan
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setNhanvien(nvfake);
            lichSuHoaDon.setNguoitao("Tuan Anh");
            lichSuHoaDon.setGhichu("Khách hàng đã đặt đơn hàng");
            lichSuHoaDon.setHoadon(hdMoiThem);
            lichSuHoaDon.setNgaytao(Timestamp.valueOf(currentDateTime));
            lichSuHoaDon.setTrangthai(0);
            daoLSHD.add(lichSuHoaDon);

            //Phuong thuc thanh toan
            PhuongThucThanhToan phuongthuc = new PhuongThucThanhToan();
            phuongthuc.setTenphuongthuc("Trả sau");
            phuongthuc.setMota("Tiền mặt");
            phuongthuc.setHoadon(hdMoiThem);
            phuongthuc.setNgaytao(Timestamp.valueOf(currentDateTime));
            phuongthuc.setTongtien(hdMoiThem.getTongtien());
            phuongthuc.setMagiaodichvnpay("N/A");
            //fake người tạo
            phuongthuc.setNguoitao("Tuan Anh");
            phuongthuc.setTrangthai(false);
            daoPTTT.add_update(phuongthuc);


            model.addAttribute("MaHDCamOn",MaHDCamOn);
            model.addAttribute("IDHDCamOn",IDHDCamOn);
            return "customer/camondathang";
        }

    }
}
