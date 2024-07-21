package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "diachi")
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "TenDuong")
    String tenduong;
    @Column(name = "XaPhuong")
    String xaphuong;
    @Column(name = "QuanHuyen")
    String quanhuyen;
    @Column(name = "TinhThanhPho")
    String tinhthanhpho;
    @Column(name = "SdtNguoiNhan")
    String sdtnguoinhan;
    @Column(name = "HoTenNguoiNhan")
    String hotennguoinhan;
    @Column(name = "NgayTao")
    Timestamp ngaytao;
    @Column(name = "Nguoitao")
    String nguoitao;
    @Column(name = "LanCapNhatCuoi")
    Timestamp lancapnhatcuoi;
    @Column(name = "NguoiCapNhat")
    String nguoicapnhat;
    @Column(name = "TrangThai")
    Boolean trangthai;
    @ManyToOne
    @JoinColumn(name = "idnguoidung")
    NguoiDung nguoidung;
}
