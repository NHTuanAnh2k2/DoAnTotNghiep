package com.example.demo.controller.banhang;

import com.example.demo.entity.*;
import com.example.demo.info.*;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.repository.KhachHangPhieuGiamRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.PhieuGiamGiaChiTiet.PhieuGiamChiTietRepository;
import com.example.demo.repository.PhieuGiamGiaRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("ban-hang-tai-quay")
public class BanHangController {
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
    HoaDonService daoHD;

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
    public ResponseEntity<?> createNewHD() {
        HoaDon hoadonCho = new HoaDon();
        hoadonCho.setTrangthai(0);
        hoadonCho.setLoaihoadon(false);
        NhanVien nv = new NhanVien();
        nv.setId(1);
        // fake id nhân viên sau này sẽ lấy thì login xuống
        hoadonCho.setNhanvien(nv);
        HoaDon hdMaGet = daoHD.timBanGhiDuocTaoGanNhat();
        Integer maMoi = Integer.valueOf(hdMaGet.getMahoadon().trim().substring(8)) + 1;
        hoadonCho.setMahoadon("HDFSPORT" + maMoi);
        LocalDateTime currentDateTime = LocalDateTime.now();
        hoadonCho.setNgaytao(Timestamp.valueOf(currentDateTime));
        daoHD.capNhatHD(hoadonCho);
        List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
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
    public String choseNV(@PathVariable("id") Integer id, Model model) {
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

    // thêm sản phẩm tại hdct
    @GetMapping("ChoseSP/{id}")
    public String choseSP(@PathVariable("id") Integer id) {
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


    @PostMapping("add-nhanh")
    public String changesTTDH(Model model, @ModelAttribute("AddKHNhanh") AddKHNhanhFormBanHang kh) {
        NguoiDung nguoidung = new NguoiDung();
        nguoidung.setHovaten(kh.getTen());
        nguoidung.setSodienthoai(kh.getSdt());
        nguoidung.setEmail(kh.getEmail());
        nguoidung.setGioitinh(true);
        nguoidung.setNgaysinh(Date.valueOf("2020-06-06"));
        nguoidung.setCccd("024099013632");
        daoNguoiDung.save(nguoidung);
        NguoiDung nguoidungtim = daoNguoiDung.findByEmail(kh.getEmail());
        DiaChi diachi = new DiaChi();
        diachi.setTenduong(kh.getDiachi());
        diachi.setXaphuong(kh.getXa());
        diachi.setQuanhuyen(kh.getHuyen());
        diachi.setTinhthanhpho(kh.getTinh());
        diachi.setNguoidung(nguoidungtim);
        daoDiaChi.save(diachi);
        KhachHang khAdd = new KhachHang();
        khAdd.setNguoidung(nguoidungtim);
        daoKH.save(khAdd);
        KhachHang khTim = daoKH.findByNguoiDung(nguoidungtim.getId());
        hdHienTai.setKhachhang(khTim);
        if (kh.getCheck()) {
            hdHienTai.setKhachhang(khTim);
            daoHD.capNhatHD(hdHienTai);
            return "redirect:/hoa-don/ban-hang";
        }
        return "redirect:/hoa-don/ban-hang";
    }

    //áp dụng phiếu giảm được chọn
    @GetMapping("ap-dung-voucher/{id}")
    @ResponseBody
    public ResponseEntity<?> addvoucherselect(@PathVariable("id") String id) {
        PhieuGiamGia phieutim = daoPGG.findPhieuGiamGiaById(Integer.valueOf(id));
        phieugiamsaoluu = phieutim;
        System.out.println("aaaaaaaaaaaa");
        System.out.println(phieugiamsaoluu.getMacode());
        return ResponseEntity.ok(phieutim);
    }
//hiển thị all phiếu giảm

    @GetMapping("show-all-voucher")
    @ResponseBody
    public ResponseEntity<?> showAllMa() {
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
    public ResponseEntity<?> showbill() {
        List<sanPhamIn> lstin = new ArrayList<>();
        List<HoaDonChiTiet> lsthdct = daoHDCT.getListSPHD(hdHienTai);
        BigDecimal tongTienSP = new BigDecimal("0");
        for (HoaDonChiTiet a : lsthdct
        ) {
            tongTienSP = tongTienSP.add(a.getGiasanpham().multiply(new BigDecimal(a.getSoluong())));
            lstin.add(new sanPhamIn(a.getSanphamchitiet().getSanpham().getTensanpham(), a.getSoluong()));

        }
        String ten = null;
        if (hdHienTai.getKhachhang() != null) {
            ten = hdHienTai.getKhachhang().getNguoidung().getHovaten();
        }

        MauHoaDon u = new MauHoaDon("FSPORT SHOP", hdHienTai.getMahoadon(), hdHienTai.getNgaytao(), "Lô H023, Nhà số 39, Ngõ 148, Xuân Phương, Phương Canh,Nam Từ Liêm, Hà Nội",
                hdHienTai.getDiachi(), "0379036607", hdHienTai.getSdt(), ten, lstin, tongTienSP);
        return ResponseEntity.ok(u);
    }

    // xác nhận thanh toán lưu vào db
    @PostMapping("xacnhanthanhtoan/{magiao}")
    public String xacnhanPTTT(@PathVariable("magiao") Integer magiao, @ModelAttribute("thongtingiaohang") DiaChiGiaoCaseBanHangOff thongTin, RedirectAttributes redirectAttributes) {

        PhieuGiamGiaChiTiet phieugiamgiachtietset = new PhieuGiamGiaChiTiet();
        //thanh toán đơn không giao hàng
        if (magiao == 1) {
            HoaDon hdset = hdHienTai;
            hdset.setTrangthai(5);
            BigDecimal tienTong = new BigDecimal("0.00");
            for (PhuongThucThanhToan a : lstPTTT
            ) {
                tienTong = tienTong.add(a.getTongtien());
                daoPTTT.add_update(a);
            }
            hdset.setTongtien(tienTong);
            hdset.setPhivanchuyen(new BigDecimal("0.00"));
            daoHD.capNhatHD(hdset);
            //tạo phiếu giảm giá chi tiết
            phieugiamgiachtietset.setHoadon(hdset);
            phieugiamgiachtietset.setPhieugiamgia(phieugiamsaoluu);
            phieugiamgiachtietset.setGiasauapdung(hdset.getTongtien());
            phieugiamgiachtietset.setGiabandau(tongtienhoadonhientai);
            phieugiamgiachtietset.setTiengiam(sotiengiam);
            LocalDateTime currentDateTime = LocalDateTime.now();
            phieugiamgiachtietset.setNgaytao(Timestamp.valueOf(currentDateTime));
            daoPGGCT.save(phieugiamgiachtietset);
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

            return "redirect:/hoa-don/ban-hang";
        }
        // thanh toán đơn có giao hàng
        HoaDon hdset1 = hdHienTai;
        if (thongTin.getTrasau() == true) {
            //trả sau_đợi call api giao hàng nhanh
            hdset1.setTrangthai(1);
            BigDecimal tienTong = new BigDecimal("0.00");
            hdset1.setTongtien(tienTong);
            //set phí vận chuyển tạm tính 30k sau đó call từ giao hàng nhanh
            hdset1.setPhivanchuyen(new BigDecimal(convertCurrency(thongTin.getPhivanchuyen())));
            //set địa chỉ
            hdset1.setDiachi(thongTin.getDiachi() + ", " + thongTin.getXa() + ", " + thongTin.getHuyen() + ", " + thongTin.getTinh());
            hdset1.setTennguoinhan(thongTin.getTen());
            hdset1.setSdt(thongTin.getSdt());
            hdset1.setEmail(thongTin.getEmail());
            daoHD.capNhatHD(hdset1);
            phieugiamgiachtietset.setHoadon(hdset1);
            phieugiamgiachtietset.setPhieugiamgia(phieugiamsaoluu);
            phieugiamgiachtietset.setGiasauapdung(hdset1.getTongtien());
            phieugiamgiachtietset.setGiabandau(tongtienhoadonhientai);
            phieugiamgiachtietset.setTiengiam(sotiengiam);
            LocalDateTime currentDateTime = LocalDateTime.now();
            phieugiamgiachtietset.setNgaytao(Timestamp.valueOf(currentDateTime));
            daoPGGCT.save(phieugiamgiachtietset);
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
            hdset1.setTrangthai(5);
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
            hdset1.setDiachi(thongTin.getDiachi() + ", " + thongTin.getXa() + ", " + thongTin.getHuyen() + ", " + thongTin.getTinh());
            hdset1.setTennguoinhan(thongTin.getTen());
            hdset1.setSdt(thongTin.getSdt());
            hdset1.setEmail(thongTin.getEmail());
            daoHD.capNhatHD(hdset1);
            phieugiamgiachtietset.setHoadon(hdset1);
            phieugiamgiachtietset.setPhieugiamgia(phieugiamsaoluu);
            phieugiamgiachtietset.setGiasauapdung(hdset1.getTongtien());
            phieugiamgiachtietset.setGiabandau(tongtienhoadonhientai);
            phieugiamgiachtietset.setTiengiam(sotiengiam);
            LocalDateTime currentDateTime = LocalDateTime.now();
            phieugiamgiachtietset.setNgaytao(Timestamp.valueOf(currentDateTime));
            daoPGGCT.save(phieugiamgiachtietset);
            lstPTTT = new ArrayList<>();
            List<HoaDon> lstcheck7 = daoHD.timTheoTrangThaiVaLoai(7, false);
            if (lstcheck7.size() > 0) {
                HoaDon TT7 = lstcheck7.get(0);
                TT7.setTrangthai(0);
                daoHD.capNhatHD(TT7);
                redirectAttributes.addFlashAttribute("checkHangCho", true);
            }
            redirectAttributes.addFlashAttribute("orderSuccess", true);
            return "redirect:/hoa-don/ban-hang";

        }
        redirectAttributes.addFlashAttribute("orderSuccess", true);
        return "redirect:/hoa-don/ban-hang";
    }

    //bấm giao hàng hiển thị tự động thông tin dựa theo hóa đơn đã có khách hàng
    //http://localhost:8080/ban-hang-tai-quay/fillDiachi
    @GetMapping("fillDiachi")
    @ResponseBody
    public ResponseEntity<?> fillDiachi() {
        HoaDon hddc = daoHD.timHDTheoMaHD(hdHienTai.getMahoadon());
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
        if (lst1.size() > 0) {
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
    //in đơn
//    MauHoaDon u = new MauHoaDon("FSPORT", hdTT.getMahoadon(), hdTT.getNgaytao(), "Lô H023, Nhà số 39, Ngõ 148, Xuân Phương, Phương Canh,Nam Từ Liêm, Hà Nội",
//            hdTT.getDiachi(), "0379036607", hdTT.getSdt(), hdTT.getTennguoinhan(), lstin, tongTT);
//    String finalhtml = null;
//    Context data = dao.setData(u);
//    finalhtml = dao1.process("index", data);
//        dao.htmlToPdfTaiQuay(finalhtml, hdTT.getMahoadon());
}
