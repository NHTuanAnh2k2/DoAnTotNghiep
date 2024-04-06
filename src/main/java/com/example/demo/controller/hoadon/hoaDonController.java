package com.example.demo.controller.hoadon;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.LichSuHoaDon;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.HoaDonCustom;
import com.example.demo.info.LichSuHoaDonCustom;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.LichSuHoaDonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.util.Optional;

@Controller
@RequestMapping("hoa-don")
public class hoaDonController {
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
                if (hdSaveInfoSeachr.getTu().compareTo(hdSaveInfoSeachr.getDen()) >= 0) {
                    Page<HoaDon> fixErr = dao.findAll(p);
                    model.addAttribute("lst", fixErr);
                    model.addAttribute("hdcustom", hdSaveInfoSeachr);
                    model.addAttribute("pageNo", page);
                    model.addAttribute("errdate", 1);
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
                if (hdSaveInfoSeachr.getTu().compareTo(hdSaveInfoSeachr.getDen()) >= 0) {
                    Page<HoaDon> fixErr = dao.findAll(p);
                    model.addAttribute("lst", fixErr);
                    model.addAttribute("hdcustom", hdSaveInfoSeachr);
                    model.addAttribute("pageNo", page);
                    model.addAttribute("errdate", 1);
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

                if (hdSaveInfoSeachr.getTu().compareTo(hdSaveInfoSeachr.getDen()) >= 0) {
                    Page<HoaDon> fixErr = dao.findAll(p);
                    model.addAttribute("lst", fixErr);
                    model.addAttribute("hdcustom", hdSaveInfoSeachr);
                    model.addAttribute("pageNo", page);
                    model.addAttribute("errdate", 1);
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
            if (hdSaveInfoSeachr.getTu().compareTo(hdSaveInfoSeachr.getDen()) >= 0) {
                Page<HoaDon> fixErr = dao.findAll(p);
                model.addAttribute("lst", fixErr);
                model.addAttribute("hdcustom", hdSaveInfoSeachr);
                model.addAttribute("pageNo", page);
                model.addAttribute("errdate", 1);
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

    //xem chi tiết hóa đơn
    @GetMapping("showDetail")
    public String show(Model model, @ModelAttribute("ghichu") LichSuHoaDonCustom noidung) {
        HoaDon hoaDonXem = new HoaDon();
        hoaDonXem.setId(idhdshowdetail);
        List<LichSuHoaDon> lstLichSuHoaDon = daoLS.timLichSuTheoIDHoaDon(hoaDonXem);
        model.addAttribute("lstlichsu", lstLichSuHoaDon);
        model.addAttribute("trangThaiHienTai", lstLichSuHoaDon.get(lstLichSuHoaDon.size() - 1).getTrangthai());
        return "admin/qlchitiethoadon";
    }

    //xác nhận ghi chú
    @GetMapping("xac-nhan-don")
    public String xacNhanHD(Model model, @ModelAttribute("ghichu") LichSuHoaDonCustom noidung) {
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
        lshd.setTrangthai(1);
        daoLS.add(lshd);
        return "redirect:/hoa-don/showDetail";
    }

    // call sang bán tại quầy
    @GetMapping("ban-hang")
    public String taoMoiHoaDon(Model model) {

        return "admin/banhangtaiquay";
    }
}
