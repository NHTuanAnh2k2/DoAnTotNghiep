package com.example.demo.repository;

import com.example.demo.entity.SanPhamChiTiet;
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
//    @Modifying
//    @Query("UPDATE SanPhamChiTiet s SET s.soluong = :soluong, s.giatien = :giatien WHERE s.id IN :ids")
//    void updateSoLuongVaGiaTien(@Param("ids") List<Integer> ids, @Param("soluong") Integer soluong, @Param("giatien") BigDecimal giatien);


    @Modifying
    @Query(value = "UPDATE SanPhamChiTiet SET soluong = :soluong, giatien = :giatien WHERE id = :id", nativeQuery = true)
    void update(@Param("id") Integer id, @Param("soluong") Integer soLuong, @Param("giatien") BigDecimal giaTien);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanpham.id = :idSanPham")
    List<SanPhamChiTiet> findBySanPhamId(@Param("idSanPham") Integer idSanPham);
}
