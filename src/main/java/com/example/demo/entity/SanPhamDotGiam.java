package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sanphamdotgiam")
public class SanPhamDotGiam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne
    @JoinColumn(name = "idchitietsanpham")
    SanPhamChiTiet sanphamchitiet;
    @ManyToOne
    @JoinColumn(name = "iddotgiam")
    DotGiamGia dotgiamgia;
}
