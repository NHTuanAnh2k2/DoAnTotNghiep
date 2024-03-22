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
@Table(name = "hoadon")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String maqr;
    String sdt;
    String mahoadon;
    String diachi;
    String email;
    BigDecimal tongtien;
    Timestamp ngayxacnhan;
    Timestamp ngayvanchuyen;
    Integer loaihoadon;
    BigDecimal phivanchuyen;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    String ghichu;
    Integer trangthai;
    @ManyToOne
    @JoinColumn(name = "idnhanvien")
    NhanVien nhanvien;
    @ManyToOne
    @JoinColumn(name = "idkhachhang")
    KhachHang khachhang;
}
