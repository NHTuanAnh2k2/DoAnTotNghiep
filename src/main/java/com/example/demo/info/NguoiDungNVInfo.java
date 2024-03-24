package com.example.demo.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NguoiDungNVInfo {
    Integer id;
    String taikhoan;
    String email;
    String hovaten;
    Date ngaysinh;
    String cccd;
    String sodienthoai;
    Boolean gioitinh;
    String anh;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;

}
