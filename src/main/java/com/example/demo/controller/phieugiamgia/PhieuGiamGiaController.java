package com.example.demo.controller.phieugiamgia;

import com.example.demo.controller.phieugiamgia.mail.EmailService;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.KhachHangPhieuGiam;
import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.service.KhachHangPhieuGiamService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import com.example.demo.service.impl.KhachHangImp;
import com.example.demo.service.impl.KhachHangPhieuGiamImp;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
//tuna anh
//Tuan anh
@Controller
public class PhieuGiamGiaController {
    @Autowired
    private PhieuGiamGiaImp phieuGiamGiaImp;
    @Autowired
    private KhachHangImp khachHangImp;
    @Autowired
    private KhachHangPhieuGiamImp  khachHangPhieuGiamImp;
    @Autowired
    private EmailService emailService;

    @GetMapping("/admin/qldotgiamgia")
    public String qldotgiamgia() {
        return "/admin/qldotgiamgia";
    }

    @GetMapping("/admin/adddotgiamgia")
    public String adddotgiamgia() {
        return "/admin/adddotgiamgia";
    }

    @RequestMapping ("/admin/hien-thi-phieu-giam-gia")
    public String qlphieugiamgia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia, Model model,
                                 @RequestParam(defaultValue = "0") Integer p,
                                 @RequestParam(value = "keySearch",required = false) String keySearch,
                                 @RequestParam(value = "tungaySearch",required = false) String tungaySearch,
                                 @RequestParam(value = "denngaySearch",required = false) String denngaySearch,
                                 @RequestParam(value = "kieuSearch",required = false) String kieuSearch,
                                 @RequestParam(value = "loaiSearch",required = false) String loaiSearch,
                                 @RequestParam(value = "ttSearch",required = false) String ttSearch,
                                 HttpSession session) {
        Timestamp tungay;
        Timestamp denngay;
        if(tungaySearch==null ||tungaySearch.isEmpty()){
            tungay=null;
        }else{
            tungay= Timestamp.valueOf(tungaySearch.replace("T", " ") + ":00");
        }
        if(denngaySearch==null ||denngaySearch.isEmpty()){
            denngay=null;
        }else{
            denngay= Timestamp.valueOf(denngaySearch.replace("T", " ") + ":00");
        }
        Boolean kieu;
        Boolean loai;
        if((kieuSearch != null && kieuSearch.equals("true")) || (kieuSearch != null && kieuSearch.equals("false"))){
            kieu= Boolean.valueOf(kieuSearch);
        }else{
            kieu=null;
        }

        if((loaiSearch != null &&loaiSearch.equals("true")) || (loaiSearch != null &&loaiSearch.equals("false"))){
            loai= Boolean.valueOf(loaiSearch);
        }else{
            loai=null;
        }
        Integer tt;
        if((ttSearch != null && ttSearch.equals("0")) || (ttSearch != null && ttSearch.equals("1")) || (ttSearch != null && ttSearch.equals("2"))){
            tt= Integer.parseInt(ttSearch);
        }else{
            tt=null;
        }
        if(keySearch!=null){
            keySearch=keySearch.trim();
        }
        List<PhieuGiamGia> lstPhieu= phieuGiamGiaImp.findAll();
        Timestamp ngayHT = new Timestamp(System.currentTimeMillis());
        for (PhieuGiamGia phieu : lstPhieu) {
            if (phieu.getTrangthai() == 1 && (phieu.getNgayketthuc().getTime() < ngayHT.getTime() || phieu.getSoluong() == 0)) {
                phieu.setTrangthai(2);
                phieuGiamGiaImp.AddPhieuGiamGia(phieu);
            }
            if (phieu.getTrangthai() == 0 && phieu.getNgaybatdau().getTime() <= ngayHT.getTime()) {
                phieu.setTrangthai(1);
                phieuGiamGiaImp.AddPhieuGiamGia(phieu);
            }
        }
        Integer size= lstPhieu.size();

        Pageable pageable = PageRequest.of(p, size);

        Page<PhieuGiamGia> pagePGG = phieuGiamGiaImp.findAllOrderByNgayTaoDESC(keySearch,tungay,denngay,kieu,loai,tt,pageable);

        model.addAttribute("pagePGG",pagePGG);
        model.addAttribute("keySearch",keySearch);
        model.addAttribute("tungay",tungay);
        model.addAttribute("denngay",denngay);
        model.addAttribute("kieu",kieu);
        model.addAttribute("loai",loai);
        model.addAttribute("tt",tt);
        return "admin/qlphieugiamgia";
    }
    @GetMapping("/admin/xem-them-phieu-giam-gia")
    public String qlxemthemphieugiamgia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia, Model model,HttpSession session){
        model.addAttribute("lstPGG",phieuGiamGiaImp.findAll());
        session.setAttribute("lstKHViewThem", khachHangImp.findAll());

        return "admin/addphieugiamgia";
    }
    @PostMapping("/admin/them-phieu-giam-gia")
    public String AddPhieuGiamGia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
                                  @RequestParam("ngayBatDau") String ngayBatDau,
                                  @RequestParam("ngayKetThuc") String ngayKetThuc,
                                  @RequestParam("loaiphieu") Boolean loaiphieu,
                                  @RequestParam("kieuphieu") Boolean kieuphieu,
                                  @RequestParam("choncheckbox") String[] choncheckbox,
                                  HttpSession session){
        Integer checkthem=0;
        if(phieuGiamGia.getKieuphieu()){
            if(phieuGiamGia.getMacode()=="" || phieuGiamGia.getMacode().isEmpty()){
                int doDaiChuoi = 10;
                // Chuỗi chứa tất cả các ký tự có thể có trong chuỗi ngẫu nhiên
                String kiTu = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                // Tạo đối tượng Random
                Random random = new Random();
                // StringBuilder để xây dựng chuỗi ngẫu nhiên
                StringBuilder chuoiNgauNhien = new StringBuilder(doDaiChuoi);
                // Lặp để thêm ký tự ngẫu nhiên vào chuỗi
                for (int i = 0; i < doDaiChuoi; i++) {
                    // Lấy một ký tự ngẫu nhiên từ chuỗi kiTu và thêm vào chuỗi ngẫu nhiên
                    chuoiNgauNhien.append(kiTu.charAt(random.nextInt(kiTu.length())));
                    //tuan anh
                }
                phieuGiamGia.setMacode(chuoiNgauNhien.toString());
            }else{
                phieuGiamGia.setMacode(phieuGiamGia.getMacode().trim());
            }
            phieuGiamGia.setTenphieu(phieuGiamGia.getTenphieu().trim());
            phieuGiamGia.setLoaiphieu(loaiphieu);
            phieuGiamGia.setKieuphieu(kieuphieu);
            phieuGiamGia.setNguoitao("Tuan Anh");
            phieuGiamGia.setNguoicapnhat("Tuan Anh");
            Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
            Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
            phieuGiamGia.setNgaybatdau(ngayBatDauTimestamp);
            phieuGiamGia.setNgayketthuc(ngayKetThucTimestamp);

            Timestamp ngayHT= new Timestamp(System.currentTimeMillis());
            if(ngayBatDauTimestamp.getTime()> ngayHT.getTime()){
                phieuGiamGia.setTrangthai(0);
            }else{
                phieuGiamGia.setTrangthai(1);
            }

            phieuGiamGia.setNgaytao(new Timestamp(System.currentTimeMillis()));
            phieuGiamGia.setLancapnhatcuoi(new Timestamp(System.currentTimeMillis()));
            phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
            List<String> listString = Arrays.asList(choncheckbox);
            List<Integer> listInt= new ArrayList<>();
            for(String s : listString){
                Integer i= Integer.parseInt(s);
                listInt.add(i);
            }
            listInt.remove(Integer.valueOf(-1));
            List<String> lstEmail= new ArrayList<>();

            for(Integer chon :listInt){
                PhieuGiamGia phieu = phieuGiamGiaImp.findFirstByOrderByNgaytaoDesc();
                KhachHang kh= khachHangImp.getOne(chon);
                lstEmail.add(kh.getNguoidung().getEmail());
                KhachHangPhieuGiam khachHangPhieuGiam= new KhachHangPhieuGiam();
                khachHangPhieuGiam.setKhachhang(kh);
                khachHangPhieuGiam.setPhieugiamgia(phieu);

                khachHangPhieuGiamImp.AddKhachHangPhieuGiam(khachHangPhieuGiam);

            }
            String subject = "Mã giảm giá mới!";
            String body = "Chào bạn,\n\n"
                    + "Chúng tôi rất vui thông báo rằng bạn đã nhận được một mã giảm giá mới!\n\n"
                    + "Mã giảm giá của bạn là: " + phieuGiamGiaImp.findFirstByOrderByNgaytaoDesc().getMacode() + "\n\n"
                    + "Xin cảm ơn và chúc bạn có một ngày tốt lành!\n";
            for(String e :lstEmail){
                emailService.sendEmail(e,subject,body);
            }
            checkthem=1;
            session.setAttribute("themthanhcong",checkthem);
            return "redirect:/admin/hien-thi-phieu-giam-gia";

        }else{
            if(phieuGiamGia.getMacode()=="" || phieuGiamGia.getMacode().isEmpty()){
                int doDaiChuoi = 10;
                // Chuỗi chứa tất cả các ký tự có thể có trong chuỗi ngẫu nhiên
                String kiTu = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                // Tạo đối tượng Random
                Random random = new Random();
                // StringBuilder để xây dựng chuỗi ngẫu nhiên
                StringBuilder chuoiNgauNhien = new StringBuilder(doDaiChuoi);
                // Lặp để thêm ký tự ngẫu nhiên vào chuỗi
                for (int i = 0; i < doDaiChuoi; i++) {
                    // Lấy một ký tự ngẫu nhiên từ chuỗi kiTu và thêm vào chuỗi ngẫu nhiên
                    chuoiNgauNhien.append(kiTu.charAt(random.nextInt(kiTu.length())));
                }
                phieuGiamGia.setMacode(chuoiNgauNhien.toString());
            }else{
                phieuGiamGia.setMacode(phieuGiamGia.getMacode().trim());
            }
            phieuGiamGia.setTenphieu(phieuGiamGia.getTenphieu().trim());
            phieuGiamGia.setLoaiphieu(loaiphieu);
            phieuGiamGia.setKieuphieu(kieuphieu);
            phieuGiamGia.setNguoitao("Tuan Anh");
            phieuGiamGia.setNguoicapnhat("Tuan Anh");
            Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
            Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
            phieuGiamGia.setNgaybatdau(ngayBatDauTimestamp);
            phieuGiamGia.setNgayketthuc(ngayKetThucTimestamp);

            Timestamp ngayHT= new Timestamp(System.currentTimeMillis());
            if(ngayBatDauTimestamp.getTime()> ngayHT.getTime()){
                phieuGiamGia.setTrangthai(0);
            }else{
                phieuGiamGia.setTrangthai(1);
            }

            phieuGiamGia.setNgaytao(new Timestamp(System.currentTimeMillis()));
            phieuGiamGia.setLancapnhatcuoi(new Timestamp(System.currentTimeMillis()));
            phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);

//            List<KhachHang> lstKH= khachHangImp.findAll();
//            String subject = "Mã giảm giá mới!";
//            String body = "Chào bạn,\n\n"
//                    + "Chúng tôi rất vui thông báo rằng bạn đã nhận được một mã giảm giá mới!\n\n"
//                    + "Mã giảm giá của bạn là: " + phieuGiamGiaImp.findFirstByOrderByNgaytaoDesc().getMacode() + "\n\n"
//                    + "Xin cảm ơn và chúc bạn có một ngày tốt lành!\n";
//            for(KhachHang e :lstKH){
//                emailService.sendEmail(e.getNguoidung().getEmail(), subject,body);
//            }
            checkthem=1;
            session.setAttribute("themthanhcong",checkthem);
            return "redirect:/admin/hien-thi-phieu-giam-gia";
        }

    }
    @GetMapping("/admin/xem-cap-nhat-phieu-giam-gia/{Id}")
    public String ChiTietPhieuGiamGia(@PathVariable("Id") Integer Id, Model model, HttpSession session){
        PhieuGiamGia phieuGiamGia= phieuGiamGiaImp.findPhieuGiamGiaById(Id);
        List<KhachHangPhieuGiam> lstKHPG= new ArrayList<>();
        if(phieuGiamGia.getKieuphieu()==true){
            lstKHPG=khachHangPhieuGiamImp.findKhachHangPhieuGiamByIdPhieugiamgia(phieuGiamGia.getId());
        }
        model.addAttribute("phieuGiamGia",phieuGiamGia);
        session.setAttribute("lstKH", khachHangImp.findAll());
        session.setAttribute("lstKHPG", lstKHPG);
        session.setAttribute("phieuGG", phieuGiamGiaImp.findPhieuGiamGiaById(Id));
        return "admin/updatephieugiamgia";
    }

    @PostMapping("/admin/cap-nhat-phieu-giam-gia/{Id}")
    public String CapNhatPhieuGiamGia(@PathVariable("Id") Integer Id,
                                  @ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
                                  @RequestParam("ngayBatDau") String ngayBatDau,
                                  @RequestParam("ngayKetThuc") String ngayKetThuc,
                                      @RequestParam("choncheckbox") String[] choncheckbox,
                                      @RequestParam("loaiphieu") Boolean loaiphieu,
                                      @RequestParam("trangthaicn") Boolean trangthaicn,
                                      HttpSession session){
        Integer checkcapnhat=0;
        PhieuGiamGia phieu= phieuGiamGiaImp.findPhieuGiamGiaById(Id);
        phieuGiamGia.setId(Id);
        phieuGiamGia.setTenphieu(phieuGiamGia.getTenphieu().trim());
        phieuGiamGia.setNguoitao(phieu.getNguoitao());
        phieuGiamGia.setNgaytao(phieu.getNgaytao());
        phieuGiamGia.setMacode(phieu.getMacode());
        phieuGiamGia.setLoaiphieu(loaiphieu);
        phieuGiamGia.setKieuphieu(phieu.getKieuphieu());
        phieuGiamGia.setMacode(phieu.getMacode());
        Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
        Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
        phieuGiamGia.setNgaybatdau(ngayBatDauTimestamp);
        phieuGiamGia.setNgayketthuc(ngayKetThucTimestamp);
        Timestamp ngayHT= new Timestamp(System.currentTimeMillis());
        phieuGiamGia.setLancapnhatcuoi(new Timestamp(System.currentTimeMillis()));
        phieuGiamGia.setNguoicapnhat("Tuan Anh");
        if(trangthaicn==false){
            if (ngayBatDauTimestamp.getTime() > ngayHT.getTime()) {
                phieuGiamGia.setTrangthai(0);
            } else {
                phieuGiamGia.setTrangthai(1);
            }
        }
        if(trangthaicn==true){
            phieuGiamGia.setTrangthai(2);
        }
        if(choncheckbox !=null){
            phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
            List<String> listString = Arrays.asList(choncheckbox);
            List<Integer> listInt= new ArrayList<>();
            for(String s : listString){
                Integer i= Integer.parseInt(s);
                listInt.add(i);
            }
            listInt.remove(Integer.valueOf(-1));
            List<String> lstEmail= new ArrayList<>();
            for(Integer chon :listInt){
                KhachHang kh= khachHangImp.getOne(chon);
                lstEmail.add(kh.getNguoidung().getEmail());
                KhachHangPhieuGiam khachHangPhieuGiam= new KhachHangPhieuGiam();
                khachHangPhieuGiam.setKhachhang(kh);
                khachHangPhieuGiam.setPhieugiamgia(phieuGiamGia);

                khachHangPhieuGiamImp.AddKhachHangPhieuGiam(khachHangPhieuGiam);

            }
            String subject = "Mã giảm giá mới!";
            String body = "Chào bạn,\n\n"
                    + "Chúng tôi rất vui thông báo rằng bạn đã nhận được một mã giảm giá mới!\n\n"
                    + "Mã giảm giá của bạn là: " + phieuGiamGia.getMacode() + "\n\n"
                    + "Xin cảm ơn và chúc bạn có một ngày tốt lành!\n";
            for(String e :lstEmail){
                emailService.sendEmail(e,subject,body);
            }
            checkcapnhat=1;
            session.setAttribute("capnhatthanhcong",checkcapnhat);
            return "redirect:/admin/hien-thi-phieu-giam-gia";
        }else{
            phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
            checkcapnhat=1;
            session.setAttribute("capnhatthanhcong",checkcapnhat);
            return "redirect:/admin/hien-thi-phieu-giam-gia";
        }




    }
    @RequestMapping("/admin/cap-nhat-trang-thai-phieu-giam-gia/{Id}")
    public String CapNhatTrangThaiPhieuGiamGia(@PathVariable("Id") Integer Id){
        PhieuGiamGia phieuGiamGia= phieuGiamGiaImp.findPhieuGiamGiaById(Id);
        if(phieuGiamGia.getTrangthai()==0 ||phieuGiamGia.getTrangthai()==1){
            phieuGiamGia.setId(Id);
            phieuGiamGia.setTrangthai(2);
            phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
            return "redirect:/admin/hien-thi-phieu-giam-gia";
        }else{
            Timestamp ngayHT = new Timestamp(System.currentTimeMillis());
            if(phieuGiamGia.getNgaybatdau().getTime() <= ngayHT.getTime()){
                phieuGiamGia.setId(Id);
                phieuGiamGia.setTrangthai(1);
                phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
            }
            if(phieuGiamGia.getNgaybatdau().getTime()>ngayHT.getTime()){
                phieuGiamGia.setId(Id);
                phieuGiamGia.setTrangthai(0);
                phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
            }

            return "redirect:/admin/hien-thi-phieu-giam-gia";
        }

    }
}
