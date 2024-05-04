package com.example.demo.service.impl;

import com.example.demo.entity.SanPhamDotGiam;
import com.example.demo.repository.SanPhamDotGiamRepository;
import com.example.demo.service.SanPhamDotGiamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPHamDotGiamImp implements SanPhamDotGiamService {
    @Autowired
    SanPhamDotGiamRepository sanPhamDotGiamRepository;
    @Override
    public SanPhamDotGiam AddSanPhamDotGiam(SanPhamDotGiam sanPhamDotGiam) {
        return sanPhamDotGiamRepository.save(sanPhamDotGiam);
    }

    @Override
    public List<SanPhamDotGiam> findSanPhamDotGiamByIdDotgiamgia(Integer IdDot) {
        return sanPhamDotGiamRepository.findSanPhamDotGiamByIdDotgiamgia(IdDot);
    }
}
