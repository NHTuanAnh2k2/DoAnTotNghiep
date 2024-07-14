package com.example.demo.service.impl;

import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.repository.PhieuGiamGiaRepository;
import com.example.demo.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PhieuGiamGiaImp implements PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;


    @Override
    public List<PhieuGiamGia> findAll() {
        return phieuGiamGiaRepository.findAll();
    }

    @Override
    public Page<PhieuGiamGia> findAllOrderByNgayTaoDESC(String keySearch, Timestamp tungaySearch, Timestamp denngaySearch,
                                                        Boolean kieuSearch,
                                                        Boolean loaiSearch,
                                                        Integer ttSearch, Pageable pageable) {
        return phieuGiamGiaRepository.findAllOrderByNgayTaoDESC(keySearch, tungaySearch, denngaySearch, kieuSearch, loaiSearch, ttSearch, pageable);
    }

    @Override
    public PhieuGiamGia AddPhieuGiamGia(PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }


    @Override
    public PhieuGiamGia findFirstByOrderByNgaytaoDesc() {
        return phieuGiamGiaRepository.findFirstByOrderByNgaytaoDesc();
    }

    @Override
    public PhieuGiamGia findPhieuGiamGiaById(Integer Id) {
        return phieuGiamGiaRepository.findPhieuGiamGiaById(Id);
    }

    @Override
    public PhieuGiamGia findPhieuGiamGiaByMaCode(String maCode) {
        return phieuGiamGiaRepository.findPhieuGiamGiaByMaCode(maCode);
    }


}
