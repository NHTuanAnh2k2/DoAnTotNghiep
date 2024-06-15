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
import java.util.Optional;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
    @Query("SELECT c FROM SanPhamChiTiet c WHERE c.sanpham.tensanpham like %?1% and c.chatlieu.ten like %?2% and c.thuonghieu.ten like %?3% and c.degiay.ten like %?4% and c.kichco.ten like %?5% and c.mausac.ten like %?6% and c.gioitinh = ?7 and c.giatien <= ?8")
    List<SanPhamChiTiet> searchSPCT(String tenSp, String chatlieu,
                                    String ThuongHieu, String De,
                                    String KichCo, String MauSac,
                                    Boolean gioitinh, BigDecimal gia);

    @Query(value = "SELECT MAX(spct.id) FROM SanPhamChiTiet spct")
    Integer findMaxIdSPCT();

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE (spct.sanpham.tensanpham LIKE ?1) AND (?2 IS NULL OR spct.thuonghieu.id=?2) " +
            "AND (?3 IS NULL OR " + " spct.degiay.id=?3) AND (?4 IS NULL OR spct.kichco.id=?4) AND (?5 IS NULL OR spct.mausac.id=?5)" +
            "AND (?6 IS NULL OR spct.chatlieu.id=?6) AND (?7 IS NULL OR spct.gioitinh=?7)")
    List<SanPhamChiTiet> search(String key, Integer idthuonghieu, Integer iddegiay, Integer idkichco, Integer idmausac, Integer idchatlieu, Boolean gioitinh);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanpham.id = :idSanPham")
    List<SanPhamChiTiet> findBySanPhamId(@Param("idSanPham") Integer idSanPham);

    @Transactional
    @Modifying
    @Query(value = "UPDATE SanPhamChiTiet SET soluong = :soluong, giatien = :giatien WHERE id = :id", nativeQuery = true)
    void updateSoLuongVaGiaTienById(@Param("id") Integer id, @Param("soluong") Integer soLuong, @Param("giatien") BigDecimal giaTien);

    @Query("SELECT s.sanpham.id FROM SanPhamChiTiet s WHERE s.id = :spctId")
    Integer findIdBySanpham(Integer spctId);

    //nam
    @Query(nativeQuery = true, value = "SELECT COUNT(th.ten) FROM SanPhamChiTiet AS spct JOIN ThuongHieu AS th ON spct.IdThuongHieu = th.Id WHERE th.ten = 'Nike' AND spct.gioitinh=1")
    int countByThuongHieuTenIsNikeNam();

    //nu
    @Query(nativeQuery = true, value = "SELECT COUNT(th.ten) FROM SanPhamChiTiet AS spct JOIN ThuongHieu AS th ON spct.IdThuongHieu = th.Id WHERE th.ten = 'Nike' AND spct.gioitinh=0")
    int countByThuongHieuTenIsNikeNu();

    // dùng cho detail sp
    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanpham.id = :sanPhamId AND spct.mausac.ten = :color")
    Optional<SanPhamChiTiet> findBySanPhamIdAndColor(@Param("sanPhamId") Integer sanPhamId, @Param("color") String color);

    // dùng để lấy giá tiền của spct
    @Query("SELECT spct.giatien FROM SanPhamChiTiet spct WHERE spct.id = :productId")
    BigDecimal findPriceByProductId(@Param("productId") Integer id);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanpham.id = :sanphamId AND spct.mausac.ten = :color AND spct.kichco.ten = :size")
    SanPhamChiTiet findBySanPhamIdAndColorAndSize(@Param("sanphamId") Integer sanphamId, @Param("color") String color, @Param("size") String size);
}