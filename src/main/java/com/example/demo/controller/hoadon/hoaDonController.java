package com.example.demo.controller.hoadon;

import com.example.demo.entity.*;
import com.example.demo.info.HoaDonCustom;
import com.example.demo.info.LichSuHoaDonCustom;
import com.example.demo.info.ThayDoiTTHoaDon_KHInfo;
import com.example.demo.repository.*;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.restcontroller.khachhang.Province;
import com.example.demo.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("hoa-don")
public class hoaDonController {
    @Autowired
    ChatLieuRepository daoChatLieu;
    @Autowired
    ThuongHieuRepository daoThuongHieu;
    @Autowired
    DeGiayRepository daoDeGiay;
    @Autowired
    KichCoRepository daoKichCo;
    @Autowired
    MauSacRepository daoMauSac;
    @Autowired
    PhieuGiamGiaChiTietService daoPGGCT;
    @Autowired
    SanPhamChiTietService daoSPCT;
    @Autowired
    NhanVienRepository nhanVienService;
    @Autowired
    KhachHangService daoKH;
    @Autowired
    PhuongThucThanhToanService daoPT;
    @Autowired
    HoaDonChiTietService daoHDCT;
    @Autowired
    LichSuHoaDonService daoLS;
    @Autowired
    HoaDonService dao;
    Integer idhdshowdetail = null;
    HoaDonCustom hdSaveInfoSeachr = new HoaDonCustom();

    @GetMapping("hien-thi")
    public String hienThi(Model model, @RequestParam("page") Optional<Integer> pageParam,
                          @ModelAttribute("hdcustom") HoaDonCustom info) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Page<HoaDon> lst = dao.findAll(p);
        model.addAttribute("lst", lst);
        model.addAttribute("pageNo", page);
        model.addAttribute("checkTT", 7);
        //hiển thị tổng số hd theo tt
        model.addAttribute("tt0", dao.tinhTong(0));
        model.addAttribute("tt1", dao.tinhTong(1));
        model.addAttribute("tt2", dao.tinhTong(2));
        model.addAttribute("tt3", dao.tinhTong(3));
        model.addAttribute("tt4", dao.tinhTong(4));
        model.addAttribute("tt5", dao.tinhTong(5));
        model.addAttribute("tt6", dao.tinhTong(6));
        model.addAttribute("tt7", dao.findAll(p).getTotalElements());
        return "admin/qlhoadon";
    }

    @GetMapping("loc")
    public String Loc(@ModelAttribute("hdcustom") HoaDonCustom HDinfo) {
        if (HDinfo.getKey().isBlank()) HDinfo.setKey("null");
        try {
            Date.valueOf(HDinfo.getTu());
        } catch (Exception e) {
            HDinfo.setTu("null");
        }
        try {
            Date.valueOf(HDinfo.getDen());
        } catch (Exception e) {
            HDinfo.setDen("null");
        }
        hdSaveInfoSeachr = HDinfo;
        return "redirect:/hoa-don/show-loc";
    }

    @GetMapping("show-loc")
    public String Loc(Model model, @RequestParam("page") Optional<Integer> pageParam, @ModelAttribute("hdcustom") HoaDonCustom HDinfo) {
        Page<HoaDon> lst = null;
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        int trangThai = -1;
        if (hdSaveInfoSeachr.getKey().equalsIgnoreCase("null")
                || hdSaveInfoSeachr.getLoaiHD().equalsIgnoreCase("null")
                || hdSaveInfoSeachr.getTu().equalsIgnoreCase("null")
                || hdSaveInfoSeachr.getDen().equalsIgnoreCase("null")) {
            //các case tìm kiếm 1 th null
            //case key null and all not null
            if (hdSaveInfoSeachr.getKey().equalsIgnoreCase("null")
                    && !hdSaveInfoSeachr.getLoaiHD().equalsIgnoreCase("null")
                    && !hdSaveInfoSeachr.getTu().equalsIgnoreCase("null")
                    && !hdSaveInfoSeachr.getDen().equalsIgnoreCase("null")) {

                lst = dao.LockTT(Boolean.valueOf(hdSaveInfoSeachr.getLoaiHD()), Date.valueOf(hdSaveInfoSeachr.getTu()),
                        Date.valueOf(hdSaveInfoSeachr.getDen()), p);
            }
            //case loaihd null and all not null
            if (hdSaveInfoSeachr.getLoaiHD().equalsIgnoreCase("null")
                    && !hdSaveInfoSeachr.getKey().equalsIgnoreCase("null")
                    && !hdSaveInfoSeachr.getTu().equalsIgnoreCase("null")
                    && !hdSaveInfoSeachr.getDen().equalsIgnoreCase("null")) {
                if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("chờ xác nhận")) {
                    trangThai = 0;
                } else {
                    if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã xác nhận")) {
                        trangThai = 1;
                    } else {
                        if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("chờ giao hàng")) {
                            trangThai = 2;
                        } else {
                            if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đang giao hàng")) {
                                trangThai = 3;
                            } else {
                                if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã thanh toán")) {
                                    trangThai = 4;
                                } else {
                                    if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã hoàn thành")) {
                                        trangThai = 5;
                                    } else {
                                        if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã hủy")) {
                                            trangThai = 6;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                lst = dao.LocKLHD(trangThai, Date.valueOf(hdSaveInfoSeachr.getTu()),
                        Date.valueOf(hdSaveInfoSeachr.getDen()), p);
            }
            //case ngaytao(tu or den) null and all not null
            if (hdSaveInfoSeachr.getTu().equalsIgnoreCase("null")
                    || hdSaveInfoSeachr.getDen().equalsIgnoreCase("null")) {
                if (!hdSaveInfoSeachr.getKey().equalsIgnoreCase("null")
                        && !hdSaveInfoSeachr.getLoaiHD().equalsIgnoreCase("null")) {
                    if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("chờ xác nhận")) {
                        trangThai = 0;
                    } else {
                        if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã xác nhận")) {
                            trangThai = 1;
                        } else {
                            if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("chờ giao hàng")) {
                                trangThai = 2;
                            } else {
                                if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đang giao hàng")) {
                                    trangThai = 3;
                                } else {
                                    if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã thanh toán")) {
                                        trangThai = 4;
                                    } else {
                                        if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã hoàn thành")) {
                                            trangThai = 5;
                                        } else {
                                            if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã hủy")) {
                                                trangThai = 6;
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    lst = dao.LocKngayTao(trangThai, Boolean.valueOf(hdSaveInfoSeachr.getLoaiHD()), p);

                }

            }
            //end case tìm kiếm 1 th null
            //các case tìm kiếm 2 th null


            //case key and loaihd null
            if (hdSaveInfoSeachr.getKey().equalsIgnoreCase("null")
                    && hdSaveInfoSeachr.getLoaiHD().equalsIgnoreCase("null")
                    && !hdSaveInfoSeachr.getTu().equalsIgnoreCase("null")
                    && !hdSaveInfoSeachr.getDen().equalsIgnoreCase("null")) {

                lst = dao.LocTheoKhoangNgay(Date.valueOf(hdSaveInfoSeachr.getTu()), Date.valueOf(hdSaveInfoSeachr.getDen()), p);
            }
            //case key and (tu or den null)
            if (hdSaveInfoSeachr.getKey().equalsIgnoreCase("null")
                    && !hdSaveInfoSeachr.getLoaiHD().equalsIgnoreCase("null")
                    && hdSaveInfoSeachr.getTu().equalsIgnoreCase("null")
                    && hdSaveInfoSeachr.getDen().equalsIgnoreCase("null")) {
                lst = dao.LocTheoLoaiDon(Boolean.valueOf(hdSaveInfoSeachr.getLoaiHD()), p);
            }

            //case loaihd and (tu or den null)
            if (!hdSaveInfoSeachr.getKey().equalsIgnoreCase("null")
                    && hdSaveInfoSeachr.getLoaiHD().equalsIgnoreCase("null")
                    && hdSaveInfoSeachr.getTu().equalsIgnoreCase("null")
                    && hdSaveInfoSeachr.getDen().equalsIgnoreCase("null")) {
                if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("chờ xác nhận")) {
                    trangThai = 0;
                } else {
                    if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã xác nhận")) {
                        trangThai = 1;
                    } else {
                        if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("chờ giao hàng")) {
                            trangThai = 2;
                        } else {
                            if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đang giao hàng")) {
                                trangThai = 3;
                            } else {
                                if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã thanh toán")) {
                                    trangThai = 4;
                                } else {
                                    if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã hoàn thành")) {
                                        trangThai = 5;
                                    } else {
                                        if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã hủy")) {
                                            trangThai = 6;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                lst = dao.timKiemTT(trangThai, p);
            }

            // end các case tìm kiếm 2 th null

        } else {
            if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("chờ xác nhận")) {
                trangThai = 0;
            } else {
                if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã xác nhận")) {
                    trangThai = 1;
                } else {
                    if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("chờ giao hàng")) {
                        trangThai = 2;
                    } else {
                        if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đang giao hàng")) {
                            trangThai = 3;
                        } else {
                            if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã thanh toán")) {
                                trangThai = 4;
                            } else {
                                if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã hoàn thành")) {
                                    trangThai = 5;
                                } else {
                                    if (hdSaveInfoSeachr.getKey().trim().equalsIgnoreCase("đã hủy")) {
                                        trangThai = 6;
                                    }
                                }
                            }
                        }

                    }
                }
            }
            lst = dao.Loc(trangThai,
                    Boolean.valueOf(hdSaveInfoSeachr.getLoaiHD()), Date.valueOf(hdSaveInfoSeachr.getTu()),
                    Date.valueOf(hdSaveInfoSeachr.getDen()), p);
        }
        model.addAttribute("lst", lst);
        model.addAttribute("hdcustom", hdSaveInfoSeachr);
        model.addAttribute("pageNo", page);
        //hiển thị số hd theo tt
        model.addAttribute("tt0", dao.tinhTong(0));
        model.addAttribute("tt1", dao.tinhTong(1));
        model.addAttribute("tt2", dao.tinhTong(2));
        model.addAttribute("tt3", dao.tinhTong(3));
        model.addAttribute("tt4", dao.tinhTong(4));
        model.addAttribute("tt5", dao.tinhTong(5));
        model.addAttribute("tt6", dao.tinhTong(6));
        model.addAttribute("tt7", dao.findAll(p).getTotalElements());
        return "admin/qlhoadon";
    }

    @GetMapping("cho-xac-nhan")
    public String choXacNhan(Model model, @RequestParam("page") Optional<Integer> pageParam,
                             @ModelAttribute("hdcustom") HoaDonCustom info) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Page<HoaDon> lst = dao.timKiemTT(0, p);
        model.addAttribute("lst", lst);
        model.addAttribute("pageNo", page);
        model.addAttribute("checkTT", 0);
        model.addAttribute("tt0", dao.tinhTong(0));
        model.addAttribute("tt1", dao.tinhTong(1));
        model.addAttribute("tt2", dao.tinhTong(2));
        model.addAttribute("tt3", dao.tinhTong(3));
        model.addAttribute("tt4", dao.tinhTong(4));
        model.addAttribute("tt5", dao.tinhTong(5));
        model.addAttribute("tt6", dao.tinhTong(6));
        model.addAttribute("tt7", dao.findAll(p).getTotalElements());
        return "admin/qlhoadon";
    }

    @GetMapping("da-xac-nhan")
    public String daXacNhan(Model model, @RequestParam("page") Optional<Integer> pageParam,
                            @ModelAttribute("hdcustom") HoaDonCustom info) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Page<HoaDon> lst = dao.timKiemTT(1, p);
        model.addAttribute("lst", lst);
        model.addAttribute("pageNo", page);
        model.addAttribute("checkTT", 1);
        model.addAttribute("tt0", dao.tinhTong(0));
        model.addAttribute("tt1", dao.tinhTong(1));
        model.addAttribute("tt2", dao.tinhTong(2));
        model.addAttribute("tt3", dao.tinhTong(3));
        model.addAttribute("tt4", dao.tinhTong(4));
        model.addAttribute("tt5", dao.tinhTong(5));
        model.addAttribute("tt6", dao.tinhTong(6));
        model.addAttribute("tt7", dao.findAll(p).getTotalElements());
        return "admin/qlhoadon";
    }

    @GetMapping("cho-giao-hang")
    public String choGiaoHang(Model model, @RequestParam("page") Optional<Integer> pageParam,
                              @ModelAttribute("hdcustom") HoaDonCustom info) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Page<HoaDon> lst = dao.timKiemTT(2, p);
        model.addAttribute("lst", lst);
        model.addAttribute("pageNo", page);
        model.addAttribute("checkTT", 2);
        model.addAttribute("tt0", dao.tinhTong(0));
        model.addAttribute("tt1", dao.tinhTong(1));
        model.addAttribute("tt2", dao.tinhTong(2));
        model.addAttribute("tt3", dao.tinhTong(3));
        model.addAttribute("tt4", dao.tinhTong(4));
        model.addAttribute("tt5", dao.tinhTong(5));
        model.addAttribute("tt6", dao.tinhTong(6));
        model.addAttribute("tt7", dao.findAll(p).getTotalElements());
        return "admin/qlhoadon";
    }

    @GetMapping("dang-giao-hang")
    public String dangGiaoHang(Model model, @RequestParam("page") Optional<Integer> pageParam,
                               @ModelAttribute("hdcustom") HoaDonCustom info) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Page<HoaDon> lst = dao.timKiemTT(3, p);
        model.addAttribute("lst", lst);
        model.addAttribute("pageNo", page);
        model.addAttribute("checkTT", 3);
        model.addAttribute("tt0", dao.tinhTong(0));
        model.addAttribute("tt1", dao.tinhTong(1));
        model.addAttribute("tt2", dao.tinhTong(2));
        model.addAttribute("tt3", dao.tinhTong(3));
        model.addAttribute("tt4", dao.tinhTong(4));
        model.addAttribute("tt5", dao.tinhTong(5));
        model.addAttribute("tt6", dao.tinhTong(6));
        model.addAttribute("tt7", dao.findAll(p).getTotalElements());
        return "admin/qlhoadon";
    }

    @GetMapping("da-thanh-toan")
    public String daThanhToan(Model model, @RequestParam("page") Optional<Integer> pageParam,
                              @ModelAttribute("hdcustom") HoaDonCustom info) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Page<HoaDon> lst = dao.timKiemTT(4, p);
        model.addAttribute("lst", lst);
        model.addAttribute("pageNo", page);
        model.addAttribute("checkTT", 4);
        model.addAttribute("tt0", dao.tinhTong(0));
        model.addAttribute("tt1", dao.tinhTong(1));
        model.addAttribute("tt2", dao.tinhTong(2));
        model.addAttribute("tt3", dao.tinhTong(3));
        model.addAttribute("tt4", dao.tinhTong(4));
        model.addAttribute("tt5", dao.tinhTong(5));
        model.addAttribute("tt6", dao.tinhTong(6));
        model.addAttribute("tt7", dao.findAll(p).getTotalElements());
        return "admin/qlhoadon";
    }

    @GetMapping("da-hoan-thanh")
    public String daHoanThanh(Model model, @RequestParam("page") Optional<Integer> pageParam,
                              @ModelAttribute("hdcustom") HoaDonCustom info) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Page<HoaDon> lst = dao.timKiemTT(5, p);
        model.addAttribute("lst", lst);
        model.addAttribute("pageNo", page);
        model.addAttribute("checkTT", 5);
        model.addAttribute("tt0", dao.tinhTong(0));
        model.addAttribute("tt1", dao.tinhTong(1));
        model.addAttribute("tt2", dao.tinhTong(2));
        model.addAttribute("tt3", dao.tinhTong(3));
        model.addAttribute("tt4", dao.tinhTong(4));
        model.addAttribute("tt5", dao.tinhTong(5));
        model.addAttribute("tt6", dao.tinhTong(6));
        model.addAttribute("tt7", dao.findAll(p).getTotalElements());
        return "admin/qlhoadon";
    }

    @GetMapping("da-huy")
    public String daHuy(Model model, @RequestParam("page") Optional<Integer> pageParam,
                        @ModelAttribute("hdcustom") HoaDonCustom info) {
        int page = pageParam.orElse(0);
        Pageable p = PageRequest.of(page, 5);
        Page<HoaDon> lst = dao.timKiemTT(6, p);
        model.addAttribute("lst", lst);
        model.addAttribute("pageNo", page);
        model.addAttribute("checkTT", 6);
        model.addAttribute("tt0", dao.tinhTong(0));
        model.addAttribute("tt1", dao.tinhTong(1));
        model.addAttribute("tt2", dao.tinhTong(2));
        model.addAttribute("tt3", dao.tinhTong(3));
        model.addAttribute("tt4", dao.tinhTong(4));
        model.addAttribute("tt5", dao.tinhTong(5));
        model.addAttribute("tt6", dao.tinhTong(6));
        model.addAttribute("tt7", dao.findAll(p).getTotalElements());
        return "admin/qlhoadon";
    }

    @GetMapping("chi-tiet/{id}")
    public String chiTiet(Model model, @PathVariable("id") Integer id) {
        idhdshowdetail = id;
        return "redirect:/hoa-don/showDetail";
    }

    @GetMapping("viewNVChanges")
    @ResponseBody
    public ResponseEntity<?> getlistNV(@RequestParam("pageNVChanges") Optional<Integer> pageParam) {
        Pageable p = PageRequest.of(pageParam.orElse(0), 5);
        Page<NhanVien> pageNV = nhanVienService.findAll(p);
        return ResponseEntity.ok(pageNV);
    }

    @GetMapping("viewSPChanges")
    @ResponseBody
    public ResponseEntity<?> getlistSP(@RequestParam("pageSPChanges") Optional<Integer> pageParam) {
        Pageable p = PageRequest.of(pageParam.orElse(0), 5);
        Page<SanPhamChiTiet> pageSP = daoSPCT.finAllPage(p);
        return ResponseEntity.ok(pageSP);
    }

    //xem chi tiết hóa đơn
    @GetMapping("showDetail")
    public String show(Model model, @ModelAttribute("ghichu") LichSuHoaDonCustom noidung,
                       @RequestParam("pageSP") Optional<Integer> pageSP,
                       @ModelAttribute("thayDoiTT") ThayDoiTTHoaDon_KHInfo ThongTinKHChange
    ) {

        int pageDetail = pageSP.orElse(0);
        Pageable p = PageRequest.of(pageDetail, 5);


        HoaDon hoaDonXem = new HoaDon();
        PhuongThucThanhToan phuongThuc = new PhuongThucThanhToan();
        List<HoaDon> hoaDonTim = dao.timTheoID(idhdshowdetail);
        hoaDonXem = hoaDonTim.get(0);
        List<PhuongThucThanhToan> lstPhuongThuc = daoPT.timTheoHoaDon(hoaDonXem);
        List<LichSuHoaDon> lstLichSuHoaDon = daoLS.timLichSuTheoIDHoaDon(hoaDonXem);
        phuongThuc = lstPhuongThuc.get(0);
        List<PhieuGiamGiaChiTiet> lstPGGCT = daoPGGCT.timListPhieuTheoHD(hoaDonXem);
        PhieuGiamGiaChiTiet phieuGiamCT = lstPGGCT.get(0);
        BigDecimal tongTienSP = new BigDecimal("0");
        List<HoaDonChiTiet> lstHDCT = daoHDCT.getListSPHD(hoaDonXem);
        for (HoaDonChiTiet b : lstHDCT
        ) {
            tongTienSP = tongTienSP.add(b.getGiasanpham().multiply(new BigDecimal(b.getSoluong())));
        }
        BigDecimal tongTT = (tongTienSP.add(hoaDonXem.getPhivanchuyen())).subtract(phieuGiamCT.getTiengiam());
        List<String> diachiLst = Arrays.asList(hoaDonXem.getDiachi().split(", "));
        String diachiCT = diachiLst.get(0);
        String xa = diachiLst.get(1);
        String huyen = diachiLst.get(2);
        String tinh = diachiLst.get(3);
        ThayDoiTTHoaDon_KHInfo formChangesTTKH = new ThayDoiTTHoaDon_KHInfo(
                hoaDonXem.getTennguoinhan(), hoaDonXem.getSdt(),
                tinh, huyen, xa, diachiCT, hoaDonXem.getPhivanchuyen(), hoaDonXem.getGhichu()
        );
        model.addAttribute("thayDoiTT", formChangesTTKH);
        model.addAttribute("tongTT", tongTT);
        model.addAttribute("hoaDonDT", hoaDonXem);
        model.addAttribute("pageNo", pageDetail);
        model.addAttribute("lstphuongThucTT", lstPhuongThuc);
        model.addAttribute("phuongThucTT", phuongThuc);
        model.addAttribute("lstlichsu", lstLichSuHoaDon);
        model.addAttribute("phieuGiamCT", phieuGiamCT);
        model.addAttribute("trangThaiHienTai", lstLichSuHoaDon.get(lstLichSuHoaDon.size() - 1).getTrangthai());
        model.addAttribute("pageSPHD", daoHDCT.getDSSPHD(hoaDonXem, p));

        return "admin/qlchitiethoadon";
    }

    //xác nhận đơn
    @GetMapping("xac-nhan-don")
    public String xacNhanHD(Model model, @ModelAttribute("ghichu") LichSuHoaDonCustom noidung) {
        String ngaytao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        HoaDon hoadon = new HoaDon();
        hoadon.setId(idhdshowdetail);
        //fake id nhân viên thao tác
        NhanVien nhanvien = new NhanVien();
        nhanvien.setId(1);
        //end fake id nhân viên thao tác
        LichSuHoaDon lshd = new LichSuHoaDon();
        lshd.setHoadon(hoadon);
        lshd.setNhanvien(nhanvien);
        lshd.setGhichu(noidung.getKey());
        lshd.setNgaytao(Timestamp.valueOf(ngaytao));
        List<HoaDon> lstSaveHD = dao.timTheoID(idhdshowdetail);
        HoaDon hdTT = lstSaveHD.get(0);
        Integer trangthaiset = hdTT.getTrangthai() + 1;
        hdTT.setTrangthai(trangthaiset);
        dao.capNhatHD(hdTT);
        lshd.setTrangthai(trangthaiset);
        daoLS.add(lshd);
        return "redirect:/hoa-don/showDetail";
    }

    //xác nhận quay lại
    @GetMapping("quay-lai")
    public String quayLai(Model model, @ModelAttribute("ghichu") LichSuHoaDonCustom noidung) {
        String ngaytao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        HoaDon hoadon = new HoaDon();
        hoadon.setId(idhdshowdetail);
        //fake id nhân viên thao tác
        NhanVien nhanvien = new NhanVien();
        nhanvien.setId(1);
        //end fake id nhân viên thao tác
        LichSuHoaDon lshd = new LichSuHoaDon();
        lshd.setHoadon(hoadon);
        lshd.setNhanvien(nhanvien);
        lshd.setGhichu(noidung.getKey());
        lshd.setNgaytao(Timestamp.valueOf(ngaytao));
        List<HoaDon> lstSaveHD = dao.timTheoID(idhdshowdetail);
        HoaDon hdTT = lstSaveHD.get(0);
        Integer trangthaiset = hdTT.getTrangthai() - 1;
        hdTT.setTrangthai(trangthaiset);
        dao.capNhatHD(hdTT);
        lshd.setTrangthai(trangthaiset);
        daoLS.add(lshd);
        return "redirect:/hoa-don/showDetail";
    }

    //xác nhận hủy
    @GetMapping("huy-don")
    public String huyDon(Model model, @ModelAttribute("ghichu") LichSuHoaDonCustom noidung) {
        String ngaytao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        HoaDon hoadon = new HoaDon();
        hoadon.setId(idhdshowdetail);
        //fake id nhân viên thao tác
        NhanVien nhanvien = new NhanVien();
        nhanvien.setId(1);
        //end fake id nhân viên thao tác
        LichSuHoaDon lshd = new LichSuHoaDon();
        lshd.setHoadon(hoadon);
        lshd.setNhanvien(nhanvien);
        lshd.setGhichu(noidung.getKey());
        lshd.setNgaytao(Timestamp.valueOf(ngaytao));
        List<HoaDon> lstSaveHD = dao.timTheoID(idhdshowdetail);
        HoaDon hdTT = lstSaveHD.get(0);
        hdTT.setTrangthai(6);
        dao.capNhatHD(hdTT);
        lshd.setTrangthai(6);
        daoLS.add(lshd);
        return "redirect:/hoa-don/showDetail";
    }

    // call sang bán tại quầy
    @GetMapping("ban-hang")
    public String taoMoiHoaDon(Model model) {
        return "admin/banhangtaiquay";
    }

    //thay đổi tt khách tại hdct
    @PostMapping("ChangesTTHD")
    public String changesTTDH(Model model, @ModelAttribute("thayDoiTT") ThayDoiTTHoaDon_KHInfo TTChanges) {
        List<HoaDon> hd = dao.timTheoID(idhdshowdetail);
        HoaDon hdset = hd.get(0);
        hdset.setTennguoinhan(TTChanges.getTen());
        hdset.setSdt(TTChanges.getSdt());
        String diachi = TTChanges.getTinh() + ", " + TTChanges.getHuyen() + ", " + TTChanges.getXa() + ", " + TTChanges.getDiachiCT();
        hdset.setDiachi(diachi.trim());
        hdset.setPhivanchuyen(TTChanges.getPhigiao());
        hdset.setGhichu(TTChanges.getGhichu());
        dao.capNhatHD(hdset);

        return "redirect:/hoa-don/showDetail";
    }

    // lựa chọn nhân viên thay thế tại hdct
    @GetMapping("ChoseNV/{id}")
    public String choseNV(@PathVariable("id") Integer id) {
        List<HoaDon> hd = dao.timTheoID(idhdshowdetail);
        HoaDon hdset = hd.get(0);
        Optional<NhanVien> nv = nhanVienService.findById(id);
        hdset.setNhanvien(nv.get());
        dao.capNhatHD(hdset);
        return "redirect:/hoa-don/showDetail";
    }

    // thêm sản phẩm tại hdct
    @GetMapping("ChoseSP/{id}")
    public String choseSP(@PathVariable("id") Integer id) {
        SanPhamChiTiet spct = daoSPCT.findById(id);
        SanPhamChiTiet spctCapNhatSL = spct;
        spctCapNhatSL.setSoluong(spctCapNhatSL.getSoluong() - 1);
        List<HoaDon> hd = dao.timTheoID(idhdshowdetail);
        HoaDon hdset = hd.get(0);
        Boolean result = daoHDCT.checkHDCT(hdset, spct);

        if (result == true) {
            List<HoaDonChiTiet> lstTim = daoHDCT.timHDCT(hdset, spct);
            HoaDonChiTiet hdct = lstTim.get(0);
            int sl = hdct.getSoluong() + 1;
            hdct.setSoluong(sl);
            daoSPCT.addSPCT(spctCapNhatSL);
            daoHDCT.capnhat(hdct);
            return "redirect:/hoa-don/showDetail";
        }
        HoaDonChiTiet hdctNew = new HoaDonChiTiet();
        hdctNew.setHoadon(hdset);
        hdctNew.setSanphamchitiet(spct);
        hdctNew.setSoluong(1);
        hdctNew.setTrangthai(true);
        hdctNew.setGiasanpham(spct.getGiatien());
        daoSPCT.addSPCT(spctCapNhatSL);
        daoHDCT.capnhat(hdctNew);
        return "redirect:/hoa-don/showDetail";
    }

    // tìm kiếm nhân viên muốn thay đổi
    @GetMapping("searchNV")
    @ResponseBody
    public ResponseEntity<?> tìmlistNV(@RequestParam("keySearch") String key) {
        List<NhanVien> pageNV = nhanVienService.timNVTheoMa(key);
        return ResponseEntity.ok(pageNV);
    }

    // tìm kiếm sảm phẩm theo bộ lọc
    @GetMapping("searchSP")
    @ResponseBody
    public ResponseEntity<?> timSPHDCT(@RequestParam("lstData") List<String> lstData
    ) {
        String ten = lstData.get(0).equalsIgnoreCase("all") ? "" : lstData.get(0);
        String ChatLieu = lstData.get(1).equalsIgnoreCase("all") ? "" : lstData.get(1);
        String ThuongHieu = lstData.get(2).equalsIgnoreCase("all") ? "" : lstData.get(2);
        String De = lstData.get(3).equalsIgnoreCase("all") ? "" : lstData.get(3);
        String KichCo = lstData.get(4).equalsIgnoreCase("all") ? "" : lstData.get(4);
        String MauSac = lstData.get(5).equalsIgnoreCase("all") ? "" : lstData.get(5);
        Boolean GioiTinh = Boolean.valueOf(lstData.get(6));
        BigDecimal KhoangGia = BigDecimal.valueOf(Double.valueOf(lstData.get(7)));
        List<SanPhamChiTiet> lst = daoSPCT.timSPCTHDCT(ten, ChatLieu, ThuongHieu, De, KichCo, MauSac, GioiTinh, KhoangGia);
        System.out.println("aaaaaaaaaa");
        System.out.println(ten);
        System.out.println(ChatLieu);
        System.out.println(ThuongHieu);
        System.out.println(De);
        System.out.println(KichCo);
        System.out.println(MauSac);
        System.out.println(GioiTinh);
        System.out.println(KhoangGia);

        return ResponseEntity.ok(lst);
    }


    @ModelAttribute("dsChatLieu")
    public List<ChatLieu> dsChatLieu() {
        return daoChatLieu.findAll();
    }


    @ModelAttribute("dsThuongHieu")
    public List<ThuongHieu> dsThuongHieu() {
        return daoThuongHieu.findAll();
    }

    @ModelAttribute("dsDe")
    public List<DeGiay> dsDe() {
        return daoDeGiay.findAll();
    }


    @ModelAttribute("dsKichCo")
    public List<KichCo> dsKichCo() {
        return daoKichCo.findAll();
    }

    @ModelAttribute("dsMauSac")
    public List<MauSac> dsMauSac() {
        return daoMauSac.findAll();
    }


}
