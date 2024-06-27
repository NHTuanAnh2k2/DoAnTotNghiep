package com.example.demo.info.hosokhachhang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateKhachHang {
    Integer id;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
}
