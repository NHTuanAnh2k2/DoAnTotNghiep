package com.example.demo.controller.dotgiamgia;

import com.example.demo.entity.*;
import com.example.demo.service.impl.DotGiamGiaImp;
import com.example.demo.service.impl.SanPHamDotGiamImp;
import com.example.demo.service.impl.SanPhamChiTietImp;
import com.example.demo.service.impl.SanPhamImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class LaySanPhamController {
    @Autowired
    SanPhamImp sanPhamImp;
    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;
    @Autowired
    SanPHamDotGiamImp sanPHamDotGiamImp;
    @Autowired
    DotGiamGiaImp dotGiamGiaImp;

    @GetMapping("/hien-thi-san-pham")
    public List<SanPham> getProducts() {
        List<SanPham> lstSanPham = sanPhamImp.findAll();
        return lstSanPham;
    }
    @GetMapping("/hien-thi-san-pham-chi-tiet/{Id}")
    public List<SanPhamChiTiet> getProductDetails(@PathVariable int Id) {
        List<SanPhamChiTiet> details = new ArrayList<>();
        List<SanPhamChiTiet> lst= sanPhamChiTietImp.findBySanPhamId(Id);
        for(SanPhamChiTiet sp : lst){
            details.add(sp);
        }
        return details;
    }
    @GetMapping("/san-pham-dot-giam")
    public List<SanPham> sanphamdotgiam(HttpSession session){
        DotGiamGia dotGiamGia = (DotGiamGia) session.getAttribute("dotGG");
        List<SanPhamDotGiam> lstSPDG= sanPHamDotGiamImp.findSanPhamDotGiamByIdDotgiamgia(dotGiamGia.getId());
        List<Integer> lstIdSanPham= new ArrayList<>();
        for(SanPhamDotGiam s: lstSPDG){
            lstIdSanPham.add(s.getSanphamchitiet().getSanpham().getId());
        }
        Set<Integer> uniqueNumbers = new HashSet<>(lstIdSanPham);

        List<Integer> lstIdSanPhamKoLap = new ArrayList<>(uniqueNumbers);
        List<SanPham> lstSP= new ArrayList<>();
        for(Integer id: lstIdSanPhamKoLap){
            SanPham sp= sanPhamImp.findById(id);
            lstSP.add(sp);
        }
        return lstSP;
    }
    @GetMapping("/chi-tiet-san-pham-dot-giam")
    public List<SanPhamChiTiet> sanphamchitietdotgiam(HttpSession session){
        DotGiamGia dotGiamGia = (DotGiamGia) session.getAttribute("dotGG");
        List<SanPhamDotGiam> lstSPDG= sanPHamDotGiamImp.findSanPhamDotGiamByIdDotgiamgia(dotGiamGia.getId());

        List<SanPhamChiTiet> lstSPCT= new ArrayList<>();
        for(SanPhamDotGiam sp: lstSPDG){
            SanPhamChiTiet spct= sanPhamChiTietImp.findById(sp.getSanphamchitiet().getId());
            lstSPCT.add(spct);
        }
        return lstSPCT;
    }
    @GetMapping("/them-dot-thanh-cong")
    public Integer themthanhcong(HttpSession session){
        Integer dathem = (Integer) session.getAttribute("themdotthanhcong");
        return dathem;
    }
    @GetMapping("/cap-nhat-dot-thanh-cong")
    public Integer capnhatthanhcong(HttpSession session){
        Integer dacapnhat = (Integer) session.getAttribute("capnhatdotthanhcong");
        return dacapnhat;
    }
    @PostMapping("/dot-clear-session")
    public void clearSession(HttpSession session) {
//        session.invalidate();
        session.removeAttribute("themdotthanhcong");
        session.removeAttribute("capnhatdotthanhcong");
    }

    @GetMapping(value = "/dot-discounts-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<DotGiamGia>> streamDiscountsDot() {
        return Flux.interval(Duration.ofSeconds(10))
                .flatMap(sequence -> Flux.fromIterable(getUpdatedDiscountsDot())
                        .map(dotGiamGia -> ServerSentEvent.<DotGiamGia>builder()
                                .id(String.valueOf(sequence))
                                .event("dot-discount-update")
                                .data(dotGiamGia)
                                .build())
                );
    }

    private List<DotGiamGia> getUpdatedDiscountsDot() {
        // Đảm bảo phương thức này trả về một danh sách hợp lệ các đối tượng PhieuGiamGia
        List<DotGiamGia> lstDot = dotGiamGiaImp.findAll();
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
        return lstDot;
    }
}
