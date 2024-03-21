package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "khachhangphieugiam")
public class KhachHangPhieuGiam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
    @ManyToOne
    @JoinColumn(name = "idkhachhang")
    KhachHang khachhang;
    @ManyToOne
    @JoinColumn(name = "idphieugiam")
    PhieuGiamGia phieugiamgia;

}
