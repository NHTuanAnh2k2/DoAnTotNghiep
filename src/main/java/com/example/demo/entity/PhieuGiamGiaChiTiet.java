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
    BigDecimal giabandau;
    BigDecimal giasauapdung;
    BigDecimal tiengiam;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    @ManyToOne
    @JoinColumn(name = "idhoadon")
    HoaDon hoadon;
    @ManyToOne
    @JoinColumn(name = "idmagiamgia")
    PhieuGiamGia phieugiamgia;

}
