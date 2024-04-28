package com.example.demo.info;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import lombok.*;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhachHangInfo {
    private KhachHang khachhang;
    private DiaChi diachi;
    private NguoiDung nguoidung;

}
