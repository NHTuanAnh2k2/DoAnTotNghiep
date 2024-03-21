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
@Table(name = "phuongthucthanhtoan")
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String tenphuongthuc;
    String mota;
    BigDecimal tongtien;
    String magiaodichvnpay;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
    @ManyToOne
    @JoinColumn(name = "idhoadon")
    HoaDon hoadon;
}
