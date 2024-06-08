package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phieugiamgia")
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String macode;
    String tenphieu;
    BigDecimal giatrigiamtoida;//tiền giảm tối đa
    Integer giatrigiam;// phần trăm giảm_hoặc tiền mặt
    BigDecimal dontoithieu;
    Integer soluong;
    Boolean loaiphieu;
    Boolean kieuphieu;
    Timestamp ngaybatdau;
    Timestamp ngayketthuc;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Integer trangthai;
}
