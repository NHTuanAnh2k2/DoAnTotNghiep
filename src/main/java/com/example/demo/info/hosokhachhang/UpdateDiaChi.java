package com.example.demo.info.hosokhachhang;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDiaChi {
    Integer id;
    @NotBlank(message = "Không được để trống địa chỉ")
    String tenduong;
    @NotBlank(message = "Không được để trống Xã/Phường")
    String xaphuong;
    @NotBlank(message = "Không được để trống Quận/Huyện")
    String quanhuyen;
    @NotBlank(message = "Không được để trống Tỉnh/Thành phố")
    String tinhthanhpho;
    String sdtnguoinhan;
    String hotennguoinhan;
    boolean trangthai;
    Timestamp lancapnhatcuoi;
    Timestamp ngaytao;
}
