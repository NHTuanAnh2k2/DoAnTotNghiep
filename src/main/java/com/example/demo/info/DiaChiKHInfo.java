package com.example.demo.info;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaChiKHInfo {
    Integer id;
    @NotBlank(message = "Không được để trống tên đường")
    String tenduong;
    @NotBlank(message = "Không được để trống Xã/Phường")
    String xaphuong;
    @NotBlank(message = "Không được để trống Quận/Huyện")
    String quanhuyen;
    @NotBlank(message = "Không được để trống Tỉnh/Thành phố")
    String tinhthanhpho;
    String sdtnguoinhan;
    String hotennguoinhan;
    Timestamp lancapnhatcuoi;
}
