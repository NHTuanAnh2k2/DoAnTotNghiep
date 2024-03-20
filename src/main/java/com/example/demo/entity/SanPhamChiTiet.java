package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sanphamchitiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String qrcode;
    String mota;
    Boolean gioitinh;
    BigDecimal giatien;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
    @ManyToOne
    @JoinColumn(name = "idsanpham")
    SanPham sanpham;

    @ManyToOne
    @JoinColumn(name = "idkichco")
    KichCo kichco;

    @ManyToOne
    @JoinColumn(name = "idhang")
    HangGiay hanggiay;

    @ManyToOne
    @JoinColumn(name = "idmausac")
    MauSac mausac;

    @ManyToOne
    @JoinColumn(name = "idchatlieu")
    ChatLieu chatlieu;

    @ManyToOne
    @JoinColumn(name = "idloaigiay")
    LoaiGiay loaigiay;

    @ManyToOne
    @JoinColumn(name = "iddegiay")
    DeGiay degiay;
}
