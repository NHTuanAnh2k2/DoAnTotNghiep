package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime ngaytao;
    String nguoitao;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idsanpham")
    SanPham sanpham;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idkichco")
    KichCo kichco;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idmausac")
    MauSac mausac;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idchatlieu")
    ChatLieu chatlieu;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idthuonghieu")
    ThuongHieu thuonghieu;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "iddegiay")
    DeGiay degiay;
    @JsonBackReference
    @OneToMany(mappedBy = "sanphamchitiet")
    List<Anh> anh;
}
