package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sanpham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String qrcode;
    String tensanpham;
    Date ngaytao;
    String nguoitao;
    Date lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
}
