package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

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
    Integer soluong;
    BigDecimal giatien;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date ngaytao;
    String nguoitao;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date lancapnhatcuoi;
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
