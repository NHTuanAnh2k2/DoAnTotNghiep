package com.example.demo.repository.hoadon;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    Page<HoaDonChiTiet> findAllByHoadon(HoaDon hd, Pageable p);

    List<HoaDonChiTiet> findAllByHoadon(HoaDon hd);

    Boolean existsByHoadonAndSanphamchitiet(HoaDon hd, SanPhamChiTiet spct);

    List<HoaDonChiTiet> findAllByHoadonAndSanphamchitiet(HoaDon hd, SanPhamChiTiet spct);

    @Query("select h from HoaDonChiTiet h  where h.hoadon.mahoadon =?1 ")
    List<HoaDonChiTiet> timDSHDCTTheoMaHD(String maHD);
}
