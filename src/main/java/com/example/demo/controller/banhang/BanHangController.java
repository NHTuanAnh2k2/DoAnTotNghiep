package com.example.demo.controller.banhang;

import com.example.demo.entity.*;
import com.example.demo.info.*;
import com.example.demo.info.token.AdminManager;
import com.example.demo.repository.*;
import com.example.demo.repository.PhieuGiamGiaChiTiet.PhieuGiamChiTietRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import com.example.demo.service.*;
import com.example.demo.service.impl.NguoiDungImpl1;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping("ban-hang-tai-quay")
public class BanHangController {
    @Autowired
    SanPhamDotGiamRepository SPdotgiamRepo;
    @Autowired
    NhanVienRepository nhanvienRPo;
    @Autowired
    LichSuHoaDonService daoLSHD;
    @Autowired
    SpringTemplateEngine dao1;
    @Autowired
    PhieuGiamChiTietRepository daoPGGCT;
    @Autowired
    PhieuGiamGiaRepository daoPGG;
    @Autowired
    PhuongThucThanhToanService daoPTTT;
    @Autowired
    KhachHangPhieuGiamRepository daoKHPG;
    @Autowired
    NguoiDungRepository daoNguoiDung;
    @Autowired
    DiaChiRepository daoDiaChi;
    @Autowired
    SanPhamChiTietService daoSPCT;
    @Autowired
    KhachHangRepostory daoKH;
    @Autowired
    NguoiDungImpl1 nguoiDung;

    @Autowired
    HoaDonService daoHD;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HoaDonChiTietService daoHDCT;
    private HoaDon hdHienTai;
    //chứa bản ghi thanh toán tạm
    List<PhuongThucThanhToan> lstPTTT = new ArrayList<>();
    //chứa bản ghi của phiếu giảm
    PhieuGiamGia phieugiamsaoluu = new PhieuGiamGia();
    //chứa tổng tiền của đơn hiện tại
    BigDecimal tongtienhoadonhientai = new BigDecimal("0");
    BigDecimal sotiengiam = new BigDecimal("0");
    MauHoaDon billTam = new MauHoaDon();
    //hóa đơn lưu tạm tính bill
    HoaDon hoaDonCheckBill;

    private static String encodeFileToBase64Binary(File file) throws IOException {
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStreamReader.read(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }


    @GetMapping("hoa-don-cho")
    @ResponseBody
    public ResponseEntity<?> getLstCho() {
        List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
        if (lst.size() > 0) {
            hdHienTai = lst.get(0);
        }
        return ResponseEntity.ok(lst);
    }

    @GetMapping("tao-don-cho")
    @ResponseBody
    public ResponseEntity<?> createNewHD(HttpSession session) {
        HoaDon hoadonCho = new HoaDon();
        hoadonCho.setTrangthai(0);
        hoadonCho.setLoaihoadon(false);
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);
        // fake id nhân viên sau này sẽ lấy thì login xuống
        hoadonCho.setNhanvien(nv);
        HoaDon hdMaGet = daoHD.timBanGhiDuocTaoGanNhat();
        Integer maMoi = Integer.valueOf(hdMaGet.getMahoadon().trim().substring(8)) + 1;
        hoadonCho.setMahoadon("HDFSPORT" + maMoi);
        LocalDateTime currentDateTime = LocalDateTime.now();
        hoadonCho.setNgaytao(Timestamp.valueOf(currentDateTime));
        hoadonCho.setHoatoc(false);
        hoadonCho.setNguoitao(nv.getNguoidung().getHovaten());
        daoHD.capNhatHD(hoadonCho);
        List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
        if (lst.size() > 0) {
            hdHienTai = lst.get(0);
        } else {
            hdHienTai = new HoaDon();
        }
        return ResponseEntity.ok(lst);
    }

    @GetMapping("tim-coutSP-theoHD")
    @ResponseBody
    public ResponseEntity<?> timCoutSPTheoMaHD(@RequestParam("maHD") String maHD
    ) {
        lstPTTT = new ArrayList<>();
        hdHienTai = daoHD.timHDTheoMaHD(maHD);
        List<HoaDonChiTiet> lst = daoHDCT.timDSHDTCTTheoMaHD(maHD);
        return ResponseEntity.ok(lst);
    }

    @GetMapping("tim-HD-TheoMaHD")
    @ResponseBody
    public ResponseEntity<?> timHDTheoMaHD(@RequestParam("maHD") String maHD
    ) {
        HoaDon hd = daoHD.timHDTheoMaHD(maHD);
        hdHienTai = hd;
        return ResponseEntity.ok(hd);
    }

    @GetMapping("viewNVChanges")
    @ResponseBody
    public ResponseEntity<?> getlistKH(@RequestParam("pageNVChanges") Optional<Integer> pageParam) {
        Pageable p = PageRequest.of(pageParam.orElse(0), 5);
        Page<KhachHang> pageNV = daoKH.findAll(p);
        return ResponseEntity.ok(pageNV);
    }

    // lựa chọn khách hàng tại hóa đơn tại quầy khi khách hàng cung cấp thông tin
    @GetMapping("ChoseKH/{id}")
    public String choseNV(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (this.hdHienTai == null) {
            redirectAttributes.addFlashAttribute("checkChoseKH", true);
            return "redirect:/hoa-don/ban-hang";
        }
        HoaDon hdset = hdHienTai;
        Optional<KhachHang> kh = daoKH.findById(id);
        KhachHang khach = kh.get();
        DiaChi diaChi = daoDiaChi.TimIdNguoiDungMacDinh(khach.getNguoidung().getId());
        hdset.setKhachhang(kh.get());
        hdset.setTennguoinhan(diaChi.getHotennguoinhan());
        hdset.setSdt(diaChi.getSdtnguoinhan());
        hdset.setEmail(khach.getNguoidung().getEmail());
        hdset.setDiachi(diaChi.getTenduong() + ", " + diaChi.getXaphuong() + ", " + diaChi.getQuanhuyen() + ", " + diaChi.getTinhthanhpho());
        daoHD.capNhatHD(hdset);
        model.addAttribute("check", hdHienTai.getMahoadon());
        return "redirect:/hoa-don/ban-hang";
    }

    @GetMapping("searchNV")
    @ResponseBody
    public ResponseEntity<?> timlistKH(@RequestParam("keySearch") String key) {
        List<KhachHang> pageNV = daoKH.timNVTheoMa(key);
        return ResponseEntity.ok(pageNV);
    }


    @GetMapping("checkSPQR")
    @ResponseBody
    public ResponseEntity<?> checkSPQR(@RequestParam("ma") String ma) {
        Boolean found = daoSPCT.checkSPQR(ma);
        Boolean result = false;
        if (found == true) {
            SanPhamChiTiet spct = daoSPCT.findBySanPhambyMa(ma).get(0);
            if (spct.getSoluong() > 0) {
                result = true;
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("checksdt")
    @ResponseBody
    public ResponseEntity<?> checksdt(@RequestParam("sdt") String sdt) {
        return ResponseEntity.ok(daoNguoiDung.existsBySodienthoai(sdt.trim()));
    }

    @GetMapping("checkemail")
    @ResponseBody
    public ResponseEntity<?> checkemail(@RequestParam("email") String email) {
        return ResponseEntity.ok(daoNguoiDung.existsByEmail(email.trim()));
    }

    @GetMapping("discounts/{id}")
    @ResponseBody
    public ResponseEntity<?> discounts(@PathVariable("id") Integer id) {
        SanPhamChiTiet spctTim = new SanPhamChiTiet();
        spctTim.setId(id);
        List<SanPhamDotGiam> lst = SPdotgiamRepo.findBySanphamchitiet(spctTim);
        Integer discounts = 0;
        for (SanPhamDotGiam a : lst
        ) {
            if (a.getDotgiamgia().getTrangthai() == 1) {
                discounts = a.getDotgiamgia().getGiatrigiam();
            }
        }
        return ResponseEntity.ok(discounts);
    }

    // thêm sản phẩm tại hdct
    @GetMapping("ChoseSP/{id}")
    public String choseSP(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        if (this.hdHienTai == null) {
            redirectAttributes.addFlashAttribute("checkChoseSp", true);
            return "redirect:/hoa-don/ban-hang";
        }
        SanPhamChiTiet spct = daoSPCT.findById(id);
        SanPhamChiTiet spctCapNhatSL = spct;
        spctCapNhatSL.setSoluong(spctCapNhatSL.getSoluong() - 1);
        List<HoaDon> hd = daoHD.timTheoID(hdHienTai.getId());
        HoaDon hdset = hd.get(0);
        Boolean result = daoHDCT.checkHDCT(hdset, spct);

        if (result == true) {
            List<HoaDonChiTiet> lstTim = daoHDCT.timHDCT(hdset, spct);
            HoaDonChiTiet hdct = lstTim.get(0);
            int sl = hdct.getSoluong() + 1;
            hdct.setSoluong(sl);
            daoSPCT.addSPCT(spctCapNhatSL);
            daoHDCT.capnhat(hdct);
            redirectAttributes.addFlashAttribute("choseUpdate", true);
            return "redirect:/hoa-don/ban-hang";
        }
        HoaDonChiTiet hdctNew = new HoaDonChiTiet();
        hdctNew.setHoadon(hdset);
        hdctNew.setSanphamchitiet(spct);
        hdctNew.setSoluong(1);
        hdctNew.setTrangthai(true);
        hdctNew.setGiasanpham(spct.getGiatien());
        daoSPCT.addSPCT(spctCapNhatSL);
        daoHDCT.capnhat(hdctNew);
        redirectAttributes.addFlashAttribute("choseSPsucsess", true);
        return "redirect:/hoa-don/ban-hang";
    }

    //chọn sản phẩm bằng QR
    @GetMapping("ChoseSPQR/{id}")
    public String choseSPQR(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        if (this.hdHienTai == null) {
            redirectAttributes.addFlashAttribute("checkChoseSp", true);
            return "redirect:/hoa-don/ban-hang";
        }
        SanPhamChiTiet spct = daoSPCT.findBySanPhambyMa(id).get(0);
        SanPhamChiTiet spctCapNhatSL = spct;
        spctCapNhatSL.setSoluong(spctCapNhatSL.getSoluong() - 1);
        List<HoaDon> hd = daoHD.timTheoID(hdHienTai.getId());
        HoaDon hdset = hd.get(0);
        Boolean result = daoHDCT.checkHDCT(hdset, spct);

        if (result == true) {
            List<HoaDonChiTiet> lstTim = daoHDCT.timHDCT(hdset, spct);
            HoaDonChiTiet hdct = lstTim.get(0);
            int sl = hdct.getSoluong() + 1;
            hdct.setSoluong(sl);
            daoSPCT.addSPCT(spctCapNhatSL);
            daoHDCT.capnhat(hdct);
            redirectAttributes.addFlashAttribute("choseUpdate", true);
            return "redirect:/hoa-don/ban-hang";
        }
        HoaDonChiTiet hdctNew = new HoaDonChiTiet();
        hdctNew.setHoadon(hdset);
        hdctNew.setSanphamchitiet(spct);
        hdctNew.setSoluong(1);
        hdctNew.setTrangthai(true);
        hdctNew.setGiasanpham(spct.getGiatien());
        daoSPCT.addSPCT(spctCapNhatSL);
        daoHDCT.capnhat(hdctNew);
        redirectAttributes.addFlashAttribute("choseSPsucsess", true);
        return "redirect:/hoa-don/ban-hang";
    }

    @GetMapping("deleteHDCho/{id}")
    public String deleteHDCho(@PathVariable("id") String mahd, RedirectAttributes redirectAttributes) {
        HoaDon hd = daoHD.timHDTheoMaHD(mahd);
        //tìm hóa đơn chi tiết
        List<HoaDonChiTiet> listHDCTUpdateSL = daoHDCT.timDSHDTCTTheoMaHD(hd.getMahoadon());
        for (HoaDonChiTiet a : listHDCTUpdateSL
        ) {
            SanPhamChiTiet spctUp = daoSPCT.findById(a.getSanphamchitiet().getId());
            spctUp.setSoluong(spctUp.getSoluong() + a.getSoluong());
            daoSPCT.addSPCT(spctUp);
            daoHDCT.deleteById(a.getId());
        }
        boolean result = daoHD.delete(hd);
        List<HoaDon> lst1 = daoHD.timTheoTrangThaiVaLoai(7, false);
        if (lst1.size() > 0) {
            HoaDon hdChuyen01 = lst1.get(0);
            hdChuyen01.setTrangthai(0);
            daoHD.capNhatHD(hdChuyen01);
        }
        if (result) {
            redirectAttributes.addFlashAttribute("checkdeleteHD", true);
        }
        List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
        if (lst.size() > 0) {
            hdHienTai = lst.get(0);
        } else {
            hdHienTai = null;
        }
        return "redirect:/hoa-don/ban-hang";
    }


    @GetMapping("delete/{id}")
    public String deleteSP(@PathVariable("id") Integer id) {
        HoaDonChiTiet hdDelete = daoHDCT.findByID(id);
        int slHienTai = hdDelete.getSoluong();
        daoHDCT.deleteById(id);
        SanPhamChiTiet spUpdateQuantity = hdDelete.getSanphamchitiet();
        spUpdateQuantity.setSoluong(spUpdateQuantity.getSoluong() + slHienTai);
        daoSPCT.addSPCT(spUpdateQuantity);
        return "redirect:/hoa-don/ban-hang";
    }


    @GetMapping("update/{id}/{sl}")
    public String updateSP(@PathVariable("id") Integer id, @PathVariable("sl") Integer sl) {
        HoaDonChiTiet hdDelete = daoHDCT.findByID(id);
        SanPhamChiTiet spUpdateQuantity = hdDelete.getSanphamchitiet();
        if (hdDelete.getSoluong() == sl) {
            return "redirect:/hoa-don/ban-hang";
        } else {
            if (hdDelete.getSoluong() < sl) {
                //tăng sl
                int sltang = sl - hdDelete.getSoluong();
                spUpdateQuantity.setSoluong(spUpdateQuantity.getSoluong() - sltang);
                daoSPCT.addSPCT(spUpdateQuantity);
                hdDelete.setSoluong(sl);
                daoHDCT.capnhat(hdDelete);
                return "redirect:/hoa-don/ban-hang";
            }
        }
        //sl giảm
        int slgiam = hdDelete.getSoluong() - sl;
        spUpdateQuantity.setSoluong(spUpdateQuantity.getSoluong() + slgiam);
        daoSPCT.addSPCT(spUpdateQuantity);
        hdDelete.setSoluong(sl);
        daoHDCT.capnhat(hdDelete);
        return "redirect:/hoa-don/ban-hang";
    }

    public static String processName(String name) {
        // Remove diacritics
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noDiacritics = pattern.matcher(normalized).replaceAll("");

        // Split the name into parts
        String[] parts = noDiacritics.split("\\s+");

        // Get the last part (last name)
        String lastName = parts[parts.length - 1].toLowerCase();

        // Get the initials of the other parts
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            initials.append(parts[i].charAt(0));
        }

        // Combine last name and initials
        return lastName + initials.toString().toLowerCase();
    }

    @PostMapping("add-nhanh")
    public String changesTTDH(Model model, @ModelAttribute("AddKHNhanh") AddKHNhanhFormBanHang kh, RedirectAttributes redirectAttributes, HttpSession session) {
        if (kh.getCheck() == true) {
            if (this.hdHienTai == null) {
                redirectAttributes.addFlashAttribute("checkaddKH", true);
                return "redirect:/hoa-don/ban-hang";
            }
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        NguoiDung nguoidung = new NguoiDung();
        nguoidung.setHovaten(kh.getTen().trim().replaceAll("\\s+", " "));
        nguoidung.setSodienthoai(kh.getSdt().trim());
        nguoidung.setEmail(kh.getEmail().trim());
        nguoidung.setGioitinh(true);
        nguoidung.setNgaysinh(Date.valueOf("2020-06-06"));
        nguoidung.setNgaytao(Timestamp.valueOf(currentDateTime));
        nguoidung.setTrangthai(true);
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);
        //fake nhân viên
        nguoidung.setNguoitao(nv.getNguoidung().getHovaten());
        String to = kh.getEmail();
        List<KhachHang> lstKHT = daoKH.findKHGanNhat();
        Integer makhnew = lstKHT.get(0).getId() + 1;
        String taikhoan = processName(kh.getTen().trim()) + makhnew;
        String matkhau = "12345";
        String subject = "Chúc mừng bạn đã đăng kí thành công tài khoản T&T shop";
        String mailType = "";
        String mailContent = "Tài khoản của bạn là: " + taikhoan + "\nMật khẩu của bạn là: " + matkhau;
        nguoidung.setTaikhoan(taikhoan);
        nguoidung.setMatkhau(passwordEncoder.encode(matkhau));
        daoNguoiDung.save(nguoidung);
        NguoiDung nguoidungtim = daoNguoiDung.findByEmail(kh.getEmail().trim());
        DiaChi diachi = new DiaChi();
        diachi.setTenduong(kh.getDiachi().trim().replaceAll("\\s+", " "));
        diachi.setXaphuong(kh.getXa());
        diachi.setQuanhuyen(kh.getHuyen());
        diachi.setTinhthanhpho(kh.getTinh());
        diachi.setSdtnguoinhan(kh.getSdt().trim());
        diachi.setHotennguoinhan(kh.getTen().trim().replaceAll("\\s+", " "));
        diachi.setNgaytao(Timestamp.valueOf(currentDateTime));
        diachi.setNguoidung(nguoidungtim);
        diachi.setTrangthai(true);
        daoDiaChi.save(diachi);
        String newMaKH = "KH" + taoChuoiNgauNhien(7, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        KhachHang khAdd = new KhachHang();
        khAdd.setMakhachhang(newMaKH);
        khAdd.setNguoidung(nguoidungtim);
        khAdd.setTrangthai(true);

        khAdd.setNgaytao(Timestamp.valueOf(currentDateTime));
        //fake người tạo
        khAdd.setNguoitao(nv.getNguoidung().getHovaten());
        daoKH.save(khAdd);
        KhachHang khTim = daoKH.findByNguoiDung(nguoidungtim.getId());
        String diaChiHD = kh.getDiachi() + ", " + kh.getXa() + ", " + kh.getHuyen() + ", " + kh.getTinh();
//        hdHienTai.setDiachi(diaChiHD);
//        hdHienTai.setTennguoinhan(kh.getTen());
//        hdHienTai.setSdt(kh.getSdt());
//        hdHienTai.setEmail(kh.getEmail());
//        daoHD.capNhatHD(hdHienTai);


        if (kh.getCheck()) {
            hdHienTai.setKhachhang(khTim);
            hdHienTai.setDiachi(diaChiHD);
            hdHienTai.setTennguoinhan(kh.getTen());
            hdHienTai.setSdt(kh.getSdt());
            hdHienTai.setEmail(kh.getEmail());
            daoHD.capNhatHD(hdHienTai);
            nguoiDung.sendEmail(to, subject, mailType, mailContent);
            redirectAttributes.addFlashAttribute("checkaddKHSucsses", true);
            return "redirect:/hoa-don/ban-hang";
        }
        redirectAttributes.addFlashAttribute("checkaddKHSucsses", true);
        nguoiDung.sendEmail(to, subject, mailType, mailContent);
        return "redirect:/hoa-don/ban-hang";
    }

    //áp dụng phiếu giảm được chọn
    @GetMapping("ap-dung-voucher/{id}")
    @ResponseBody
    public ResponseEntity<?> addvoucherselect(@PathVariable("id") String id) {
        PhieuGiamGia phieutim = daoPGG.findPhieuGiamGiaById(Integer.valueOf(id));
        phieugiamsaoluu = phieutim;
// tính lại số tiền giảm
        sotiengiam = new BigDecimal("0");

        if (phieugiamsaoluu.getLoaiphieu() == true) {
            // phiếu %
            if ((new BigDecimal(phieugiamsaoluu.getGiatrigiam())).multiply(tongtienhoadonhientai.divide(new BigDecimal("100"))).compareTo(phieugiamsaoluu.getGiatrigiamtoida()) > 0) {
                sotiengiam = phieugiamsaoluu.getGiatrigiamtoida();
            } else {
                sotiengiam = (new BigDecimal(phieugiamsaoluu.getGiatrigiam())).multiply(tongtienhoadonhientai.divide(new BigDecimal("100")));
            }

        } else {
            // phiếu vnđ
            sotiengiam = new BigDecimal(phieugiamsaoluu.getGiatrigiam());
        }
        return ResponseEntity.ok(phieutim);
    }

    //checkphieu
    @GetMapping("check-phieu/{id}")
    @ResponseBody
    public ResponseEntity<?> checkphieu(@PathVariable("id") String id) {
        PhieuGiamGia phieutim = daoPGG.findPhieuGiamGiaById(Integer.valueOf(id));
        return ResponseEntity.ok(phieutim);
    }
//hiển thị all phiếu giảm

    @GetMapping("show-all-voucher")
    @ResponseBody
    public ResponseEntity<?> showAllMa() {

        if (hdHienTai == null) {
            return ResponseEntity.ok(false);
        }
        KhachHang kh = hdHienTai.getKhachhang();
        List<KhachHangPhieuGiam> lst = new ArrayList<>();
        if (kh != null) {
            lst = daoKHPG.findAllByKhachhang(kh);
        }
        List<HoaDonChiTiet> lsthdct = daoHDCT.getListSPHD(hdHienTai);

        //lst phiếu giảm cá nhân đang kích hoạt trạng thái là 1
        List<PhieuGiamGia> lstPhieuGiamCaNhan = new ArrayList<>();
        if (lst.size() > 0) {
            for (KhachHangPhieuGiam a : lst
            ) {
                if (a.getPhieugiamgia().getTrangthai() == 1) {
                    lstPhieuGiamCaNhan.add(a.getPhieugiamgia());
                }
            }
        }
        List<PhieuGiamGia> lstphieuPublic = daoPGG.findAllByKieuphieuaAndTrangthais(false, 1);
        // add phiếu cá nhân và phiếu công khai về cùng 1 lst cá nhân
        for (PhieuGiamGia a : lstphieuPublic
        ) {
            lstPhieuGiamCaNhan.add(a);
        }

        List<PhieuGiamGia> lstThoaMan = lstPhieuGiamCaNhan;


        return ResponseEntity.ok(lstThoaMan);
    }


    @GetMapping("fillMaGiam")
    @ResponseBody
    public ResponseEntity<?> fillMaGiam() {
        KhachHang kh = hdHienTai.getKhachhang();
        List<KhachHangPhieuGiam> lst = daoKHPG.findAllByKhachhang(kh);
        List<HoaDonChiTiet> lsthdct = daoHDCT.getListSPHD(hdHienTai);
        BigDecimal tongTienSP = new BigDecimal("0");
        for (HoaDonChiTiet b : lsthdct
        ) {
            tongTienSP = tongTienSP.add(b.getGiasanpham().multiply(new BigDecimal(b.getSoluong())));
        }
        tongtienhoadonhientai = new BigDecimal("0");
        tongtienhoadonhientai = tongTienSP;

        //lst phiếu giảm cá nhân đang kích hoạt trạng thái là 1
        List<PhieuGiamGia> lstPhieuGiamCaNhan = new ArrayList<>();
        for (KhachHangPhieuGiam a : lst
        ) {
            if (a.getPhieugiamgia().getTrangthai() == 1) {
                lstPhieuGiamCaNhan.add(a.getPhieugiamgia());
            }
        }


        //lấy phiếu công khai
        List<PhieuGiamGia> lstphieuPublic = daoPGG.findAllByKieuphieuaAndTrangthais(false, 1);
        // add phiếu cá nhân và phiếu công khai về cùng 1 lst cá nhân
        for (PhieuGiamGia a : lstphieuPublic
        ) {
            lstPhieuGiamCaNhan.add(a);
        }

        //hoàn thành, phiếu cá nhân đã có đầy đủ ds phiếu giảm cả công khai và cá nhân với trang thái đã kích hoạt(1)
        //tạo lst lưu trữ cuối dùng

        List<PhieuGiamGia> lstThoaMan = new ArrayList<>();
        //for vào ds tổng để tìm ra mã nào thỏa mãn điều kiện đơn tối thiểu của hóa đơn
        for (PhieuGiamGia a : lstPhieuGiamCaNhan
        ) {
            if (a.getDontoithieu().compareTo(tongTienSP) <= 0) {
                lstThoaMan.add(a);
            }
        }
        //tách phiếu % và phiếu giảm tiền mặt
        List<PhieuGiamGia> lstphantram = new ArrayList<>();
        List<PhieuGiamGia> lsttienmat = new ArrayList<>();

        for (PhieuGiamGia a : lstThoaMan
        ) {
            if (a.getLoaiphieu() == true) {
                // phần %
                lstphantram.add(a);
            } else {
                //tiền mặt
                lsttienmat.add(a);

            }
        }
        //giả định mã % thơm, tiền mặt thơm
        PhieuGiamGia phantram = lstphantram.size() > 0 ? lstphantram.get(0) : new PhieuGiamGia();
        PhieuGiamGia tienmat = lsttienmat.size() > 0 ? lsttienmat.get(0) : new PhieuGiamGia();

        if (lstphantram.size() > 0) {
            //for vào ds % để tìm mã thơm nhất:)))
            for (PhieuGiamGia a : lstphantram
            ) {
                BigDecimal tien0 = (tongTienSP.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(Double.valueOf("" + a.getGiatrigiam())));
                BigDecimal tien1 = (tongTienSP.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(Double.valueOf("" + phantram.getGiatrigiam())));
                BigDecimal tientaiday = tien0.compareTo(a.getGiatrigiamtoida()) > 0 ? a.getGiatrigiamtoida() : tien0;
                BigDecimal tiensosanh = tien1.compareTo(phantram.getGiatrigiamtoida()) > 0 ? phantram.getGiatrigiamtoida() : tien1;
                if (tientaiday.compareTo(tiensosanh) > 0) {
                    phantram = a;
                }

            }
        }
        if (lsttienmat.size() > 0) {
            //for vào ds tienmat để tìm mã thơm nhất:)))
            for (PhieuGiamGia a : lsttienmat
            ) {
                if (a.getGiatrigiam().compareTo(tienmat.getGiatrigiam()) > 0) {
                    tienmat = a;
                }
            }
        }

        //reset số tiền giảm
        sotiengiam = new BigDecimal("0");

        KhachHangPhieuGiam result = new KhachHangPhieuGiam();
        result.setPhieugiamgia(new PhieuGiamGia());
        if (tienmat.getId() != null && phantram.getId() != null) {
            BigDecimal tiengiam = BigDecimal.valueOf(Double.valueOf("" + tienmat.getGiatrigiam()));
            BigDecimal phantramgiam = (tongTienSP.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(Double.valueOf("" + phantram.getGiatrigiam())));
            BigDecimal maxphantram = phantramgiam.compareTo(phantram.getGiatrigiamtoida()) > 0 ? phantram.getGiatrigiamtoida() : phantramgiam;
            // so sánh để đưa ra mã ngon nhất
            if (tiengiam.compareTo(maxphantram) > 0) {
                result.setPhieugiamgia(tienmat);
                sotiengiam = tiengiam;
            } else {
                if (tiengiam.compareTo(maxphantram) == 0) {
                    result.setPhieugiamgia(tienmat);
                    sotiengiam = tiengiam;
                } else {
                    result.setPhieugiamgia(phantram);
                    sotiengiam = maxphantram;
                }
            }

        } else {
            if (phantram.getId() == null && tienmat.getId() == null) {
                return ResponseEntity.ok(result);
            }

            if (phantram.getId() == null && tienmat.getId() != null) {
                result.setPhieugiamgia(tienmat);
                BigDecimal tiengiam = BigDecimal.valueOf(Double.valueOf("" + tienmat.getGiatrigiam()));
                sotiengiam = tiengiam;

            }
            if (tienmat.getId() == null && phantram.getId() != null) {
                BigDecimal phantramgiam = (tongTienSP.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(Double.valueOf("" + phantram.getGiatrigiam())));
                BigDecimal maxphantram = phantramgiam.compareTo(phantram.getGiatrigiamtoida()) > 0 ? phantram.getGiatrigiamtoida() : phantramgiam;
                result.setPhieugiamgia(phantram);
                sotiengiam = maxphantram;

            }
        }
        phieugiamsaoluu = result.getPhieugiamgia();


        return ResponseEntity.ok(result);
    }


    @GetMapping("xac-nhan-phuong-thuc")
    @ResponseBody
    public ResponseEntity<?> xacNhanPhuongThuc(@RequestParam("phuongthuc") List<String> lst) {
        String mota = Integer.valueOf(lst.get(0)) == 1 ? "tiền mặt" : "chuyển khoản";
        PhuongThucThanhToan phuongThucInsert = new PhuongThucThanhToan();
        phuongThucInsert.setHoadon(hdHienTai);
        phuongThucInsert.setMagiaodichvnpay("N/A");
        phuongThucInsert.setTenphuongthuc("trả trước");
        phuongThucInsert.setTongtien(BigDecimal.valueOf(Double.valueOf(convertCurrency(lst.get(1)))));
        phuongThucInsert.setMota(mota);
        phuongThucInsert.setTrangthai(true);
        LocalDateTime currentDateTime = LocalDateTime.now();
        phuongThucInsert.setNgaytao(Timestamp.valueOf(currentDateTime));
        if (BigDecimal.valueOf(Double.valueOf(convertCurrency(lst.get(1)))).compareTo(new BigDecimal("0.00")) <= 0) {
            return ResponseEntity.ok(lstPTTT);
        }
        lstPTTT.add(phuongThucInsert);
        return ResponseEntity.ok(lstPTTT);
    }

    // thanh toán

    @GetMapping("thanh-toan")
    @ResponseBody
    public ResponseEntity<?> thanhtoan() {
        return ResponseEntity.ok(lstPTTT);
    }

    public static String convertCurrency(String formattedAmount) {
        // Xóa ký hiệu "₫" và các dấu phân cách
        String numericAmount = formattedAmount.replaceAll("[^\\d]", "");
        return numericAmount;
    }

    //xóa bản ghi thanh toán tạm
    @GetMapping("delete-pttt/{id}")
    @ResponseBody
    public ResponseEntity<?> deletePTTT(@PathVariable("id") String tien) {
        BigDecimal tienp = BigDecimal.valueOf(Double.valueOf(tien));
        Iterator<PhuongThucThanhToan> iterator = lstPTTT.iterator();
        while (iterator.hasNext()) {
            PhuongThucThanhToan a = iterator.next();
            if (a.getTongtien().compareTo(tienp) == 0) {
                iterator.remove();
            }
        }
        return ResponseEntity.ok(lstPTTT);
    }

    //hiển thị bill lên modal
    @GetMapping("show-bill")
    @ResponseBody
    public ResponseEntity<?> showbill(HttpSession session) {

        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);
        String nhanviens = nv.getNguoidung().getHovaten();
        List<sanPhamIn> lstin = new ArrayList<>();
        List<HoaDonChiTiet> lsthdct = daoHDCT.getListSPHD(hoaDonCheckBill);
        BigDecimal tongTienSP = new BigDecimal("0");
        String qrcode = "";
        for (HoaDonChiTiet a : lsthdct
        ) {
            lstin.add(new sanPhamIn(a.getSanphamchitiet().getSanpham().getTensanpham(), a.getSoluong()));

        }
        tongTienSP = hoaDonCheckBill.getTongtien();
        String ten = null;
        if (hoaDonCheckBill.getKhachhang() != null) {
            ten = hoaDonCheckBill.getKhachhang().getNguoidung().getHovaten();
        }
        //start tạo qr
        String qrCodeText = hoaDonCheckBill.getMahoadon(); // Chuỗi để tạo QR
        int size = 250; // Kích thước của mã QR

        // Tạo tham số cho mã QR
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Tạo đối tượng QRCodeWriter
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;

        try {
            // Tạo mã QR dưới dạng BitMatrix từ chuỗi và kích thước đã chỉ định
            bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);

            // Lưu BitMatrix thành ảnh PNG
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", new File("src/main/resources/static/QRshowBill" + hdHienTai.getMahoadon() + ".png").toPath());
            // Thay đổi đường dẫn của thư mục chứa ảnh PNG của bạn ở đây
            // Đường dẫn tới file PNG của bạn
            String filePath = "src/main/resources/static/QRshowBill" + hdHienTai.getMahoadon() + ".png";

            File file = new File(filePath);

            if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                try {
                    String base64String = encodeFileToBase64Binary(file);
                    qrcode = base64String;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Đường dẫn không phải là một file PNG hợp lệ.");
            }

        } catch (WriterException | IOException e) {
            System.out.println("Lỗi tạo QR Code: " + e.getMessage());
        }
        //end tạo qr

        MauHoaDon u = new MauHoaDon("FSPORT SHOP", hoaDonCheckBill.getMahoadon(), hoaDonCheckBill.getNgaytao(), "103 Trịnh Văn Bô,Phương Canh, Nam Từ Liêm, Hà Nội",
                hoaDonCheckBill.getDiachi(), "0379036607", hoaDonCheckBill.getSdt(), ten, lstin, tongTienSP, qrcode, nhanviens);
        billTam = u;

        return ResponseEntity.ok(u);
    }

    // xác nhận thanh toán lưu vào db
    @PostMapping("xacnhanthanhtoan/{magiao}")
    public String xacnhanPTTT(@PathVariable("magiao") Integer magiao, @ModelAttribute("thongtingiaohang") DiaChiGiaoCaseBanHangOff thongTin, RedirectAttributes redirectAttributes, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);
        PhieuGiamGiaChiTiet phieugiamgiachtietset = new PhieuGiamGiaChiTiet();
        //thanh toán đơn không giao hàng
        if (magiao == 1) {
            //nhân viên fake

            NhanVien nvfake = nv;
            //
            HoaDon hdset = daoHD.timHDTheoMaHD(hdHienTai.getMahoadon());
            hdset.setTrangthai(5);
            BigDecimal tienTong = new BigDecimal("0.00");
            for (PhuongThucThanhToan a : lstPTTT
            ) {
                tienTong = tienTong.add(a.getTongtien());
                daoPTTT.add_update(a);
            }
            hdset.setTongtien(tienTong);
            hdset.setPhivanchuyen(new BigDecimal("0.00"));
            hdset.setDiachi("ngõ 11, Phường Phương Canh, Quận Nam Từ Liêm, Thành phố Hà Nội");
            hdset.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
            if (hdset.getKhachhang() != null) {
                hdset.setTennguoinhan(hdset.getKhachhang().getNguoidung().getHovaten());
                hdset.setSdt(hdset.getKhachhang().getNguoidung().getSodienthoai());
            } else {
                hdset.setTennguoinhan("Khách lẻ");
                hdset.setSdt("037xxxxxx6");
            }

            daoHD.capNhatHD(hdset);
            LocalDateTime currentDateTime = LocalDateTime.now();
            //tạo phiếu giảm giá chi tiết
            if (phieugiamsaoluu.getMacode() != null) {
                phieugiamgiachtietset.setHoadon(hdset);
                phieugiamgiachtietset.setPhieugiamgia(phieugiamsaoluu);
                phieugiamgiachtietset.setGiasauapdung(hdset.getTongtien());
                phieugiamgiachtietset.setGiabandau(tongtienhoadonhientai);
                phieugiamgiachtietset.setTiengiam(sotiengiam);
                phieugiamgiachtietset.setNgaytao(Timestamp.valueOf(currentDateTime));
                PhieuGiamGia phieuGiamGiasaveSl = phieugiamsaoluu;
                if (phieuGiamGiasaveSl.getSoluong() > 0) {
                    phieuGiamGiasaveSl.setSoluong(phieuGiamGiasaveSl.getSoluong() - 1);
                    if (phieuGiamGiasaveSl.getSoluong() == 0) {
                        phieuGiamGiasaveSl.setTrangthai(2);
                    }
                    daoPGG.save(phieuGiamGiasaveSl);
                }
                daoPGGCT.save(phieugiamgiachtietset);
            }
            // lịch sử hóa đơn 0
            LichSuHoaDon lichSuHoaDon1 = new LichSuHoaDon();
            lichSuHoaDon1.setNhanvien(nvfake);
            lichSuHoaDon1.setGhichu("khách hàng đã đặt đơn hàng");
            lichSuHoaDon1.setHoadon(hdset);
            lichSuHoaDon1.setNgaytao(Timestamp.valueOf(currentDateTime));
            lichSuHoaDon1.setTrangthai(0);
            daoLSHD.add(lichSuHoaDon1);
            // lịch sử hóa đơn 1
            LichSuHoaDon lichSuHoaDon2 = new LichSuHoaDon();
            lichSuHoaDon2.setNhanvien(nvfake);
            lichSuHoaDon2.setGhichu("khách hàng đã xác nhận đơn hàng");
            lichSuHoaDon2.setHoadon(hdset);
            lichSuHoaDon2.setNgaytao(Timestamp.valueOf(currentDateTime));
            lichSuHoaDon2.setTrangthai(1);
            daoLSHD.add(lichSuHoaDon2);
            // lịch sử hóa đơn 4
            LichSuHoaDon lichSuHoaDon3 = new LichSuHoaDon();
            lichSuHoaDon3.setNhanvien(nvfake);
            lichSuHoaDon3.setGhichu("khách hàng đã thanh toán đơn hàng");
            lichSuHoaDon3.setHoadon(hdset);
            lichSuHoaDon3.setNgaytao(Timestamp.valueOf(currentDateTime));
            lichSuHoaDon3.setTrangthai(4);
            daoLSHD.add(lichSuHoaDon3);
            // lịch sử hóa đơn 5
            LichSuHoaDon lichSuHoaDon4 = new LichSuHoaDon();
            lichSuHoaDon4.setNhanvien(nvfake);
            lichSuHoaDon4.setGhichu("Hoàn thành đơn hàng");
            lichSuHoaDon4.setHoadon(hdset);
            lichSuHoaDon4.setNgaytao(Timestamp.valueOf(currentDateTime));
            lichSuHoaDon4.setTrangthai(5);
            daoLSHD.add(lichSuHoaDon4);


            //end lịch sử hóa đơn
            hdHienTai = hdset;
            lstPTTT = new ArrayList<>();
            redirectAttributes.addFlashAttribute("orderSuccess", true);
            List<HoaDon> lstcheck7 = daoHD.timTheoTrangThaiVaLoai(7, false);
            if (lstcheck7.size() > 0) {
                HoaDon TT7 = lstcheck7.get(0);
                TT7.setTrangthai(0);
                daoHD.capNhatHD(TT7);
                redirectAttributes.addFlashAttribute("checkHangCho", true);
            }
            hoaDonCheckBill = hdset;
            return "redirect:/hoa-don/ban-hang";
        }
        // thanh toán đơn có giao hàng
        HoaDon hdset1 = hdHienTai;
        if (thongTin.getTrasau() == true) {
            //trả sau_đợi call api giao hàng nhanh
            hdset1.setTrangthai(1);// khách hàng đã xác nhận
            BigDecimal tienTong = new BigDecimal("0.00");
            List<HoaDonChiTiet> lsthdctTien = daoHDCT.getListSPHD(hdset1);
            for (HoaDonChiTiet a : lsthdctTien
            ) {
                tienTong = tienTong.add(a.getGiasanpham().multiply(new BigDecimal(a.getSoluong())));
            }


            //set phí vận chuyển
            hdset1.setPhivanchuyen(new BigDecimal(convertCurrency(thongTin.getPhivanchuyen())));
            hdset1.setTongtien((tienTong.add(hdset1.getPhivanchuyen())).subtract(sotiengiam));
            //set địa chỉ
            hdset1.setDiachi(thongTin.getDiachi().trim().replaceAll("\\s+", " ") + ", " + thongTin.getXa() + ", " + thongTin.getHuyen() + ", " + thongTin.getTinh());
            hdset1.setTennguoinhan(thongTin.getTen().trim().replaceAll("\\s+", " "));
            hdset1.setSdt(thongTin.getSdt().trim());
            hdset1.setNgayxacnhan(Timestamp.valueOf(LocalDateTime.now()));
            hdset1.setEmail(thongTin.getEmail().trim());
            hdset1.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
            hdset1.setGhichu(thongTin.getGhichu() != null ? thongTin.getGhichu() : "");
//            hdset1.setNgaygiaodukien();
            long unixTimestamp = Long.valueOf(thongTin.getNgaygiaodukien());
            // Convert Unix timestamp to milliseconds
            long milliseconds = unixTimestamp * 1000;

            // Create java.util.Date object
            java.util.Date utilDate = new java.util.Date(milliseconds);

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            Timestamp ngaygiaodukien = new Timestamp(sqlDate.getTime());
            hdset1.setNgaygiaodukien(ngaygiaodukien);
            if (thongTin.getGiaohoatoc() == true) {
                hdset1.setHoatoc(true);
            } else {
                hdset1.setHoatoc(false);
            }
            hoaDonCheckBill = hdset1;
            daoHD.capNhatHD(hdset1);
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (phieugiamsaoluu.getMacode() != null) {
                phieugiamgiachtietset.setHoadon(hdset1);
                phieugiamgiachtietset.setPhieugiamgia(phieugiamsaoluu);
                phieugiamgiachtietset.setGiasauapdung(hdset1.getTongtien());
                phieugiamgiachtietset.setGiabandau(tongtienhoadonhientai);
                phieugiamgiachtietset.setTiengiam(sotiengiam);
                phieugiamgiachtietset.setNgaytao(Timestamp.valueOf(currentDateTime));
                daoPGGCT.save(phieugiamgiachtietset);
            }

            //trả sau thì cần  fake luôn lịch sử đã xác nhận
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            //fake nhân viên
            NhanVien nvfake = nv;
            //fake lịch sử chờ 0
            lichSuHoaDon.setNhanvien(nvfake);
            lichSuHoaDon.setGhichu("khách hàng đã xác đặt đơn đơn hàng");
            lichSuHoaDon.setHoadon(hdset1);
            lichSuHoaDon.setNgaytao(Timestamp.valueOf(currentDateTime));
            lichSuHoaDon.setTrangthai(0);
            daoLSHD.add(lichSuHoaDon);
            //fake lịch sử hóa đơn 1
            LichSuHoaDon lichSuHoaDon1 = new LichSuHoaDon();
            lichSuHoaDon1.setNhanvien(nvfake);
            lichSuHoaDon1.setGhichu("khách hàng đã xác nhận đơn hàng");
            lichSuHoaDon1.setHoadon(hdset1);
            lichSuHoaDon1.setNgaytao(Timestamp.valueOf(currentDateTime));
            lichSuHoaDon1.setTrangthai(1);
            daoLSHD.add(lichSuHoaDon1);
            ////tạo bill thanh toán chưa thành công
            PhuongThucThanhToan phuongthuc = new PhuongThucThanhToan();
            phuongthuc.setTenphuongthuc("Trả sau");
            phuongthuc.setMota("Tiền mặt");
            phuongthuc.setHoadon(hdset1);
            phuongthuc.setNgaytao(Timestamp.valueOf(currentDateTime));
            phuongthuc.setTongtien(hdset1.getTongtien());
            phuongthuc.setMagiaodichvnpay("N/A");
            //fake người tạo
            phuongthuc.setNguoitao("admin");
            phuongthuc.setTrangthai(false);
            daoPTTT.add_update(phuongthuc);
            /////////////


            lstPTTT = new ArrayList<>();
            List<HoaDon> lstcheck7 = daoHD.timTheoTrangThaiVaLoai(7, false);
            if (lstcheck7.size() > 0) {
                HoaDon TT7 = lstcheck7.get(0);
                TT7.setTrangthai(0);
                daoHD.capNhatHD(TT7);
                redirectAttributes.addFlashAttribute("checkHangCho", true);
            }
        } else {
            //trả trước

            hdset1.setTrangthai(1);
            BigDecimal tienTong = new BigDecimal("0.00");
            for (PhuongThucThanhToan a : lstPTTT
            ) {
                tienTong = tienTong.add(a.getTongtien());
                daoPTTT.add_update(a);
            }
            hdset1.setTongtien(tienTong);
            //set phí vận chuyển
            hdset1.setPhivanchuyen(new BigDecimal(convertCurrency(thongTin.getPhivanchuyen())));
            //set địa chỉ
            hdset1.setDiachi(thongTin.getDiachi().trim().replaceAll("\\s+", " ") + ", " + thongTin.getXa() + ", " + thongTin.getHuyen() + ", " + thongTin.getTinh());
            hdset1.setTennguoinhan(thongTin.getTen().trim().replaceAll("\\s+", " "));
            hdset1.setSdt(thongTin.getSdt().trim());
            hdset1.setEmail(thongTin.getEmail().trim());
            hdset1.setNgayxacnhan(Timestamp.valueOf(LocalDateTime.now()));
            hdset1.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
            hdset1.setGhichu(thongTin.getGhichu() != null ? thongTin.getGhichu() : "");
            //hdset1.setNgaygiaodukien();
            long unixTimestamp = Long.valueOf(thongTin.getNgaygiaodukien());
            // Convert Unix timestamp to milliseconds
            long milliseconds = unixTimestamp * 1000;

            // Create java.util.Date object
            java.util.Date utilDate = new java.util.Date(milliseconds);

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            Timestamp ngaygiaodukien = new Timestamp(sqlDate.getTime());
            hdset1.setNgaygiaodukien(ngaygiaodukien);
            if (thongTin.getGiaohoatoc() == true) {
                hdset1.setHoatoc(true);
            } else {
                hdset1.setHoatoc(false);
            }
            daoHD.capNhatHD(hdset1);
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (phieugiamsaoluu.getMacode() != null) {
                phieugiamgiachtietset.setHoadon(hdset1);
                phieugiamgiachtietset.setPhieugiamgia(phieugiamsaoluu);
                phieugiamgiachtietset.setGiasauapdung(hdset1.getTongtien());
                phieugiamgiachtietset.setGiabandau(tongtienhoadonhientai);
                phieugiamgiachtietset.setTiengiam(sotiengiam);
                phieugiamgiachtietset.setNgaytao(Timestamp.valueOf(currentDateTime));
                daoPGGCT.save(phieugiamgiachtietset);
            }


            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            //fake nhân viên
            NhanVien nvfake = nv;

            //fake lịch sử chờ 0
            lichSuHoaDon.setNhanvien(nvfake);
            lichSuHoaDon.setGhichu("khách hàng đã xác đặt đơn đơn hàng");
            lichSuHoaDon.setHoadon(hdset1);
            lichSuHoaDon.setNgaytao(Timestamp.valueOf(currentDateTime));
            lichSuHoaDon.setTrangthai(0);
            daoLSHD.add(lichSuHoaDon);
            //fake lịch sử hóa đơn 1
            LichSuHoaDon lichSuHoaDon1 = new LichSuHoaDon();
            lichSuHoaDon1.setNhanvien(nvfake);
            lichSuHoaDon1.setGhichu("khách hàng đã xác nhận đơn hàng");
            lichSuHoaDon1.setHoadon(hdset1);
            lichSuHoaDon1.setNgaytao(Timestamp.valueOf(currentDateTime));
            lichSuHoaDon1.setTrangthai(1);
            daoLSHD.add(lichSuHoaDon1);


            lstPTTT = new ArrayList<>();
            List<HoaDon> lstcheck7 = daoHD.timTheoTrangThaiVaLoai(7, false);
            if (lstcheck7.size() > 0) {
                HoaDon TT7 = lstcheck7.get(0);
                TT7.setTrangthai(0);
                daoHD.capNhatHD(TT7);
                redirectAttributes.addFlashAttribute("checkHangCho", true);
            }
            redirectAttributes.addFlashAttribute("orderSuccess", false);
            hoaDonCheckBill = hdset1;
            return "redirect:/hoa-don/ban-hang";

        }
        redirectAttributes.addFlashAttribute("orderSuccess", false);
        return "redirect:/hoa-don/ban-hang";
    }

    //bấm giao hàng hiển thị tự động thông tin dựa theo hóa đơn đã có khách hàng
    //http://localhost:8080/ban-hang-tai-quay/fillDiachi
    @GetMapping("fillDiachi")
    @ResponseBody
    public ResponseEntity<?> fillDiachi() {

        HoaDon hddc = hdHienTai == null ? new HoaDon() : daoHD.timHDTheoMaHD(hdHienTai.getMahoadon());
        DiaChiGiaoCaseBanHangOff diachiRT = new DiaChiGiaoCaseBanHangOff();
        if (hddc.getKhachhang() != null) {
            List<String> diachiLst = Arrays.asList(hddc.getDiachi().split(", "));
            String diachiCT = diachiLst.get(0);
            String xa = diachiLst.get(1);
            String huyen = diachiLst.get(2);
            String tinh = diachiLst.get(3);
            diachiRT.setDiachi(diachiCT);
            diachiRT.setTinh(tinh);
            diachiRT.setHuyen(huyen);
            diachiRT.setXa(xa);
            diachiRT.setTen(hddc.getTennguoinhan());
            diachiRT.setSdt(hddc.getSdt());
            diachiRT.setEmail(hddc.getEmail());
        }
        if (hddc.getKhachhang() == null) {
            return ResponseEntity.ok(new DiaChiGiaoCaseBanHangOff());
        }
        return ResponseEntity.ok(diachiRT);
    }

    //xử lý hóa đơn mới thứ 6
    @GetMapping("chuyen-hang-cho")
    @ResponseBody
    public ResponseEntity<?> chuyenHDVeHangCho() {
        List<HoaDon> lst1 = daoHD.timTheoTrangThaiVaLoai(7, false);
        if (lst1.size() > 2) {
            //check hóa đơn tràn hàng chờ đã tồn tại chưa, nếu tồn tại return false
            return ResponseEntity.ok(false);
        } else {
            //chưa tồn tại hóa đơn tràn hàng chờ
            List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
            HoaDon hdChuyenVeHangCho = lst.get(4);
            hdChuyenVeHangCho.setTrangthai(7);
            daoHD.capNhatHD(hdChuyenVeHangCho);
        }
        return ResponseEntity.ok(true);
    }

    //show chuyển all đơn tt0 và tt7
    @GetMapping("show-hang-cho")
    @ResponseBody
    public ResponseEntity<?> showTT0TT7() {
        List<HoaDon> lst1 = daoHD.timTheoTrangThaiVaLoai(7, false);
        List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
        List<List> lstrt = new ArrayList<>();
        lstrt.add(lst1);
        lstrt.add(lst);
        return ResponseEntity.ok(lstrt);
    }

    //chặn giao và thanh toán khi không có đơn
    //http://localhost:8080/ban-hang-tai-quay/checkGiaoThanhToan
    @GetMapping("checkGiaoThanhToan")
    @ResponseBody
    public ResponseEntity<?> checkGiaoThanhToan() {
        List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
        if (lst.size() > 0) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    //chuyển đổi đơn tt7-->> tt0
    @GetMapping("chuyen-doi-don")
    public ResponseEntity<?> chuyenHDTT7(@RequestParam("lstData") List<String> lstData
    ) {
        if (lstData.size() > 0) {
            HoaDon hd1 = daoHD.timHDTheoMaHD(lstData.get(0));// hóa đơn tràn chờ => về xử lý
            HoaDon hd2 = daoHD.timHDTheoMaHD(lstData.get(1));// hóa đơn đang xử lý => tràn chờ
            hd1.setTrangthai(0);
            hd2.setTrangthai(7);
            daoHD.capNhatHD(hd1);
            daoHD.capNhatHD(hd2);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @GetMapping("validate-SL")
    public ResponseEntity<?> validateSL(@RequestParam("id") String id
    ) {
        HoaDonChiTiet hdct = daoHDCT.findByID(Integer.valueOf(id));
        return ResponseEntity.ok(hdct);
    }

    private String taoChuoiNgauNhien(int doDaiChuoi, String kiTu) {
        Random random = new Random();
        StringBuilder chuoiNgauNhien = new StringBuilder(doDaiChuoi);
        for (int i = 0; i < doDaiChuoi; i++) {
            chuoiNgauNhien.append(kiTu.charAt(random.nextInt(kiTu.length())));
        }
        return chuoiNgauNhien.toString();
    }

    @GetMapping("in-don-tai-quay")
    public ResponseEntity<?> inBill(
    ) {
        String finalhtml = null;
        //start tạo qr
        //tạo qr
        String qrCodeText = hoaDonCheckBill.getMahoadon(); // Chuỗi để tạo QR
        int size = 250; // Kích thước của mã QR

        // Tạo tham số cho mã QR
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Tạo đối tượng QRCodeWriter
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;

        try {
            // Tạo mã QR dưới dạng BitMatrix từ chuỗi và kích thước đã chỉ định
            bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);

            // Lưu BitMatrix thành ảnh PNG
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", new File("src/main/resources/static/" + hoaDonCheckBill.getMahoadon() + ".png").toPath());
            // Thay đổi đường dẫn của thư mục chứa ảnh PNG của bạn ở đây
            // Đường dẫn tới file PNG của bạn
            String filePath = "src/main/resources/static/" + hoaDonCheckBill.getMahoadon() + ".png";

            File file = new File(filePath);

            if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                try {
                    String base64String = encodeFileToBase64Binary(file);
                    System.out.println("File: " + file.getName());
                    billTam.setQr(base64String);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Đường dẫn không phải là một file PNG hợp lệ.");
            }

        } catch (WriterException | IOException e) {
            System.out.println("Lỗi tạo QR Code: " + e.getMessage());
        }
        //end tạo qr
        Context data = daoHD.setData(billTam);
        finalhtml = dao1.process("billhoadon", data);
        daoHD.htmlToPdfTaiQuay(finalhtml, hoaDonCheckBill.getMahoadon());
        return ResponseEntity.ok(true);
    }

}
