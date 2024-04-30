package com.example.demo.controller.dotgiamgia;

import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.impl.DotGiamGiaImp;
import com.example.demo.service.impl.SanPhamChiTietImp;
import com.example.demo.service.impl.SanPhamImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DotGiamGiaController {

    @Autowired
    DotGiamGiaImp dotGiamGiaImp;
    @Autowired
    SanPhamImp sanPhamImp;
    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;

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
        model.addAttribute("lstSP",sanPhamImp.findAll());
        model.addAttribute("lstCTSP",sanPhamChiTietImp.findAll());
        return "admin/adddotgiamgia";
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
}
