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
    @Column(name = "HoaToc")
    Boolean hoatoc;
    @Column(name = "SDT")
    String sdt;
    @Column(name = "MaHoaDon")
    String mahoadon;
    @Column(name = "TenNguoiNhan")
    String tennguoinhan;
    @Column(name = "DiaChi")
    String diachi;
    @Column(name = "Email")
    String email;
    @Column(name = "TongTien")
    BigDecimal tongtien;
    @Column(name = "NgayXacNhan")
    Timestamp ngayxacnhan;
    @Column(name = "NgayVanChuyen")
    Timestamp ngayvanchuyen;
    @Column(name = "NgayNhanHang")
    Timestamp ngaynhanhang;
    @Column(name = "NgayHoanThanh")
    Timestamp ngayhoanthanh;
    @Column(name = "NgayGiaoDuKien")
    Timestamp ngaygiaodukien;
    @Column(name = "LoaiHoaDon")
    Boolean loaihoadon;
    @Column(name = "PhiVanChuyen")
    BigDecimal phivanchuyen;
    @Column(name = "NgayTao")
    Timestamp ngaytao;
    @Column(name = "NguoiTao")
    String nguoitao;
    @Column(name = "LanCapNhatCuoi")
    Timestamp lancapnhatcuoi;
    @Column(name = "NguoiCapNhat")
    String nguoicapnhat;
    @Column(name = "GhiChu")
    String ghichu;
    @Column(name = "TrangThai")
    Integer trangthai;
    @ManyToOne
    @JoinColumn(name = "idnhanvien")
    NhanVien nhanvien;
    @ManyToOne
    @JoinColumn(name = "idkhachhang")
    KhachHang khachhang;
}
