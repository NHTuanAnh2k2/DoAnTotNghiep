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
    List<NhanVien> getNhanVienByTrangThai(Boolean trangThai);
    List<NhanVien> getAll();
    public NhanVien add(NhanVienInfo nhanVien);
    public NhanVien update(NhanVienInfo nhanVien, Integer id);
    NhanVien search(Integer id);
    List<NhanVien> searchND(String ten, Boolean trangThai, java.sql.Date batDau, Date ketThuc);
    List<NhanVien> searchKey(NhanVienSearch nhanVienSearch);
    List<NhanVien> timSDT(String sdt);
    List<NhanVien> timEmail(String email);
}
