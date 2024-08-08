package com.example.demo.controller.hoadon;

import com.example.demo.entity.*;
import com.example.demo.info.*;
import com.example.demo.repository.*;
import com.example.demo.repository.PhieuGiamGiaChiTiet.PhieuGiamChiTietRepository;
import com.example.demo.repository.hoadon.HoaDonChiTietRepository;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.restcontroller.khachhang.Province;
import com.example.demo.service.*;
import com.example.demo.service.impl.NguoiDungImpl1;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("hoa-don")
public class hoaDonController {
    @Autowired
    SanPhamDotGiamRepository SPdotgiamRepo;
    @Autowired
    NguoiDungRepository daoNguoiDung;
    @Autowired
    NhanVienRepository nhanvienRPo;
    @Autowired
    PhieuGiamChiTietRepository daoPGGCTRepo;
    @Autowired
    HoaDonRepository daoRepo;
    @Autowired
    PhieuGiamGiaRepository daoPGG;
    @Autowired
    KhachHangPhieuGiamRepository daoKHPG;
    @Autowired
    SpringTemplateEngine dao1;
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
    @Autowired
    NguoiDungImpl1 nguoiDung;
    Integer idhdshowdetail = null;
    HoaDonCustom hdSaveInfoSeachr = new HoaDonCustom();
    List<String> lstdiachigiao = new ArrayList<>();

    private static String encodeFileToBase64Binary(File file) throws IOException {
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStreamReader.read(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

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

            if (hdSaveInfoSeachr.getKey().equalsIgnoreCase("null")
                    && hdSaveInfoSeachr.getLoaiHD().equalsIgnoreCase("null")
                    && hdSaveInfoSeachr.getTu().equalsIgnoreCase("null")
                    && hdSaveInfoSeachr.getDen().equalsIgnoreCase("null")) {
                lst = dao.findAll(p);
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
        if (hdSaveInfoSeachr.getKey().equalsIgnoreCase("null")) {
            model.addAttribute("checkKeyNull", true);
        }
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
        Page<SanPhamChiTiet> pageSP = daoSPCT.finAllPage(0, p);
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
        if (lstPhuongThuc.size() > 0) {
            phuongThuc = lstPhuongThuc.get(0);
        } else {
            phuongThuc = new PhuongThucThanhToan();
        }

        List<PhieuGiamGiaChiTiet> lstPGGCT = daoPGGCT.timListPhieuTheoHD(hoaDonXem);
        PhieuGiamGiaChiTiet phieuGiamCT = new PhieuGiamGiaChiTiet();
        if (lstPGGCT.size() > 0) {
            phieuGiamCT = lstPGGCT.get(0);
        }
        BigDecimal tongTienSP = new BigDecimal("0");
        List<HoaDonChiTiet> lstHDCT = daoHDCT.getListSPHD(hoaDonXem);

        int page = 0;
        Page<HoaDonChiTiet> hoaDonPage = daoHDCT.getDSSPHD(hoaDonXem, p);
        List<SanPhamDotGiam> lstsanphamdotgiam = new ArrayList<>();
        do {
            Pageable pageable = PageRequest.of(page, 5);
            hoaDonPage = daoHDCT.getDSSPHD(hoaDonXem, pageable);
            List<HoaDonChiTiet> hoaDonChiTiets = hoaDonPage.getContent();
            for (HoaDonChiTiet a : hoaDonChiTiets) {
                List<SanPhamDotGiam> lsts = SPdotgiamRepo.findBySanphamchitiet(a.getSanphamchitiet());
                if (lsts.size() > 0) {
                    for (SanPhamDotGiam b : lsts
                    ) {
                        if (b.getDotgiamgia().getTrangthai() == 1) {
                            lstsanphamdotgiam.add(b);
                        }
                    }
                }
            }
            page++;
        } while (hoaDonPage.hasNext());

        for (HoaDonChiTiet b : lstHDCT
        ) {
            tongTienSP = tongTienSP.add(b.getGiasanpham().multiply(new BigDecimal(b.getSoluong())));
        }

        BigDecimal tongTT = (tongTienSP.add(hoaDonXem.getPhivanchuyen())).subtract(phieuGiamCT.getTiengiam() == null ? new BigDecimal("0") : phieuGiamCT.getTiengiam());

        List<String> diachiLst = Arrays.asList(hoaDonXem.getDiachi().split(", "));
        String diachiCT = diachiLst.get(0);
        String xa = diachiLst.get(1);
        String huyen = diachiLst.get(2);
        String tinh = diachiLst.get(3);
        ThayDoiTTHoaDon_KHInfo formChangesTTKH = new ThayDoiTTHoaDon_KHInfo(
                hoaDonXem.getTennguoinhan(), hoaDonXem.getSdt(),
                tinh, huyen, xa, diachiCT, hoaDonXem.getPhivanchuyen() + "", hoaDonXem.getGhichu()
        );
        //tìm list đợt giảm giá

        //gửi địa chỉ giao
        lstdiachigiao = new ArrayList<>();
        lstdiachigiao.add(tinh);
        lstdiachigiao.add(huyen);
        lstdiachigiao.add(xa);
        model.addAttribute("tongTienSP", tongTienSP);
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
        model.addAttribute("lstsanphamdotgiam", lstsanphamdotgiam);
        model.addAttribute("checksizes", lstsanphamdotgiam.size()>0?true:false);
        return "admin/qlchitiethoadon";
    }

    //ap dụng voucher với off
    @GetMapping("ap-dung-voucher/{id}")
    public String addvoucherselect(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        PhieuGiamGia phieutim = daoPGG.findPhieuGiamGiaById(Integer.valueOf(id));
        BigDecimal tongTienSP = new BigDecimal("0");
        BigDecimal sotiengiam = new BigDecimal("0");
        HoaDon hds = daoRepo.findById(idhdshowdetail).get();

        List<HoaDonChiTiet> lstHDCT = daoHDCT.getListSPHD(hds);
//tính tổng tiền đơn
        for (HoaDonChiTiet b : lstHDCT
        ) {
            tongTienSP = tongTienSP.add(b.getGiasanpham().multiply(new BigDecimal(b.getSoluong())));
        }
        if (tongTienSP.compareTo(phieutim.getDontoithieu()) < 0) {
            redirectAttributes.addFlashAttribute("addVoucherfail", true);
            return "redirect:/hoa-don/showDetail";
        }
// tính lại số tiền giảm
        sotiengiam = new BigDecimal("0");
        if (phieutim.getLoaiphieu() == true) {
            // phiếu %
            if ((new BigDecimal(phieutim.getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100"))).compareTo(phieutim.getGiatrigiamtoida()) > 0) {
                sotiengiam = phieutim.getGiatrigiamtoida();
            } else {
                sotiengiam = (new BigDecimal(phieutim.getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100")));
            }

        } else {
            // phiếu vnđ
            sotiengiam = new BigDecimal(phieutim.getGiatrigiam());
        }

        BigDecimal tongTT = (tongTienSP.add(hds.getPhivanchuyen())).subtract(sotiengiam);
        PhuongThucThanhToan ptttTim = daoPT.timTheoHoaDon(hds).get(0);
        ptttTim.setTongtien(tongTT);
        daoPT.add_update(ptttTim);
        hds.setTongtien(tongTT);
        dao.capNhatHD(hds);
        PhieuGiamGiaChiTiet phieuGiamGiaChiTietTim = new PhieuGiamGiaChiTiet();
        List<PhieuGiamGiaChiTiet> lst = daoPGGCT.timListPhieuTheoHD(hds);
        if (lst.size() > 0) {
            phieuGiamGiaChiTietTim = lst.get(0);
        } else {
            phieuGiamGiaChiTietTim.setHoadon(hds);
            phieuGiamGiaChiTietTim.setPhieugiamgia(phieutim);
            phieuGiamGiaChiTietTim.setGiabandau(tongTienSP);
            phieuGiamGiaChiTietTim.setGiasauapdung(tongTT);
            phieuGiamGiaChiTietTim.setTiengiam(sotiengiam);
        }
        daoPGGCTRepo.save(phieuGiamGiaChiTietTim);
        redirectAttributes.addFlashAttribute("addVoucherSucsess", true);
        return "redirect:/hoa-don/showDetail";
    }

    @GetMapping("call-api-ngay-giao")
    @ResponseBody
    public ResponseEntity<?> callapi() {
        return ResponseEntity.ok(lstdiachigiao);
    }

    @GetMapping("set-ngay-giao-du-kien/{date}")
    @ResponseBody
    public void setdate(@PathVariable("date") String date) {
        Long unixTimestamp = Long.valueOf(date);
        Long milliseconds = unixTimestamp * 1000;
        Timestamp timestamp = new Timestamp(milliseconds);
        HoaDon hoaDonXem = new HoaDon();
        List<HoaDon> hoaDonTim = dao.timTheoID(idhdshowdetail);
        hoaDonXem = hoaDonTim.get(0);
        hoaDonXem.setNgaygiaodukien(timestamp);
        dao.capNhatHD(hoaDonXem);

    }

    public static String convertCurrency(String formattedAmount) {
        // Xóa ký hiệu "₫" và các dấu phân cách
        String numericAmount = formattedAmount.replaceAll("[^\\d]", "");
        return numericAmount;
    }

    //xác nhận đơn
    @GetMapping("xac-nhan-don")
    public String xacNhanHD(Model model, @ModelAttribute("ghichu") LichSuHoaDonCustom noidung, RedirectAttributes redirectAttributes, HttpSession session) {
        String ngaytao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        HoaDon hoadon = new HoaDon();
        hoadon.setId(idhdshowdetail);
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);
        String tennv = nv.getNguoidung().getHovaten();
        //fake id nhân viên thao tác
        NhanVien nhanvien = nv;

        //end fake id nhân viên thao tác
        LichSuHoaDon lshd = new LichSuHoaDon();
        lshd.setHoadon(hoadon);
        lshd.setNhanvien(nhanvien);
        lshd.setGhichu(noidung.getKey());
        lshd.setNgaytao(Timestamp.valueOf(ngaytao));
        List<HoaDon> lstSaveHD = dao.timTheoID(idhdshowdetail);
        HoaDon hdTT = lstSaveHD.get(0);
        if (hdTT.getTrangthai() == 0 && hdTT.getLoaihoadon() == true) {
            //đơn online thì trừ số lượng vào kho
            List<HoaDonChiTiet> lsdthdctdownSL = daoHDCT.getListSPHD(hdTT);
            for (HoaDonChiTiet a : lsdthdctdownSL
            ) {
                SanPhamChiTiet sanPhamChiTietUd = daoSPCT.findById(a.getSanphamchitiet().getId());
                sanPhamChiTietUd.setSoluong(sanPhamChiTietUd.getSoluong() - a.getSoluong());
                daoSPCT.addSPCT(sanPhamChiTietUd);
            }

        }
        Integer trangthaiset = hdTT.getTrangthai() + 1;

        if (trangthaiset == 4) {
            List<PhuongThucThanhToan> lstpttim = daoPT.timTheoHoaDon(hdTT);
            for (PhuongThucThanhToan a : lstpttim
            ) {
                a.setTrangthai(true);
                daoPT.add_update(a);
            }
        }
        //set ngày xác nhận
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (trangthaiset == 1) {
            hdTT.setNgayxacnhan(Timestamp.valueOf(currentDateTime));
        } else {
            if (trangthaiset == 3) {
                hdTT.setNgayvanchuyen(Timestamp.valueOf(currentDateTime));
            } else {
                if (trangthaiset == 4) {
                    hdTT.setNgaynhanhang(Timestamp.valueOf(currentDateTime));
                } else {
                    if (trangthaiset == 5) {
                        hdTT.setNgayhoanthanh(Timestamp.valueOf(currentDateTime));
                    } else {

                    }
                }
            }
        }
        //set lần cập nhât cuối
        hdTT.setLancapnhatcuoi(Timestamp.valueOf(currentDateTime));
        //set người cập nhật theo id đã login
        hdTT.setNguoicapnhat(nhanvien.getNguoidung().getHovaten());
        //
        hdTT.setTrangthai(trangthaiset);
        if (trangthaiset == 2) {
            String to = hdTT.getEmail();
            String mahdemail = hdTT.getMahoadon();
            String ngaytaoemail = Timestamp.valueOf(ngaytao) + "";
            String subject = "Chúc mừng bạn đã đặt thành công đơn hàng của T&T shop";
            String mailType = "";
            String mailContent = "Mã vận đơn của bạn là: " + mahdemail + "\nNgày tạo: " + ngaytaoemail;
            nguoiDung.sendEmail(to, subject, mailType, mailContent);
            redirectAttributes.addFlashAttribute("sendmail", true);
            PhieuGiamGia phieuGiamGiasaveSl = new PhieuGiamGia();
            List<PhieuGiamGiaChiTiet> lstPGGCTs = daoPGGCT.timListPhieuTheoHD(hdTT);
            if (lstPGGCTs.size() > 0) {
                phieuGiamGiasaveSl = lstPGGCTs.get(0).getPhieugiamgia();
                if (phieuGiamGiasaveSl.getSoluong() > 0) {
                    phieuGiamGiasaveSl.setSoluong(phieuGiamGiasaveSl.getSoluong() - 1);
                    if (phieuGiamGiasaveSl.getSoluong() == 0) {
                        phieuGiamGiasaveSl.setTrangthai(2);
                    }
                    daoPGG.save(phieuGiamGiasaveSl);
                }
            }

        }
        dao.capNhatHD(hdTT);
        lshd.setTrangthai(trangthaiset);
        daoLS.add(lshd);
        List<HoaDonChiTiet> lstsp = daoHDCT.getListSPHD(hdTT);
        List<sanPhamIn> lstin = new ArrayList<>();
        BigDecimal tongTienSP = new BigDecimal("0");
        List<PhieuGiamGiaChiTiet> lstPGGCT = daoPGGCT.timListPhieuTheoHD(hdTT);
        PhieuGiamGiaChiTiet phieuGiamCT = new PhieuGiamGiaChiTiet();
        phieuGiamCT.setTiengiam(new BigDecimal("0"));
        if (lstPGGCT.size() > 0) {
            phieuGiamCT = lstPGGCT.get(0);
        }
        for (HoaDonChiTiet a : lstsp
        ) {
            tongTienSP = tongTienSP.add(a.getGiasanpham().multiply(new BigDecimal(a.getSoluong())));
            lstin.add(new sanPhamIn(a.getSanphamchitiet().getSanpham().getTensanpham(), a.getSoluong()));

        }
        BigDecimal tongTT = (tongTienSP.add(hdTT.getPhivanchuyen())).subtract(phieuGiamCT.getTiengiam());


        MauHoaDon u = new MauHoaDon("FSPORT", hdTT.getMahoadon(), hdTT.getNgaytao(), "Lô H023, Nhà số 39, Ngõ 148, Xuân Phương, Phương Canh,Nam Từ Liêm, Hà Nội",
                hdTT.getDiachi(), "0379036607", hdTT.getSdt(), hdTT.getTennguoinhan(), lstin, tongTT, "", tennv);
        String finalhtml = null;
        //tạo qr
        String qrCodeText = hdTT.getMahoadon(); // Chuỗi để tạo QR
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
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", new File("src/main/resources/static/" + hdTT.getMahoadon() + ".png").toPath());
            // Thay đổi đường dẫn của thư mục chứa ảnh PNG của bạn ở đây
            // Đường dẫn tới file PNG của bạn
            String filePath = "src/main/resources/static/" + hdTT.getMahoadon() + ".png";

            File file = new File(filePath);

            if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                try {
                    String base64String = encodeFileToBase64Binary(file);
                    System.out.println("File: " + file.getName());
                    u.setQr(base64String);
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

        Context data = dao.setData(u);
        finalhtml = dao1.process("index", data);
        if (hdTT.getTrangthai() == 2) {
            dao.htmlToPdf(finalhtml, hdTT.getMahoadon());
        }
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
        //check đơn online ở trạng thái đã xác nhận về chờ xác nhận
        if (hdTT.getTrangthai() == 1) {
            List<HoaDonChiTiet> lsdthdctdownSL = daoHDCT.getListSPHD(hdTT);
            for (HoaDonChiTiet a : lsdthdctdownSL
            ) {
                SanPhamChiTiet sanPhamChiTietUd = daoSPCT.findById(a.getSanphamchitiet().getId());
                sanPhamChiTietUd.setSoluong(sanPhamChiTietUd.getSoluong() + a.getSoluong());
                daoSPCT.addSPCT(sanPhamChiTietUd);
            }
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        hdTT.setLancapnhatcuoi(Timestamp.valueOf(currentDateTime));
        //set người cập nhật theo id đã login

        //
        hdTT.setTrangthai(trangthaiset);
        dao.capNhatHD(hdTT);
        lshd.setTrangthai(trangthaiset);
        daoLS.add(lshd);
        return "redirect:/hoa-don/showDetail";
    }

    @GetMapping("show-all-voucher")
    @ResponseBody
    public ResponseEntity<?> showAllMa() {
        List<HoaDon> lstSaveHD = dao.timTheoID(idhdshowdetail);
        HoaDon hdHienTai = lstSaveHD.get(0);
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
        LocalDateTime currentDateTime = LocalDateTime.now();
        hdTT.setLancapnhatcuoi(Timestamp.valueOf(currentDateTime));
        //set người cập nhật theo id đã login

        //
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
    public String changesTTDH(Model model, @ModelAttribute("thayDoiTT") ThayDoiTTHoaDon_KHInfo TTChanges, RedirectAttributes redirectAttributes) {
        List<HoaDon> hd = dao.timTheoID(idhdshowdetail);
        HoaDon hdset = hd.get(0);
        hdset.setTennguoinhan(TTChanges.getTen().trim().replaceAll("\\s+", " "));
        hdset.setSdt(TTChanges.getSdt().trim());
        String diachi = TTChanges.getDiachiCT() + ", " + TTChanges.getXa() + ", " + TTChanges.getHuyen() + ", " + TTChanges.getTinh();
        hdset.setDiachi(diachi.trim().replaceAll("\\s+", " "));
        hdset.setPhivanchuyen(BigDecimal.valueOf(Double.valueOf(convertCurrency(TTChanges.getPhigiao()))));
        hdset.setGhichu(TTChanges.getGhichu().trim().replaceAll("\\s+", " "));
        dao.capNhatHD(hdset);
        redirectAttributes.addFlashAttribute("chagesTTHDSucsess", true);
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
    public String choseSP(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        SanPhamChiTiet spct = daoSPCT.findById(id);
        SanPhamChiTiet spctCapNhatSL = spct;


        List<HoaDon> hd = dao.timTheoID(idhdshowdetail);
        HoaDon hdset = hd.get(0);
        if (hdset.getLoaihoadon() == false) {
            spctCapNhatSL.setSoluong(spctCapNhatSL.getSoluong() - 1);
        }
        Boolean result = daoHDCT.checkHDCT(hdset, spct);

        if (result == true) {
            List<HoaDonChiTiet> lstTim = daoHDCT.timHDCT(hdset, spct);
            HoaDonChiTiet hdct = lstTim.get(0);
            int sl = hdct.getSoluong() + 1;
            hdct.setSoluong(sl);
            daoSPCT.addSPCT(spctCapNhatSL);
            daoHDCT.capnhat(hdct);
            redirectAttributes.addFlashAttribute("addProductSuccsess", true);
            return "redirect:/hoa-don/showDetail";
        }
        HoaDonChiTiet hdctNew = new HoaDonChiTiet();
        hdctNew.setHoadon(hdset);
        hdctNew.setSanphamchitiet(spct);
        hdctNew.setSoluong(1);
        hdctNew.setTrangthai(true);
        List<SanPhamDotGiam> lst = SPdotgiamRepo.findBySanphamchitiet(spct);
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
            hdctNew.setGiasanpham((spct.getGiatien().divide(new BigDecimal("100"))).multiply(new BigDecimal(discountbacks+"")));
        } else {
            hdctNew.setGiasanpham(spct.getGiatien());
        }
        daoSPCT.addSPCT(spctCapNhatSL);
        daoHDCT.capnhat(hdctNew);
        PhieuGiamGiaChiTiet pgctTim = daoPGGCT.timListPhieuTheoHD(hdset).size() > 0 ? daoPGGCT.timListPhieuTheoHD(hdset).get(0) : new PhieuGiamGiaChiTiet();

        BigDecimal tongTienSP = new BigDecimal("0");
        BigDecimal sotiengiam = new BigDecimal("0");
        List<HoaDonChiTiet> lstHDCT = daoHDCT.getListSPHD(hdset);
        for (HoaDonChiTiet b : lstHDCT
        ) {
            tongTienSP = tongTienSP.add(b.getGiasanpham().multiply(new BigDecimal(b.getSoluong())));
        }
        if (pgctTim.getPhieugiamgia() != null) {
            if (pgctTim.getPhieugiamgia().getLoaiphieu() == true) {
                // phiếu %
                if ((new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100"))).compareTo(pgctTim.getPhieugiamgia().getGiatrigiamtoida()) > 0) {
                    sotiengiam = pgctTim.getPhieugiamgia().getGiatrigiamtoida();
                } else {
                    sotiengiam = (new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100")));
                }

            } else {
                // phiếu vnđ
                sotiengiam = new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam());
            }
        }
//set lại tổng tiền ở phương thức thanh toán


        BigDecimal tongTT = (tongTienSP.add(hdset.getPhivanchuyen())).subtract(sotiengiam);
        PhuongThucThanhToan ptttTim = daoPT.timTheoHoaDon(hdset).get(0);
        ptttTim.setTongtien(tongTT);
        daoPT.add_update(ptttTim);
        hdset.setTongtien(tongTT);
        dao.capNhatHD(hdset);
        redirectAttributes.addFlashAttribute("addProductSuccsess", true);
        return "redirect:/hoa-don/showDetail";
    }

    // tìm kiếm nhân viên muốn thay đổi
    @GetMapping("searchNV")
    @ResponseBody
    public ResponseEntity<?> timlistNV(@RequestParam("keySearch") String key) {
        List<NhanVien> pageNV = nhanVienService.timNVTheoMa(key);
        return ResponseEntity.ok(pageNV);
    }

    // tìm kiếm sảm phẩm theo bộ lọc
    @GetMapping("searchSP")
    @ResponseBody
    public ResponseEntity<?> timSPHDCT(@RequestParam("lstData") String lstDataStr
    ) {
        String[] lstDataArray = lstDataStr.split(",");

        // Giải mã các phần tử trong mảng
        List<String> lstData = Arrays.stream(lstDataArray)
                .map(item -> URLDecoder.decode(item, StandardCharsets.UTF_8))
                .collect(Collectors.toList());

        String ten = lstData.get(0).equalsIgnoreCase("all") ? "" : lstData.get(0);
        String ChatLieu = lstData.get(1).equalsIgnoreCase("all") ? "" : lstData.get(1);
        String ThuongHieu = lstData.get(2).equalsIgnoreCase("all") ? "" : lstData.get(2);
        String De = lstData.get(3).equalsIgnoreCase("all") ? "" : lstData.get(3);
        String KichCo = lstData.get(4).equalsIgnoreCase("all") ? "" : lstData.get(4);
        String MauSac = lstData.get(5).equalsIgnoreCase("all") ? "" : lstData.get(5);
        Boolean GioiTinh = Boolean.valueOf(lstData.get(6));
        BigDecimal KhoangGia = BigDecimal.valueOf(Double.valueOf(lstData.get(7)));
        List<SanPhamChiTiet> lst = daoSPCT.timSPCTHDCT(ten, ChatLieu, ThuongHieu, De, KichCo, MauSac, GioiTinh, KhoangGia);
        return ResponseEntity.ok(lst);
    }

    //xóa sản phẩm ra khỏi hóa đơn
    @GetMapping("delete-sp-hdct/{id}")
    public String deleteSPHDCT(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        HoaDonChiTiet hdDelete = daoHDCT.findByID(id);
        HoaDon hd = dao.timHDTheoMaHD(hdDelete.getHoadon().getMahoadon());
        int slHienTai = hdDelete.getSoluong();
        daoHDCT.deleteById(id);
        if (hd.getLoaihoadon() == false) {
            SanPhamChiTiet spUpdateQuantity = hdDelete.getSanphamchitiet();
            spUpdateQuantity.setSoluong(spUpdateQuantity.getSoluong() + slHienTai);
            daoSPCT.addSPCT(spUpdateQuantity);
        }
        PhieuGiamGiaChiTiet pgctTim = daoPGGCT.timListPhieuTheoHD(hd).size() > 0 ? daoPGGCT.timListPhieuTheoHD(hd).get(0) : new PhieuGiamGiaChiTiet();

        BigDecimal tongTienSP = new BigDecimal("0");
        BigDecimal sotiengiam = new BigDecimal("0");
        List<HoaDonChiTiet> lstHDCT = daoHDCT.getListSPHD(hd);
        for (HoaDonChiTiet b : lstHDCT
        ) {
            tongTienSP = tongTienSP.add(b.getGiasanpham().multiply(new BigDecimal(b.getSoluong())));
        }
        if (pgctTim.getPhieugiamgia() != null) {
            if (tongTienSP.compareTo(pgctTim.getPhieugiamgia().getDontoithieu()) >= 0) {
                if (pgctTim.getPhieugiamgia().getLoaiphieu() == true) {
                    // phiếu %
                    if ((new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100"))).compareTo(pgctTim.getPhieugiamgia().getGiatrigiamtoida()) > 0) {
                        sotiengiam = pgctTim.getPhieugiamgia().getGiatrigiamtoida();
                    } else {
                        sotiengiam = (new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100")));
                    }

                } else {
                    // phiếu vnđ
                    sotiengiam = new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam());
                }
            } else {
                daoPGGCTRepo.deleteById(pgctTim.getId());
                redirectAttributes.addFlashAttribute("autokickvoucher", true);
            }

        }


        BigDecimal tongTT = (tongTienSP.add(hd.getPhivanchuyen())).subtract(sotiengiam);
        PhuongThucThanhToan ptttTim = daoPT.timTheoHoaDon(hd).get(0);
        ptttTim.setTongtien(tongTT);
        daoPT.add_update(ptttTim);
        hd.setTongtien(tongTT);
        dao.capNhatHD(hd);
        redirectAttributes.addFlashAttribute("updateSLSuccsess", true);
        return "redirect:/hoa-don/showDetail";
    }


    @GetMapping("update-sp-hdct/{id}/{sl}")
    public String updateSPHDCT(@PathVariable("id") Integer id, @PathVariable("sl") Integer sl, RedirectAttributes redirectAttributes) {
        HoaDonChiTiet hdDelete = daoHDCT.findByID(id);
        SanPhamChiTiet spUpdateQuantity = hdDelete.getSanphamchitiet();
        HoaDon hd = dao.timHDTheoMaHD(hdDelete.getHoadon().getMahoadon());

        PhieuGiamGiaChiTiet pgctTim = daoPGGCT.timListPhieuTheoHD(hd).size() > 0 ? daoPGGCT.timListPhieuTheoHD(hd).get(0) : new PhieuGiamGiaChiTiet();

        if (hdDelete.getSoluong() == sl) {
            redirectAttributes.addFlashAttribute("updateSLSuccsess", true);
            return "redirect:/hoa-don/showDetail";
        } else {
            if (hdDelete.getSoluong() < sl) {
                //tăng sl
                int sltang = sl - hdDelete.getSoluong();
                if (hd.getLoaihoadon() == false) {
                    spUpdateQuantity.setSoluong(spUpdateQuantity.getSoluong() - sltang);
                    daoSPCT.addSPCT(spUpdateQuantity);
                }
                hdDelete.setSoluong(sl);

                BigDecimal tongTienSP = new BigDecimal("0");
                BigDecimal sotiengiam = new BigDecimal("0");
                List<HoaDonChiTiet> lstHDCT = daoHDCT.getListSPHD(hd);
                for (HoaDonChiTiet b : lstHDCT
                ) {
                    tongTienSP = tongTienSP.add(b.getGiasanpham().multiply(new BigDecimal(b.getSoluong())));
                }

                if (pgctTim.getPhieugiamgia() != null) {
                    if (tongTienSP.compareTo(pgctTim.getPhieugiamgia().getDontoithieu()) >= 0) {
                        if (pgctTim.getPhieugiamgia().getLoaiphieu() == true) {
                            // phiếu %
                            if ((new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100"))).compareTo(pgctTim.getPhieugiamgia().getGiatrigiamtoida()) > 0) {
                                sotiengiam = pgctTim.getPhieugiamgia().getGiatrigiamtoida();
                            } else {
                                sotiengiam = (new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100")));
                            }

                        } else {
                            // phiếu vnđ
                            sotiengiam = new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam());
                        }
                    } else {
                        daoPGGCTRepo.deleteById(pgctTim.getId());
                        redirectAttributes.addFlashAttribute("autokickvoucher", true);
                    }
                }


                BigDecimal tongTT = (tongTienSP.add(hd.getPhivanchuyen())).subtract(sotiengiam);
                PhuongThucThanhToan ptttTim = daoPT.timTheoHoaDon(hd).get(0);
                ptttTim.setTongtien(tongTT);
                daoPT.add_update(ptttTim);
                daoHDCT.capnhat(hdDelete);
                hd.setTongtien(tongTT);
                dao.capNhatHD(hd);
                redirectAttributes.addFlashAttribute("updateSLSuccsess", true);
                return "redirect:/hoa-don/showDetail";
            }
        }
        //sl giảm
        int slgiam = hdDelete.getSoluong() - sl;
        if (hd.getLoaihoadon() == false) {
            spUpdateQuantity.setSoluong(spUpdateQuantity.getSoluong() + slgiam);
            daoSPCT.addSPCT(spUpdateQuantity);
        }
        hdDelete.setSoluong(sl);
        daoHDCT.capnhat(hdDelete);
        BigDecimal tongTienSP = new BigDecimal("0");
        BigDecimal sotiengiam = new BigDecimal("0");
        List<HoaDonChiTiet> lstHDCT = daoHDCT.getListSPHD(hd);
        for (HoaDonChiTiet b : lstHDCT
        ) {
            tongTienSP = tongTienSP.add(b.getGiasanpham().multiply(new BigDecimal(b.getSoluong())));
        }

        if (pgctTim.getPhieugiamgia() != null) {
            if (tongTienSP.compareTo(pgctTim.getPhieugiamgia().getDontoithieu()) >= 0) {
                if (pgctTim.getPhieugiamgia().getLoaiphieu() == true) {
                    // phiếu %
                    if ((new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100"))).compareTo(pgctTim.getPhieugiamgia().getGiatrigiamtoida()) > 0) {
                        sotiengiam = pgctTim.getPhieugiamgia().getGiatrigiamtoida();
                    } else {
                        sotiengiam = (new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam())).multiply(tongTienSP.divide(new BigDecimal("100")));
                    }

                } else {
                    // phiếu vnđ
                    sotiengiam = new BigDecimal(pgctTim.getPhieugiamgia().getGiatrigiam());
                }
            } else {
                daoPGGCTRepo.deleteById(pgctTim.getId());
                redirectAttributes.addFlashAttribute("autokickvoucher", true);
            }
        }


        BigDecimal tongTT = (tongTienSP.add(hd.getPhivanchuyen())).subtract(sotiengiam);

        PhuongThucThanhToan ptttTim = daoPT.timTheoHoaDon(hd).get(0);
        ptttTim.setTongtien(tongTT);
        daoPT.add_update(ptttTim);
        hd.setTongtien(tongTT);
        dao.capNhatHD(hd);
        redirectAttributes.addFlashAttribute("updateSLSuccsess", true);
        return "redirect:/hoa-don/showDetail";
    }

    //truyền model attribute
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

    @ModelAttribute("AddKHNhanh")
    public AddKHNhanhFormBanHang addKHNhanh() {
        return new AddKHNhanhFormBanHang(null, null, null, "chọn tỉnh", "chọn huyện", "chọn xã", null, true);
    }

    @ModelAttribute("thongtingiaohang")
    public DiaChiGiaoCaseBanHangOff diachigiaohangtaiquay() {
        return new DiaChiGiaoCaseBanHangOff(null, null, null, "chọn tỉnh", "chọn huyện", "chọn xã", null, null, null, null, "ta..", null);
    }

}
