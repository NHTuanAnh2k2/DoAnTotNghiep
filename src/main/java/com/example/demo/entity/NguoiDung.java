package com.example.demo.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "nguoidung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @NotNull(message = "Không được để trống ngày sinh")
    Date ngaysinh;
    String cccd;
    @NotNull(message = "Không được để trống số điện thoại")
    @Pattern(regexp="^0[0-9]{9}$", message="Sai định dạng số điện thoại")
    String sodienthoai;
    Boolean gioitinh;
    String anh;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;

}