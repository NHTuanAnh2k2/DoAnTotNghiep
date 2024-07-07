package com.example.demo.controller.sanpham;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepositoty;
import com.example.demo.service.impl.SanPhamImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SanPhamRestcontroller {
    @Autowired
    SanPhamImp sanPhamImp;

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

    @PostMapping("/updateProductStatus/{id}")
    public ResponseEntity<Map<String, Object>> updateProductStatus(@PathVariable("id") Integer id, @RequestParam("status") boolean trangthai) {
        Map<String, Object> response = new HashMap<>();
        try {
            sanPhamImp.updateProductStatus(id, trangthai);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
