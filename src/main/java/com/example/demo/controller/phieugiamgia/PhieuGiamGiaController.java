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

    @GetMapping("/admin/hien-thi-phieu-giam-gia")
    public String qlphieugiamgia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia, Model model,@RequestParam(defaultValue = "0") Integer p) {
        Pageable pageable = PageRequest.of(p, 15);
        Page<PhieuGiamGia> pagePGG = phieuGiamGiaImp.findAll(pageable);
        model.addAttribute("pagePGG",pagePGG);
        return "admin/qlphieugiamgia";
    }
    @GetMapping("/admin/xem-them-phieu-giam-gia")
    public String qlxemthemphieugiamgia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia, Model model){
        model.addAttribute("lstKH", khachHangImp.findAll());
        return "admin/addphieugiamgia";
    }
    @PostMapping("/admin/them-phieu-giam-gia")
    public String AddPhieuGiamGia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
                                  @RequestParam("ngayBatDau") String ngayBatDau,
                                  @RequestParam("ngayKetThuc") String ngayKetThuc,
                                  @RequestParam("loaiphieu") boolean loaiphieu,
                                  @RequestParam("choncheckbox") String[] choncheckbox){
        if(phieuGiamGia.isKieuphieu()){

            phieuGiamGia.setLoaiphieu(loaiphieu);
            phieuGiamGia.setNguoitao("Tuan Anh");
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

            return "redirect:/admin/hien-thi-phieu-giam-gia";

        }else{
            phieuGiamGia.setLoaiphieu(loaiphieu);
            phieuGiamGia.setNguoitao("Tuan Anh");
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
            phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
            return "redirect:/admin/hien-thi-phieu-giam-gia";
        }

    }
    @GetMapping("/admin/chi-tiet-phieu-giam-gia/{Id}")
    public String ChiTietPhieuGiamGia(@PathVariable("Id") Integer Id, Model model){
        Optional<PhieuGiamGia> phieuGiamGia= phieuGiamGiaImp.findPhieuGiamGiaById(Id);
        model.addAttribute("phieuGiamGiaCT",phieuGiamGia);
        return "redirect:/admin/hien-thi-phieu-giam-gia";
    }

    @PostMapping("/admin/cap-nhat-phieu-giam-gia/{Id}")
    public String CapNhatPhieuGiamGia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
                                  @RequestParam("ngayBatDau") String ngayBatDau,
                                  @RequestParam("ngayKetThuc") String ngayKetThuc){

        phieuGiamGia.setNguoitao("Tuan Anh");
        Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
        Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
        phieuGiamGia.setNgaybatdau(ngayBatDauTimestamp);
        phieuGiamGia.setNgayketthuc(ngayKetThucTimestamp);

        phieuGiamGia.setNgaytao(new Timestamp(System.currentTimeMillis()));
        phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
        System.out.println(phieuGiamGia.isLoaiphieu());
        return "redirect:/admin/hien-thi-phieu-giam-gia";
    }
}
