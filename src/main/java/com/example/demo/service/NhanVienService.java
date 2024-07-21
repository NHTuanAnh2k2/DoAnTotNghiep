package com.example.demo.service;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.NhanVienInfo;
import com.example.demo.info.NhanVienSearch;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public interface NhanVienService {
    List<NhanVien> getAll();
    List<NhanVien> getAll1(Integer id);
    public NhanVien add(NhanVienInfo nhanVien);
    public NhanVien update(NhanVienInfo nhanVien, Integer id);
    public  NhanVien updateS(NhanVien nv);
    NhanVien search(Integer id);
    List<NhanVien> searchND(String ten, Boolean trangThai, Date batDau, Date ketThuc);
    List<NhanVien> searchNDs(String ten, Date batDau, Date ketThuc);
}
