package com.example.demo.controller.thanhtoanmuaonline;

import com.example.demo.controller.thanhtoanmuaonline.ConfigVN_Pay.VNPAYService;
import com.example.demo.entity.*;
import com.example.demo.info.DiaChiThanhToanNoTaiKhoanOnline;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.info.ThongTinHoaDonOnline;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.repository.KhachHangPhieuGiamRepository;
import com.example.demo.repository.PhieuGiamGiaChiTiet.PhieuGiamChiTietRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.repository.giohang.KhachHangGioHangRepository;
import com.example.demo.repository.giohang.NguoiDungGioHangRepository;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.LichSuHoaDonService;
import com.example.demo.service.PhuongThucThanhToanService;
import com.example.demo.service.impl.HoaDonChiTietImp;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Controller
public class ThanhToanVNPay {
    @Autowired
    private VNPAYService vnPayService;
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

    public static String convertCurrency(String formattedAmount) {
        // Xóa ký hiệu "₫" và các dấu phân cách
        String numericAmount = formattedAmount.replaceAll("[^\\d]", "");
        return numericAmount;
    }
    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") String orderTotal1,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request,
                              @ModelAttribute("diachikotaikhoan") DiaChiThanhToanNoTaiKhoanOnline diachikotaikhoan,
                              @RequestParam("maCodeGiamVNPay") String maCodeGiam,
                              @RequestParam("phivanchuyenVNPay") String phivanchuyen,
                              @RequestParam("ngaygiaodukienVNPay") String ngaygiaodukien,
                              @RequestParam("tongtienVNPay") String tongtien,
                              @RequestParam("tamtinhVNPay") String tamtinh,
                              @RequestParam("sotiengiamVNPay") String sotiengiam,
                              HttpSession session){
        ThongTinHoaDonOnline thongTinHoaDonOnline= new ThongTinHoaDonOnline(maCodeGiam,phivanchuyen,ngaygiaodukien,tongtien,tamtinh,sotiengiam);

        int orderTotal= Integer.parseInt(convertCurrency(orderTotal1));
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);

        session.setAttribute("diachiVNpay",diachikotaikhoan);
        session.setAttribute("thongtinhoadonVNPay",thongTinHoaDonOnline);
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String GetMapping(HttpServletRequest request, Model model, HttpSession session){
        int paymentStatus =vnPayService.orderReturn(request);

        DiaChiThanhToanNoTaiKhoanOnline diachikotaikhoan= (DiaChiThanhToanNoTaiKhoanOnline) session.getAttribute("diachiVNpay");
        ThongTinHoaDonOnline thongTinHoaDonOnline= (ThongTinHoaDonOnline) session.getAttribute("thongtinhoadonVNPay");

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        String token = (String) session.getAttribute("token");
        NguoiDung nguoiDung = null;
        KhachHang khachHang = null;
        GioHang gioHang = null;
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
                        hoaDon.setTongtien(new BigDecimal(convertCurrency(thongTinHoaDonOnline.getTongtien())));

                        // Định dạng ngày tháng cho chuỗi đầu vào
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            // Chuyển đổi chuỗi ngày tháng sang đối tượng Date
                            Date parsedDate = dateFormat.parse(thongTinHoaDonOnline.getNgaygiaodukien());

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

                        hoaDon.setPhivanchuyen(new BigDecimal(convertCurrency(thongTinHoaDonOnline.getPhivanchuyen())));
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        hoaDon.setNgaytao(Timestamp.valueOf(currentDateTime));
                        hoaDon.setTennguoinhan(diachikotaikhoan.getHovaten());
                        hoaDon.setTrangthai(0);
                        daoHD.capNhatHD(hoaDon);

                        //Thêm hóa đơn ci tiết
                        gioHang = gioHangRepository.findByKhachhang(khachHang);
                        List<GioHangChiTiet> lstGHCT = gioHang.getGioHangChiTietList();

                        HoaDon hdMoiThem = daoHD.timBanGhiDuocTaoGanNhat();
                        for (GioHangChiTiet g : lstGHCT) {
                            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                            hoaDonChiTiet.setHoadon(hdMoiThem);
                            hoaDonChiTiet.setSanphamchitiet(g.getSanphamchitiet());
                            hoaDonChiTiet.setGiasanpham(g.getSanphamchitiet().getGiatien());
                            hoaDonChiTiet.setSoluong(g.getSoluong());
                            hoaDonChiTiet.setTrangthai(true);
                            hoaDonChiTietImp.capnhat(hoaDonChiTiet);

                        }
                        //Xóa đối tượng trong list gio hàng

                        //Xác định khách dùng phiếu giả hay không
                        if (thongTinHoaDonOnline.getMaCodeGiam().equals("khong")) {

                        } else {
                            PhieuGiamGia phieuGiamGia = phieuGiamGiaImp.findPhieuGiamGiaByMaCode(thongTinHoaDonOnline.getMaCodeGiam());
                            phieuGiamGia.setId(phieuGiamGia.getId());
                            //Giam so luong phieu giam
                            phieuGiamGia.setSoluong(phieuGiamGia.getSoluong() - 1);
                            phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
                            //Them phieu giam chi tiet
                            PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = new PhieuGiamGiaChiTiet();
                            phieuGiamGiaChiTiet.setPhieugiamgia(phieuGiamGia);
                            phieuGiamGiaChiTiet.setHoadon(hdMoiThem);
                            phieuGiamGiaChiTiet.setGiabandau(new BigDecimal(convertCurrency(thongTinHoaDonOnline.getTamtinh())));

                            phieuGiamGiaChiTiet.setGiasauapdung(new BigDecimal(Integer.parseInt(convertCurrency(thongTinHoaDonOnline.getTamtinh())) - Integer.parseInt(convertCurrency(thongTinHoaDonOnline.getSotiengiam()))));
                            phieuGiamGiaChiTiet.setTiengiam(new BigDecimal(convertCurrency(thongTinHoaDonOnline.getSotiengiam())));
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
                        phuongthuc.setTenphuongthuc("Trả trước");
                        phuongthuc.setMota("VNPay");
                        phuongthuc.setHoadon(hdMoiThem);
                        phuongthuc.setNgaytao(Timestamp.valueOf(currentDateTime));
                        phuongthuc.setTongtien(hdMoiThem.getTongtien());
                        phuongthuc.setMagiaodichvnpay(transactionId);
                        //fake người tạo
                        phuongthuc.setNguoitao("Tuan Anh");
                        phuongthuc.setTrangthai(true);
                        daoPTTT.add_update(phuongthuc);
                    }
                }
            }
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
            hoaDon.setTongtien(new BigDecimal(convertCurrency(thongTinHoaDonOnline.getTongtien())));

            // Định dạng ngày tháng cho chuỗi đầu vào
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                // Chuyển đổi chuỗi ngày tháng sang đối tượng Date
                Date parsedDate = dateFormat.parse(thongTinHoaDonOnline.getNgaygiaodukien());

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

            hoaDon.setPhivanchuyen(new BigDecimal(convertCurrency(thongTinHoaDonOnline.getPhivanchuyen())));
            LocalDateTime currentDateTime = LocalDateTime.now();
            hoaDon.setNgaytao(Timestamp.valueOf(currentDateTime));
            hoaDon.setTennguoinhan(diachikotaikhoan.getHovaten());
            hoaDon.setTrangthai(0);
            daoHD.capNhatHD(hoaDon);

            //Thêm hóa đơn ci tiết
            List<GioHangChiTiet> lstGHCT = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            HoaDon hdMoiThem = daoHD.timBanGhiDuocTaoGanNhat();
            for (GioHangChiTiet g : lstGHCT) {
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                hoaDonChiTiet.setHoadon(hdMoiThem);
                hoaDonChiTiet.setSanphamchitiet(g.getSanphamchitiet());
                hoaDonChiTiet.setGiasanpham(g.getSanphamchitiet().getGiatien());
                hoaDonChiTiet.setSoluong(g.getSoluong());
                hoaDonChiTiet.setTrangthai(true);
                hoaDonChiTietImp.capnhat(hoaDonChiTiet);

            }
            //Xóa đối tượng trong list gio hàng

            //Xác định khách dùng phiếu giả hay không
            if (thongTinHoaDonOnline.getMaCodeGiam().equals("khong")) {

            } else {
                PhieuGiamGia phieuGiamGia = phieuGiamGiaImp.findPhieuGiamGiaByMaCode(thongTinHoaDonOnline.getMaCodeGiam());
                phieuGiamGia.setId(phieuGiamGia.getId());
                //Giam so luong phieu giam
                phieuGiamGia.setSoluong(phieuGiamGia.getSoluong() - 1);
                phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
                //Them phieu giam chi tiet
                PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = new PhieuGiamGiaChiTiet();
                phieuGiamGiaChiTiet.setPhieugiamgia(phieuGiamGia);
                phieuGiamGiaChiTiet.setHoadon(hdMoiThem);
                phieuGiamGiaChiTiet.setGiabandau(new BigDecimal(convertCurrency(thongTinHoaDonOnline.getTamtinh())));

                phieuGiamGiaChiTiet.setGiasauapdung(new BigDecimal(Integer.parseInt(convertCurrency(thongTinHoaDonOnline.getTamtinh())) - Integer.parseInt(convertCurrency(thongTinHoaDonOnline.getSotiengiam()))));
                phieuGiamGiaChiTiet.setTiengiam(new BigDecimal(convertCurrency(thongTinHoaDonOnline.getSotiengiam())));
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
            phuongthuc.setTenphuongthuc("Trả trước");
            phuongthuc.setMota("VNPay");
            phuongthuc.setHoadon(hdMoiThem);
            phuongthuc.setNgaytao(Timestamp.valueOf(currentDateTime));
            phuongthuc.setTongtien(hdMoiThem.getTongtien());
            phuongthuc.setMagiaodichvnpay(transactionId);
            //fake người tạo
            phuongthuc.setNguoitao("Tuan Anh");
            phuongthuc.setTrangthai(true);
            daoPTTT.add_update(phuongthuc);

        }

        return paymentStatus == 1 ? "customer/orderSuccess" : "customer/orderFail";
    }
}
