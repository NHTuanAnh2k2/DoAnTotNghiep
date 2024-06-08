package com.example.demo.repository;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SanPhamRepositoty extends JpaRepository<SanPham, Integer> {
    // tìm id lớn nhất bên sp
    @Query(value = "SELECT MAX(s.id) FROM SanPham s")
    Integer findMaxIdSP();

    SanPham findFirstByOrderByNgaytaoDesc();

    Boolean existsByTensanpham(String tensanpham);

    // search bên sp
    @Query("SELECT sp.id, sp.tensanpham, sp.ngaytao,SUM(spct.soluong) as tongSoLuong ,sp.trangthai,sp.masanpham " +
            "FROM SanPham sp " +
            "JOIN sp.spct spct " +
            "WHERE (sp.masanpham LIKE?1 OR sp.tensanpham LIKE ?2)AND(?3 IS NULL OR sp.trangthai=?3) " +
            "GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, sp.masanpham " +
            "ORDER BY sp.ngaytao DESC, tongSoLuong DESC")
    Page<Object[]> findByMasanphamAndTenSanPhamAndTrangThai(String masanpham, String key, Boolean trangthai, Pageable pageable);

    // các sp mới nhất bên sp
    @Query("SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong,sp.trangthai, sp.masanpham " +
            "FROM SanPham sp JOIN sp.spct spct " +
            "GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, sp.masanpham " +
            "ORDER BY sp.ngaytao DESC, tongSoLuong DESC")
    Page<Object[]> findProductsWithTotalQuantityOrderByDateDesc(Pageable pageable);

}