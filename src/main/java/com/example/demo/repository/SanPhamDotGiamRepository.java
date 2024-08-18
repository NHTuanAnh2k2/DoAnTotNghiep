package com.example.demo.repository;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.entity.SanPhamDotGiam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamDotGiamRepository extends JpaRepository<SanPhamDotGiam, Integer> {
    @Query("SELECT s FROM SanPhamDotGiam s WHERE s.dotgiamgia.id =:IdDot")
    List<SanPhamDotGiam> findSanPhamDotGiamByIdDotgiamgia(Integer IdDot);

    List<SanPhamDotGiam> findBySanphamchitiet(SanPhamChiTiet spct);

    /// Đợt giảm của spct
    @Query("""
        SELECT s FROM SanPhamDotGiam s 
        WHERE s.dotgiamgia.trangthai = 1 
        AND s.sanphamchitiet.id = :id
       """)
    SanPhamDotGiam findDotGiamSPCT(@Param("id") Integer id);





    @Query("""
            SELECT s FROM SanPhamDotGiam s WHERE s.dotgiamgia.trangthai=1
            """)
    List<SanPhamDotGiam> ListDotGiamDangHD();
}
