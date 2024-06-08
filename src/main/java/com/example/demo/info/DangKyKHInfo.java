package com.example.demo.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DangKyKHInfo {
    Integer id;
    String makhachhang;
    Timestamp ngaytao;
    Timestamp lancapnhatcuoi;
    Boolean trangthai;
    String nguoitao;
    String nguoicapnhat;
}
