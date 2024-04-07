package com.example.demo.repository;

import com.example.demo.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE (spct.sanpham.tensanpham LIKE ?1) AND (?2 IS NULL OR spct.thuonghieu.id=?2) " +
            "AND (?3 IS NULL OR " + " spct.degiay.id=?3) AND (?4 IS NULL OR spct.kichco.id=?4) AND (?5 IS NULL OR spct.mausac.id=?5)" +
            "AND (?6 IS NULL OR spct.chatlieu.id=?6)")
    List<SanPhamChiTiet> search(String key, Integer idthuonghieu, Integer iddegiay, Integer idkichco, Integer idmausac, Integer idchatlieu);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanpham.id = :idSanPham")
    List<SanPhamChiTiet> findBySanPhamId(@Param("idSanPham") Integer idSanPham);

    @Transactional
    @Modifying
    @Query(value = "UPDATE SanPhamChiTiet SET soluong = :soluong, giatien = :giatien WHERE id = :id", nativeQuery = true)
    void update(@Param("id") Integer id, @Param("soluong") Integer soLuong, @Param("giatien") BigDecimal giaTien);

    @Transactional
    @Modifying
    @Query(value = "UPDATE SanPhamChiTiet SET SoLuong = :soluong, GiaTien = :giatien WHERE IdSanPham = :idsanpham", nativeQuery = true)
    void updateByIdSanPham(@Param("idsanpham") Integer idsanpham, @Param("soluong") Integer soluong, @Param("giatien") BigDecimal giatien);

}
