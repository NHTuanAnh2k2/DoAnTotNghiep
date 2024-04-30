package com.example.demo.service;

import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface DotGamGiaService {
    List<DotGiamGia> findAll();
    Page<DotGiamGia> findAllOrderByNgayTaoDESC(String keySearch, Timestamp tungaySearch, Timestamp denngaySearch,
                                               Integer ttSearch, Pageable pageable);
    DotGiamGia AddDotGiamGia(DotGiamGia dotGiamGia);
    DotGiamGia findDotGiamGiaById(Integer Id);
}
