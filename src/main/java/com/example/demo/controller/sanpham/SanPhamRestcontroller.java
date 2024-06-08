package com.example.demo.controller.sanpham;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.List;

@RestController
public class SanPhamRestcontroller {
    @Autowired
     SanPhamRepositoty sanPhamRepositoty;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping(value = "/products-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<SanPham>> streamProducts() {
        return Flux.interval(Duration.ofSeconds(30))
                .flatMap(sequence -> Flux.fromIterable(getUpdatedProducts())
                        .map(sanPham -> ServerSentEvent.<SanPham>builder()
                                .id(String.valueOf(sequence))
                                .event("product-update")
                                .data(sanPham)
                                .build())
                );
    }

    public List<SanPham> getUpdatedProducts() {
        List<SanPham> lstSP = sanPhamRepositoty.findAll();
        for (SanPham sp : lstSP) {
            for (SanPhamChiTiet spct : sp.getSpct()) {
                if (spct.getSoluong() == 0 && sp.getTrangthai()) {
                    sp.setTrangthai(false);
                    sanPhamRepositoty.save(sp);
                }
            }
        }
        return lstSP;
    }
}
