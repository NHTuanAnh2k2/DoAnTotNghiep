package com.example.demo.service;

import com.example.demo.entity.DiaChi;
import com.example.demo.info.DiaChiNVInfo;
import com.example.demo.info.NhanVienSearch;

import java.sql.Date;
import java.util.List;

public interface DiaChiService {
    List<DiaChi> getAll();
    List<DiaChi> get(String ht, String sdt);
    List<DiaChi> getTT(Boolean tt);
    public DiaChi add(DiaChiNVInfo diaChi);
    public DiaChi update(DiaChiNVInfo diaChi, Integer id);
    DiaChi search(Integer id);
    List<DiaChi> searchND(String ten, Boolean trangThai, Date batDau, Date ketThuc);
    List<DiaChi> searchkey(NhanVienSearch nhanVienSearch);
    List<DiaChi> searchStart(String ten, Boolean trangThai, Date batDau);
    List<DiaChi> searchEnd(String ten, Boolean trangThai, Date ketThuc);
}
