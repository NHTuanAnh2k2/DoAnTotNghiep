package com.example.demo.info;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DangKyNDInfo {
    Integer id;
    @NotBlank(message = "Không được để trống email")
    @Email(message = "Sai định dạng của email")
    String email;
    @Pattern(regexp="^[\\p{L} '‘’-]+$", message="Tên không hợp lệ")
    @NotBlank(message = "Không được để trống họ tên")
    String hovaten;
    @NotNull(message = "Không được để trống số điện thoại")
    @Pattern(regexp="^0[0-9]{9}$", message="Sai định dạng số điện thoại")
    String sodienthoai;
    @NotNull(message = "Không được để trống ngày sinh")
    Date ngaysinh;
    Boolean gioitinh;
    @NotBlank(message = "Không được để trống tên đăng nhập")
    String taikhoan;
    @NotBlank(message = "Không được để trống mật khẩu")
    String matkhau;
    String xacnhanmatkhau;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
}
