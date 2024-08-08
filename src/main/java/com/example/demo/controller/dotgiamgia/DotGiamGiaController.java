package com.example.demo.controller.dotgiamgia;

import com.example.demo.entity.*;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.impl.DotGiamGiaImp;
import com.example.demo.service.impl.SanPHamDotGiamImp;
import com.example.demo.service.impl.SanPhamChiTietImp;
import com.example.demo.service.impl.SanPhamImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@Controller
public class DotGiamGiaController {

    @Autowired
    DotGiamGiaImp dotGiamGiaImp;
    @Autowired
    SanPhamImp sanPhamImp;
    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;
    @Autowired
    SanPHamDotGiamImp sanPHamDotGiamImp;
    @Autowired
    NguoiDungRepository daoNguoiDung;
    @Autowired
    NhanVienRepository nhanvienRPo;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;


    @RequestMapping("/admin/hien-thi-dot-giam-gia")
    public String qlphieugiamgia(Model model,
                                 @RequestParam(defaultValue = "0") Integer p,
                                 @RequestParam(value = "keySearch",required = false) String keySearch,
                                 @RequestParam(value = "tungaySearch",required = false) String tungaySearch,
                                 @RequestParam(value = "denngaySearch",required = false) String denngaySearch,
                                 @RequestParam(value = "ttSearch",required = false) String ttSearch) {
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
        Integer tt;
        if((ttSearch != null && ttSearch.equals("0")) || (ttSearch != null && ttSearch.equals("1")) || (ttSearch != null && ttSearch.equals("2"))){
            tt= Integer.parseInt(ttSearch);
        }else{
            tt=null;
        }
        List<DotGiamGia> lstDot= dotGiamGiaImp.findAll();
        Timestamp ngayHT = new Timestamp(System.currentTimeMillis());
        for (DotGiamGia dot : lstDot) {
            if (dot.getTrangthai() == 1 && dot.getNgayketthuc().getTime() < ngayHT.getTime()) {
                dot.setTrangthai(2);
                dotGiamGiaImp.AddDotGiamGia(dot);
            }
            if (dot.getTrangthai() == 0 && dot.getNgaybatdau().getTime() <= ngayHT.getTime()) {
                dot.setTrangthai(1);
                dotGiamGiaImp.AddDotGiamGia(dot);
            }
        }
        if(keySearch!=null){
            keySearch=keySearch.trim();
        }
        Integer size= lstDot.size();
        Pageable pageable = PageRequest.of(p, size);
        Page<DotGiamGia> pageDGG = dotGiamGiaImp.findAllOrderByNgayTaoDESC(keySearch,tungay,denngay,tt,pageable);
        model.addAttribute("pageDGG",pageDGG);
        model.addAttribute("keySearch",keySearch);
        model.addAttribute("tungay",tungay);
        model.addAttribute("denngay",denngay);
        model.addAttribute("tt",tt);
        return "admin/qldotgiamgia";
    }
    @RequestMapping("/admin/cap-nhat-trang-thai-dot-giam-gia/{Id}")
    public String CapNhatTrangThaiDotGiamGia(@PathVariable("Id") Integer Id){
        DotGiamGia dotGiamGia= dotGiamGiaImp.findDotGiamGiaById(Id);
        if(dotGiamGia.getTrangthai()==0 ||dotGiamGia.getTrangthai()==1){
            dotGiamGia.setId(Id);
            dotGiamGia.setTrangthai(2);
            dotGiamGiaImp.AddDotGiamGia(dotGiamGia);
            return "redirect:/admin/hien-thi-dot-giam-gia";
        }else {
            Timestamp ngayHT = new Timestamp(System.currentTimeMillis());
            if (dotGiamGia.getNgaybatdau().getTime() <= ngayHT.getTime()) {
                dotGiamGia.setId(Id);
                dotGiamGia.setTrangthai(1);
                dotGiamGiaImp.AddDotGiamGia(dotGiamGia);
            }
            if (dotGiamGia.getNgaybatdau().getTime() > ngayHT.getTime()) {
                dotGiamGia.setId(Id);
                dotGiamGia.setTrangthai(0);
                dotGiamGiaImp.AddDotGiamGia(dotGiamGia);
            }
            return "redirect:/admin/hien-thi-dot-giam-gia";
        }

    }
    @GetMapping("/admin/xem-them-dot-giam-gia")
    public String qlxemthemphieugiamgia(@ModelAttribute("dotGiamGia") DotGiamGia dotGiamGia, Model model){
//        model.addAttribute("lstSP",sanPhamImp.findAll());
//        model.addAttribute("lstCTSP",sanPhamChiTietImp.findAll());
        return "admin/adddotgiamgia";
    }
    @PostMapping("/admin/them-dot-giam-gia")
    public String AddDotGiamGia(@ModelAttribute("dotGiamGia") DotGiamGia dotGiamGia,
                                  @RequestParam("ngayBatDau") String ngayBatDau,
                                  @RequestParam("ngayKetThuc") String ngayKetThuc,
                                  @RequestParam("choncheckbox") String[] choncheckbox,
                                HttpSession session){
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);
            Integer checkthem=1;
            dotGiamGia.setTendot(dotGiamGia.getTendot().trim());
            dotGiamGia.setNguoitao(nv.getNguoidung().getHovaten());
            Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
            Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
            dotGiamGia.setNgaybatdau(ngayBatDauTimestamp);
            dotGiamGia.setNgayketthuc(ngayKetThucTimestamp);

            Timestamp ngayHT= new Timestamp(System.currentTimeMillis());
            dotGiamGia.setLancapnhatcuoi(new Timestamp(System.currentTimeMillis()));
            dotGiamGia.setNguoicapnhat(nv.getNguoidung().getHovaten());
            if(ngayBatDauTimestamp.getTime()> ngayHT.getTime()){
                dotGiamGia.setTrangthai(0);
            }else{
                dotGiamGia.setTrangthai(1);
            }

            dotGiamGia.setNgaytao(new Timestamp(System.currentTimeMillis()));
            dotGiamGiaImp.AddDotGiamGia(dotGiamGia);
            checkthem=1;
            List<String> listString = Arrays.asList(choncheckbox);
            List<Integer> listInt= new ArrayList<>();
            for(String s : listString){
                System.out.println(s);
                Integer i= Integer.parseInt(s);
                listInt.add(i);
            }
            listInt.remove(Integer.valueOf(-1));
            List<SanPhamDotGiam> lstDotSP= sanPHamDotGiamImp.findAll();
        DotGiamGia dot = dotGiamGiaImp.findFirstByOrderByNgaytaoDesc();
        for (Integer chon : listInt) {
            Optional<SanPhamChiTiet> spctOpt = sanPhamChiTietRepository.findById(chon);
            if (spctOpt.isPresent()) {
                SanPhamChiTiet spct = spctOpt.get();
                boolean isUpdated = false;

                for (SanPhamDotGiam d : lstDotSP) {
                    if (spct.getId().equals(d.getSanphamchitiet().getId())) {
                        if (dot.getNgaybatdau().getTime() <= d.getDotgiamgia().getNgayketthuc().getTime()) {
                            sanPHamDotGiamImp.delete(d);
                        }
                        addSanPhamDotGiam(spct, dot);
                        isUpdated = true;
                        break;
                    }
                }

                if (!isUpdated) {
                    addSanPhamDotGiam(spct, dot);
                }
            }
        }

        session.setAttribute("themdotthanhcong",checkthem);
        return "redirect:/admin/hien-thi-dot-giam-gia";

    }
    @GetMapping("/admin/xem-cap-nhat-dot-giam-gia/{Id}")
    public String qlxemcapnhatdotgiamgia(@PathVariable("Id") Integer Id, Model model, HttpSession session){
        model.addAttribute("dotGiamGia",dotGiamGiaImp.findDotGiamGiaById(Id));
        session.setAttribute("dotGG", dotGiamGiaImp.findDotGiamGiaById(Id));
        return "admin/updatedotgiamgia";
    }
    @PostMapping("/admin/cap-nhat-dot-giam-gia/{Id}")
    public String qlcapnhatdotgiamgia(@PathVariable("Id") Integer Id,
                                      @ModelAttribute("dotGiamGia") DotGiamGia dotGiamGia,
                                      @RequestParam("ngayBatDau") String ngayBatDau,
                                      @RequestParam("ngayKetThuc") String ngayKetThuc,
                                      @RequestParam("choncheckbox") String[] choncheckbox,
                                      @RequestParam("trangthaicn") Boolean trangthaicn,
                                      HttpSession session){
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);
        Integer checkcapnhat=1;
        DotGiamGia dot= dotGiamGiaImp.findDotGiamGiaById(Id);
        dotGiamGia.setId(Id);

        dotGiamGia.setTendot(dotGiamGia.getTendot().trim());
        Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
        Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
        dotGiamGia.setNgaybatdau(ngayBatDauTimestamp);
        dotGiamGia.setNgayketthuc(ngayKetThucTimestamp);
        Timestamp ngayHT= new Timestamp(System.currentTimeMillis());
        dotGiamGia.setLancapnhatcuoi(new Timestamp(System.currentTimeMillis()));
        dotGiamGia.setNguoicapnhat(nv.getNguoidung().getHovaten());
        dotGiamGia.setNguoitao(dot.getNguoitao());
        dotGiamGia.setNgaytao(dot.getNgaytao());
        if(trangthaicn==false){
            if (ngayBatDauTimestamp.getTime() > ngayHT.getTime()) {
                dotGiamGia.setTrangthai(0);
            } else {
                dotGiamGia.setTrangthai(1);
            }
        }
        if(trangthaicn==true){
            dotGiamGia.setTrangthai(2);
        }


        dotGiamGiaImp.AddDotGiamGia(dotGiamGia);
        checkcapnhat=1;
        List<String> listString = Arrays.asList(choncheckbox);
        List<Integer> listInt= new ArrayList<>();
        for(String s : listString){
            System.out.println(s);
            Integer i= Integer.parseInt(s);
            listInt.add(i);
        }
        listInt.remove(Integer.valueOf(-1));

        List<SanPhamDotGiam> lstSPDG= sanPHamDotGiamImp.findSanPhamDotGiamByIdDotgiamgia(dotGiamGia.getId());
        for(SanPhamDotGiam sp :lstSPDG){
            sanPHamDotGiamImp.delete(sp);
        }
        List<SanPhamDotGiam> lstDotSP= sanPHamDotGiamImp.findAll();
        for (Integer chon : listInt) {
            Optional<SanPhamChiTiet> spctOpt = sanPhamChiTietRepository.findById(chon);
            if (spctOpt.isPresent()) {
                SanPhamChiTiet spct = spctOpt.get();
                boolean isUpdated = false;

                for (SanPhamDotGiam d : lstDotSP) {
                    if (spct.getId().equals(d.getSanphamchitiet().getId())) {
                        if (dotGiamGia.getNgaybatdau().getTime() <= d.getDotgiamgia().getNgayketthuc().getTime()) {
                            sanPHamDotGiamImp.delete(d);
                        }
                        addSanPhamDotGiam(spct, dotGiamGia);
                        isUpdated = true;
                        break;
                    }
                }

                if (!isUpdated) {
                    addSanPhamDotGiam(spct, dotGiamGia);
                }
            }
        }

//        for(Integer chon :listInt){
//            SanPhamChiTiet spct= sanPhamChiTietImp.findById(chon);
//            SanPhamDotGiam sanPhamDotGiam= new SanPhamDotGiam();
//            sanPhamDotGiam.setSanphamchitiet(spct);
//            sanPhamDotGiam.setDotgiamgia(dotGiamGia);
//
//            sanPHamDotGiamImp.AddSanPhamDotGiam(sanPhamDotGiam);
//
//        }
        session.setAttribute("capnhatdotthanhcong",checkcapnhat);
        return "redirect:/admin/hien-thi-dot-giam-gia";
    }
    private void addSanPhamDotGiam(SanPhamChiTiet spct, DotGiamGia dot) {
        SanPhamDotGiam sanPhamDotGiam = new SanPhamDotGiam();
        sanPhamDotGiam.setSanphamchitiet(spct);
        sanPhamDotGiam.setDotgiamgia(dot);
        sanPHamDotGiamImp.AddSanPhamDotGiam(sanPhamDotGiam);
    }
}
