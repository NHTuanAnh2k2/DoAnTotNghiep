package com.example.demo.repository;

import com.example.demo.entity.*;
import com.example.demo.info.TraCuuDetailInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface HoaDonChiTietRepository1 extends JpaRepository<HoaDonChiTiet, Integer> {
    @Query("SELECT new com.example.demo.info.TraCuuDetailInfo(a.tenanh, sp.tensanpham, kc.ten, hdc.soluong, hd.tongtien, ms.ten)" +
            "FROM HoaDonChiTiet hdc " +
            "JOIN SanPhamChiTiet spc ON hdc.sanphamchitiet.id = spc.id " +
            "JOIN SanPham sp ON spc.sanpham.id = sp.id " +
            "JOIN KichCo kc ON spc.kichco.id = kc.id " +
            "JOIN MauSac ms ON spc.mausac.id = ms.id " +
            "JOIN HoaDon hd ON hdc.hoadon.id = hd.id " +
            "JOIN Anh a ON spc.id = a.sanphamchitiet.id " +
            "WHERE hd.id = :hoaDonId")
    List<TraCuuDetailInfo> findTraCuu(@Param("hoaDonId") Integer hoaDonId);

    @Query("SELECT h FROM HoaDonChiTiet h WHERE h.hoadon.id =?1")
    List<HoaDonChiTiet> findHoaDonChiTietByIdHoaDon(Integer idHoaDon);


}