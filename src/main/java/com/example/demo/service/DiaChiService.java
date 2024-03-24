package com.example.demo.service;

import com.example.demo.entity.DiaChi;
import com.example.demo.info.DiaChiNVInfo;

import java.util.List;

public interface DiaChiService {
    List<DiaChi> getAll();
    public DiaChi add(DiaChiNVInfo diaChi);
}
