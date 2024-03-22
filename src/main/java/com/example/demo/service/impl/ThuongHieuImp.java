package com.example.demo.service.impl;

import com.example.demo.entity.LoaiGiay;
import com.example.demo.repository.ThuongHieuRepository;
import com.example.demo.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThuongHieuImp implements ThuongHieuService{
    @Autowired
    ThuongHieuRepository thuongHieuRepository;

    @Override
    public List<LoaiGiay> findAll() {
        return thuongHieuRepository.findAll();
    }
}
