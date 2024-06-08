package com.example.demo.controller.customer;

import com.example.demo.entity.Anh;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sanpham")
public class SanPhamApiController  {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getSanPhamDetails(@PathVariable Integer id, @RequestParam String color) {
        Optional<SanPhamChiTiet> optionalSpct = sanPhamChiTietRepository.findBySanPhamIdAndColor(id, color);
        if (!optionalSpct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sản phẩm chi tiết không tìm thấy");
        }
        SanPhamChiTiet spct = optionalSpct.get();
        Map<String, Object> response = new HashMap<>();
        response.put("gia", spct.getGiatien());
        response.put("anh", spct.getAnh().stream().map(Anh::getTenanh).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }
}
