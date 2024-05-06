package com.example.demo.service;

import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SanPhamChiTietService {
    List<SanPhamChiTiet> timSPCTHDCT(String tenSp, String chatlieu,
                                     String ThuongHieu, String De,
                                     String KichCo, String MauSac,
                                     Boolean gioitinh, BigDecimal gia);
    List<SanPhamChiTiet> findAll();

    Page<SanPhamChiTiet> finAllPage(Pageable pageable);

    SanPhamChiTiet addSPCT(SanPhamChiTiet sanPhamChiTiet);

    void deleteSPCT(Integer id);

//    void updateSoLuongVaGiaTien(List<Integer> ids, Integer soluong, BigDecimal giatien);

//    void update(Integer id, Integer soLuong, BigDecimal giaTien);
    List<SanPhamChiTiet> findBySanPhamId(Integer idSanPham);
    SanPhamChiTiet findById( Integer id);
}
