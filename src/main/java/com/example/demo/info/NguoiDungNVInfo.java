package com.example.demo.info;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class NguoiDungNVInfo {
    Integer id;

    @NotBlank(message = "Không được để trống email")
    @Email(message = "Sai định dạng của email")
    String email;
    @NotBlank(message = "Không được để trống họ và tên")
    @Pattern(regexp="^[\\p{L} '‘’-]+$", message="Tên không hợp lệ")
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
    @NotNull(message = "Vui lòng chọn giới tính")
    Boolean gioitinh;
    String anh;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;

}