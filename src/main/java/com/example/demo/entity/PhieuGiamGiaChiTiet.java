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
@Table(name = "phieugiamgiachitiet")
public class PhieuGiamGiaChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "GiaBanDau")
    BigDecimal giabandau;
    @Column(name = "GiaSauApDung")
    BigDecimal giasauapdung;
    @Column(name = "TienGiam")
    BigDecimal tiengiam;
    @Column(name = "NgayTao")
    Timestamp ngaytao;
    @Column(name = "NguoiTao")
    String nguoitao;
    @Column(name = "LanCapNhatCuoi")
    Timestamp lancapnhatcuoi;
    @Column(name = "NguoiCapNhat")
    String nguoicapnhat;
    @ManyToOne
    @JoinColumn(name = "idhoadon")
    HoaDon hoadon;
    @ManyToOne
    @JoinColumn(name = "idmagiamgia")
    PhieuGiamGia phieugiamgia;

}
