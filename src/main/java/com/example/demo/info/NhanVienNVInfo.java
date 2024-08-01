package com.example.demo.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NhanVienNVInfo {
    Integer id;
    String manhanvien;
    String hovaten;
    String sdt;
    Date ngaysinh;
    String tinhthanh;
    String quanhuyen;
    String xaphuong;
    String tenduong;
    Boolean vaitro;
    Boolean trangthai;
}
