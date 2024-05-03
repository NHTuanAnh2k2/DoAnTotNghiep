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
public class NguoiDungKHInfo {
    Integer id;
    String taikhoan;
    String matkhau;
    @NotBlank(message = "Không được để trống email")
    @Email(message = "Sai định dạng của email")
    String email;
    @NotBlank(message = "Không được để trống họ và tên")
//    @Pattern(regexp="^[\\\\p{L} '‘’]+$", message="Invalid name")
    String hovaten;
    //    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @Temporal(TemporalType.DATE)
    Date ngaysinh;
    @NotNull(message = "Không được để trống căn cước công dân")
    @Pattern(regexp="^0[0-9]{11}$", message="Sai định dạng số căn cước")
    String cccd;
    @NotNull(message = "Không được để trống số điện thoại")
    @Pattern(regexp="^0[0-9]{9}$", message="Sai định dạng số điện thoại")
    String sodienthoai;
    @NotNull(message = "Không được để trống giới tính")
    Boolean gioitinh;
}
