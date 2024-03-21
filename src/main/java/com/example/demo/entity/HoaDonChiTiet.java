package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hoadonchitiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    BigDecimal giasanpham;
    Integer soluong;
    String ghichu;
    Boolean trangthai;
    @ManyToOne
    @JoinColumn(name = "idsanphamchitiet")
    SanPhamChiTiet sanphamchitiet;
    @ManyToOne
    @JoinColumn(name = "idhoadon")
    HoaDon hoadon;

}
