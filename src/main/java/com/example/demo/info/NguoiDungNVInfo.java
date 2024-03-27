package com.example.demo.info;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@Valid
public class NguoiDungNVInfo {
    Integer id;
    @NotBlank(message = "Vui lòng nhập tài khoản")
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
