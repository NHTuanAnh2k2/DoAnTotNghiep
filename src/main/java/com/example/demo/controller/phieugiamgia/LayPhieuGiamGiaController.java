package com.example.demo.controller.phieugiamgia;

import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.KhachHangPhieuGiam;
import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.info.PhieuGiamGiaInfo;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LayPhieuGiamGiaController {
    @Autowired
    private PhieuGiamGiaImp phieuGiamGiaImp;
    @GetMapping("/lay-phieu-giam-gia")
    public PhieuGiamGia layphieu(HttpSession session){
        PhieuGiamGia phieuGiamGia = (PhieuGiamGia) session.getAttribute("phieuGG");
        return phieuGiamGia;
    }
    @GetMapping("/lay-khach-hang")
    public List<KhachHang> layKhachHang(HttpSession session){
        List<KhachHang> lstKH = (List<KhachHang>) session.getAttribute("lstKH");
        return lstKH;
    }
    @GetMapping("/khach-hang-xem-them")
    public List<KhachHang> khachhangxemthem(HttpSession session){
        List<KhachHang> lstKH = (List<KhachHang>) session.getAttribute("lstKHViewThem");
        return lstKH;
    }
    @GetMapping("/lay-khach-hang-phieu-giam")
    public List<KhachHangPhieuGiam> layKhachHangPhieuGiam(HttpSession session){
        List<KhachHangPhieuGiam> lstKHPG = (List<KhachHangPhieuGiam>) session.getAttribute("lstKHPG");
        return lstKHPG;
    }
@GetMapping(value = "/discounts-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<ServerSentEvent<PhieuGiamGia>> streamDiscounts() {
    return Flux.interval(Duration.ofSeconds(10))
            .flatMap(sequence -> Flux.fromIterable(getUpdatedDiscounts())
                    .map(phieu -> ServerSentEvent.<PhieuGiamGia>builder()
                            .id(String.valueOf(sequence))
                            .event("discount-update")
                            .data(phieu)
                            .build())
            );
}

    private List<PhieuGiamGia> getUpdatedDiscounts() {
        // Đảm bảo phương thức này trả về một danh sách hợp lệ các đối tượng PhieuGiamGia
        List<PhieuGiamGia> lstPhieu = phieuGiamGiaImp.findAll();
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
        return lstPhieu;
    }
    @GetMapping("/them-thanh-cong")
    public Integer themthanhcong(HttpSession session){
        Integer dathem = (Integer) session.getAttribute("themthanhcong");
        return dathem;
    }
    @GetMapping("/cap-nhat-thanh-cong")
    public Integer capnhatthanhcong(HttpSession session){
        Integer dacapnhat = (Integer) session.getAttribute("capnhatthanhcong");
        return dacapnhat;
    }
    @PostMapping("/clear-session")
    public void clearSession(HttpSession session) {
//        session.invalidate();
        session.removeAttribute("themthanhcong");
        session.removeAttribute("capnhatthanhcong");
    }
    @GetMapping("/lay-phieu-giam-theo-id/{Id}")
    public PhieuGiamGia layPhieuGiamTheoId(@PathVariable("Id") int Id){
        PhieuGiamGia p= phieuGiamGiaImp.findPhieuGiamGiaById(Id);
        return p;
    }

}
