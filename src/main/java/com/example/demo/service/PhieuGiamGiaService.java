package com.example.demo.service;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PhieuGiamGiaService {
    Page<PhieuGiamGia> findAll(Pageable pageable);
    PhieuGiamGia AddPhieuGiamGia(PhieuGiamGia phieuGiamGia);
    Optional<PhieuGiamGia> findPhieuGiamGiaById(Integer Id);
}
