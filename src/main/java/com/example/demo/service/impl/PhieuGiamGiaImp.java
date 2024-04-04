package com.example.demo.service.impl;

import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.repository.PhieuGiamGiaRepository;
import com.example.demo.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhieuGiamGiaImp implements PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;
    @Override
    public Page<PhieuGiamGia> findAll(Pageable pageable) {
        return phieuGiamGiaRepository.findAll(pageable);
    }

    @Override
    public PhieuGiamGia AddPhieuGiamGia(PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    @Override
    public Optional<PhieuGiamGia> findPhieuGiamGiaById(Integer Id) {
        return phieuGiamGiaRepository.findById(Id);
    }

    @Override
    public PhieuGiamGia findFirstByOrderByNgaytaoDesc() {
        return phieuGiamGiaRepository.findFirstByOrderByNgaytaoDesc();
    }


}
