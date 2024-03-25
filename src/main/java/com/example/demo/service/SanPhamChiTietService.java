package com.example.demo.service;

import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SanPhamChiTietService {

    List<SanPhamChiTiet> findAll();

    Page<SanPhamChiTiet> finAllPage(Pageable pageable);

    SanPhamChiTiet addSPCT(SanPhamChiTiet sanPhamChiTiet);

    void deleteSPCT(Integer id);

//    void updateSoLuongVaGiaTien(List<Integer> ids, Integer soluong, BigDecimal giatien);

    void update(Integer id, Integer soLuong, BigDecimal giaTien);

    SanPhamChiTiet findById( Integer id);
}
