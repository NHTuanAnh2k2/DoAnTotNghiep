package com.example.demo.service;

import com.example.demo.entity.NhanVien;
import com.example.demo.info.NhanVienInfo;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NhanVienService {
    List<NhanVien> getNhanVienByTrangThai(Boolean trangThai);
    List<NhanVien> getAll();
    public NhanVien add(NhanVienInfo nhanVien);
}
