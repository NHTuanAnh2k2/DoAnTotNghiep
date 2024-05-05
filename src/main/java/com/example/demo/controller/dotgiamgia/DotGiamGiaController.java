package com.example.demo.controller.dotgiamgia;

import com.example.demo.entity.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
//        Timestamp ngayHT= new Timestamp(System.currentTimeMillis());
//        for(PhieuGiamGia phieu :lstPhieu){
//            if(phieu.getSoluong()==0){
//                phieu.setTrangthai(2);
//                phieu.setId(phieu.getId());
//                phieuGiamGiaImp.AddPhieuGiamGia(phieu);
//            }
//            if(phieu.getTrangthai()==0 && phieu.getNgaybatdau().getTime()<=ngayHT.getTime()){
//                phieu.setTrangthai(1);
//                phieu.setId(phieu.getId());
//                phieuGiamGiaImp.AddPhieuGiamGia(phieu);
//            }
//            if(phieu.getTrangthai()==1 && phieu.getNgayketthuc().getTime()<ngayHT.getTime()){
//                phieu.setTrangthai(2);
//                phieu.setId(phieu.getId());
//                phieuGiamGiaImp.AddPhieuGiamGia(phieu);
//            }
//        }
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
        dotGiamGia.setId(Id);
        dotGiamGia.setTrangthai(2);
        dotGiamGiaImp.AddDotGiamGia(dotGiamGia);
        return "redirect:/admin/hien-thi-dot-giam-gia";
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
                                  @RequestParam("choncheckbox") String[] choncheckbox){

            dotGiamGia.setTendot(dotGiamGia.getTendot().trim());
            dotGiamGia.setNguoitao("Tuan Anh");
            Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
            Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
            dotGiamGia.setNgaybatdau(ngayBatDauTimestamp);
            dotGiamGia.setNgayketthuc(ngayKetThucTimestamp);

            Timestamp ngayHT= new Timestamp(System.currentTimeMillis());
            dotGiamGia.setLancapnhatcuoi(new Timestamp(System.currentTimeMillis()));
            dotGiamGia.setNguoicapnhat("Tuan Anh");
            if(ngayBatDauTimestamp.getTime()> ngayHT.getTime()){
                dotGiamGia.setTrangthai(0);
            }else{
                dotGiamGia.setTrangthai(1);
            }

            dotGiamGia.setNgaytao(new Timestamp(System.currentTimeMillis()));
            dotGiamGiaImp.AddDotGiamGia(dotGiamGia);
            List<String> listString = Arrays.asList(choncheckbox);
            List<Integer> listInt= new ArrayList<>();
            for(String s : listString){
                System.out.println(s);
                Integer i= Integer.parseInt(s);
                listInt.add(i);
            }
            listInt.remove(Integer.valueOf(-1));

            for(Integer chon :listInt){
                DotGiamGia dot = dotGiamGiaImp.findFirstByOrderByNgaytaoDesc();
                SanPhamChiTiet spct= sanPhamChiTietImp.findById(chon);
                SanPhamDotGiam sanPhamDotGiam= new SanPhamDotGiam();
                sanPhamDotGiam.setSanphamchitiet(spct);
                sanPhamDotGiam.setDotgiamgia(dot);

                sanPHamDotGiamImp.AddSanPhamDotGiam(sanPhamDotGiam);

            }

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
                                      @RequestParam("choncheckbox") String[] choncheckbox){
        DotGiamGia dot= dotGiamGiaImp.findDotGiamGiaById(Id);
        dotGiamGia.setId(Id);

        dotGiamGia.setTendot(dotGiamGia.getTendot().trim());
        Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
        Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
        dotGiamGia.setNgaybatdau(ngayBatDauTimestamp);
        dotGiamGia.setNgayketthuc(ngayKetThucTimestamp);
        Timestamp ngayHT= new Timestamp(System.currentTimeMillis());
        dotGiamGia.setLancapnhatcuoi(new Timestamp(System.currentTimeMillis()));
        dotGiamGia.setNguoicapnhat("Tuan Anh");
        dotGiamGia.setNguoitao(dot.getNguoitao());
        dotGiamGia.setNgaytao(dot.getNgaytao());
        if (dot.getTrangthai() == 2) {
            dotGiamGia.setTrangthai(2);
        } else {
            if (ngayBatDauTimestamp.getTime() > ngayHT.getTime()) {
                dotGiamGia.setTrangthai(0);
            } else {
                dotGiamGia.setTrangthai(1);
            }
        }


        dotGiamGiaImp.AddDotGiamGia(dotGiamGia);
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

        for(Integer chon :listInt){
            SanPhamChiTiet spct= sanPhamChiTietImp.findById(chon);
            SanPhamDotGiam sanPhamDotGiam= new SanPhamDotGiam();
            sanPhamDotGiam.setSanphamchitiet(spct);
            sanPhamDotGiam.setDotgiamgia(dotGiamGia);

            sanPHamDotGiamImp.AddSanPhamDotGiam(sanPhamDotGiam);

        }

        return "redirect:/admin/hien-thi-dot-giam-gia";
    }
//    @GetMapping("/products")
//    public String getAllProducts(Model model) {
//        List<Product> products = productService.getAllProducts();
//        model.addAttribute("products", products);
//        return "products"; // Trả về template HTML chứa danh sách sản phẩm và chi tiết sản phẩm
//    }
//
//    @GetMapping("/productDetails/{productId}")
//    public String getProductDetails(@PathVariable Long productId, Model model) {
//        List<ProductDetail> productDetails = productService.getProductDetails(productId);
//        model.addAttribute("productDetails", productDetails);
//        return "products"; // Trả về template HTML chứa danh sách sản phẩm và chi tiết sản phẩm
//    }

//    @GetMapping("/xem-chi-tiet-san-pham")
//
//    public String getProductDetails(@RequestParam("productIds") List<String> productIds,Model model) {
//        List<SanPhamChiTiet> allProductDetails = new ArrayList<>();
//        for (String productId : productIds) {
//            Integer id= Integer.parseInt(productId);
//            List<SanPhamChiTiet> productDetails = sanPhamChiTietImp.findBySanPhamId(id);
//            allProductDetails.addAll(productDetails);
//        }
//        model.addAttribute("lstSPCT",allProductDetails);
//        return "forward://admin/hien-thi-dot-giam-gia";// Trả về danh sách chi tiết sản phẩm của tất cả các sản phẩm
//    }

}
