package com.example.demo.info;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class KhachHangInfo {
    private KhachHang khachhang;
    private DiaChi diachi;
    private NguoiDung nguoidung;
}
