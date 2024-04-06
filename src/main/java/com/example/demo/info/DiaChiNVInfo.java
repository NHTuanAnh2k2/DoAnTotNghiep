package com.example.demo.info;

import com.example.demo.entity.NguoiDung;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaChiNVInfo {
    @NotBlank(message = "Không được để trống")
    String tenduong;
    @NotBlank(message = "Vui lòng chọn xã/ phường")
    String xaphuong;
    @NotBlank(message = "Vui lòng chọn quận / huyện")
    String quanhuyen;
    @NotBlank(message = "Vui lòng chọn tỉnh/ thành phố")
    String tinhthanhpho;
    Timestamp ngaytao;
    String nguoitao;
    Timestamp lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
    NguoiDung idnguoidung;
}
