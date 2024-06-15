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
@Table(name = "dotgiamgia")
public class DotGiamGia{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String tendot;
    Integer giatrigiam;// phần trăm giảm
    Timestamp ngaybatdau;
    Timestamp ngayketthuc;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Integer trangthai;
}
