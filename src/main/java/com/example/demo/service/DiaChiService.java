package com.example.demo.service;

import com.example.demo.entity.DiaChi;
import com.example.demo.info.DiaChiNVInfo;

import java.util.List;

public interface DiaChiService {
    List<DiaChi> getAll();
    List<DiaChi> get(String ht, String sdt);
    List<DiaChi> getTT(Boolean tt);
    public DiaChi add(DiaChiNVInfo diaChi);
    public DiaChi update(DiaChiNVInfo diaChi, Integer id);
    DiaChi search(Integer id);
}
