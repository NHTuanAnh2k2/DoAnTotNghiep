package com.example.demo.controller.banhang;

import com.example.demo.entity.*;
import com.example.demo.info.AddKHNhanhFormBanHang;
import com.example.demo.info.DiaChiGiaoCaseBanHangOff;
import com.example.demo.info.NguoiDungKHInfo;
import com.example.demo.info.ThayDoiTTHoaDon_KHInfo;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.repository.KhachHangPhieuGiamRepository;
import com.example.demo.repository.NguoiDungRepository;
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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("ban-hang-tai-quay")
public class BanHangController {
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

    @GetMapping("hoa-don-cho")
    @ResponseBody
    public ResponseEntity<?> getLstCho() {
        List<HoaDon> lst = daoHD.timTheoTrangThaiVaLoai(0, false);
        hdHienTai = lst.get(0);
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
        hdset.setKhachhang(kh.get());
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
        List<KhachHangPhieuGiam> lstnew = new ArrayList<>();
        for (KhachHangPhieuGiam a : lst
        ) {
            if (a.getPhieugiamgia().getDontoithieu().compareTo(tongTienSP) <= 0) {
                lstnew.add(a);
            }
        }
        if (lstnew.size() < 1) {
            return ResponseEntity.ok(new KhachHangPhieuGiam());
        }
        KhachHangPhieuGiam result = lstnew.get(0);

        for (KhachHangPhieuGiam a : lstnew
        ) {
            if (a.getPhieugiamgia().getGiatrigiamtoida().compareTo(result.getPhieugiamgia().getGiatrigiamtoida()) == 1) {
                result = a;
            }
        }
        return ResponseEntity.ok(result);
    }

    //chứa bản ghi thanh toán tạm
    List<PhuongThucThanhToan> lstPTTT = new ArrayList<>();

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

    // xác nhận thanh toán lưu vào db
    @GetMapping("xacnhanthanhtoan/{magiao}")
    public String xacnhanPTTT(@PathVariable("magiao") Integer magiao,@ModelAttribute("thongtingiaohang") DiaChiGiaoCaseBanHangOff thongTin) {
       if(magiao==1){
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
           return "redirect:/hoa-don/ban-hang";
       }
       
        return "redirect:/hoa-don/ban-hang";
    }

}
