package com.example.demo.service.impl;

import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.repository.DotGiamGiaRepository;
import com.example.demo.repository.PhieuGiamGiaRepository;
import com.example.demo.service.DotGamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class DotGiamGiaImp implements DotGamGiaService {
    @Autowired
    private DotGiamGiaRepository dotGiamGiaRepository;

    @Override
    public List<DotGiamGia> findAll() {
        return dotGiamGiaRepository.findAll();
    }

    @Override
    public Page<DotGiamGia> findAllOrderByNgayTaoDESC(String keySearch, Timestamp tungaySearch, Timestamp denngaySearch, Integer ttSearch, Pageable pageable) {
        return dotGiamGiaRepository.findAllOrderByNgayTaoDESC(keySearch, tungaySearch, denngaySearch, ttSearch, pageable);
    }

    @Override
    public DotGiamGia AddDotGiamGia(DotGiamGia dotGiamGia) {
        return dotGiamGiaRepository.save(dotGiamGia);
    }

    @Override
    public DotGiamGia findDotGiamGiaById(Integer Id) {
        return dotGiamGiaRepository.findDotGiamGiaById(Id);
    }
}
