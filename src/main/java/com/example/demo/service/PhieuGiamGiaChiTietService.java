package com.example.demo.service;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.PhieuGiamGiaChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PhieuGiamGiaChiTietService {

    List<PhieuGiamGiaChiTiet> timListPhieuTheoHD(HoaDon hd);

}
