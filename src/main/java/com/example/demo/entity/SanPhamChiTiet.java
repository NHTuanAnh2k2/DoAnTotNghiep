package com.example.demo.entity;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import com.fasterxml.jackson.annotation.JsonIdentityInfo;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
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
    String masanphamchitiet;
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


    @ManyToOne
    @JoinColumn(name = "idsanpham")
    SanPham sanpham;

    @ManyToOne
    @JoinColumn(name = "idkichco")
    KichCo kichco;

    @ManyToOne
    @JoinColumn(name = "idmausac")
    MauSac mausac;

    @ManyToOne
    @JoinColumn(name = "idchatlieu")
    ChatLieu chatlieu;

    @ManyToOne
    @JoinColumn(name = "idthuonghieu")
    ThuongHieu thuonghieu;

    @ManyToOne
    @JoinColumn(name = "iddegiay")
    DeGiay degiay;

    @OneToMany(mappedBy = "sanphamchitiet")
    List<Anh> anh;
}
