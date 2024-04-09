package com.example.demo.service;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PhieuGiamGiaService {
    List<PhieuGiamGia> findAll();
    Page<PhieuGiamGia> findAllOrderByNgayTaoDESC(String keySearch, Timestamp tungaySearch, Timestamp denngaySearch,
                                                 Boolean kieuSearch,
                                                 Boolean loaiSearch,
                                                 Integer ttSearch, Pageable pageable);
    PhieuGiamGia AddPhieuGiamGia(PhieuGiamGia phieuGiamGia);
    PhieuGiamGia findFirstByOrderByNgaytaoDesc();
    PhieuGiamGia findPhieuGiamGiaById(Integer Id);
}
