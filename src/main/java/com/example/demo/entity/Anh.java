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
@Table(name = "anh")
public class Anh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String tenanh;
    Date ngaytao;
    String nguoitao;
    Date lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
    @ManyToOne
    @JoinColumn(name = "idsanphamchitiet")
    SanPhamChiTiet sanphamchitiet;

}
