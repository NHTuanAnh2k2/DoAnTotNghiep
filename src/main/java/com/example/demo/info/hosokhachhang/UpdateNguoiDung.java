package com.example.demo.info.hosokhachhang;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateNguoiDung {
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
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
}
