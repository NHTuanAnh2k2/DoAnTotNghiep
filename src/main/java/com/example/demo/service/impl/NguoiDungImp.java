package com.example.demo.service.impl;

import com.example.demo.entity.NguoiDung;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NguoiDungImp implements NguoiDungService {
    @Autowired
    NguoiDungRepository nguoiDungRepository;

    @Override
    public NguoiDung findByTaiKhoan(String taikhoan) {
        return nguoiDungRepository.findNguoiDungByTaikhoan(taikhoan);
    }

    @Override
    public List<NguoiDung> findAll() {
        return nguoiDungRepository.findAll();
    }

    @Override
    public void save(NguoiDung nguoiDung) {
        nguoiDungRepository.save(nguoiDung);
    }
}
