package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Locale;

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
    String tennguoinhan;
    String diachi;
    String email;
    BigDecimal tongtien;
    Timestamp ngayxacnhan;
    Timestamp ngayvanchuyen;
    Timestamp ngaynhanhang;
    Timestamp ngayhoanthanh;
    Boolean loaihoadon;
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
