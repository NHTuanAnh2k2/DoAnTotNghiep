package com.example.demo.info;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.SanPhamChiTiet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietInfo {
    Integer id;
    BigDecimal giasanpham;
    Integer soluong;
    String ghichu;
    Boolean trangthai;
    SPCTIF sanphamchitiet;
    HoaDon hoadon;
}
