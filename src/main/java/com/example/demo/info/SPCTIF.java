package com.example.demo.info;

import com.example.demo.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SPCTIF {
    Integer id;
    String qrcode;
    String masanphamchitiet;
    String mota;
    Boolean gioitinh;
    Integer soluong;
    BigDecimal giatien;
    LocalDateTime ngaytao;
    String nguoitao;
    LocalDateTime lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
    SanPham sanpham;
    KichCo kichco;
    MauSac mausac;
    ChatLieu chatlieu;
    ThuongHieu thuonghieu;
    DeGiay degiay;
    List<Anh> anh;
}
