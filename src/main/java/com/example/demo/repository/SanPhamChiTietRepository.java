package com.example.demo.repository;

import com.example.demo.entity.*;
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
import java.util.Optional;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {

    @Query(value = """
                    SELECT s FROM SanPhamChiTiet s WHERE  s.id=:Id
                    """)
    SanPhamChiTiet findByIdSPCT(Integer Id);


    @Query("SELECT c FROM SanPhamChiTiet c WHERE c.sanpham.tensanpham like %?1% and c.chatlieu.ten like %?2% and c.thuonghieu.ten like %?3% and c.degiay.ten like %?4% and c.kichco.ten like %?5% and c.mausac.ten like %?6% and c.gioitinh = ?7 and c.giatien <= ?8 and c.soluong >0")
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

    // dùng cho detail sp
    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanpham.id = :sanPhamId AND spct.mausac.ten = :color")
    Optional<SanPhamChiTiet> findBySanPhamIdAndColor(@Param("sanPhamId") Integer sanPhamId, @Param("color") String color);

    // dùng để lấy giá tiền của spct
    @Query("SELECT spct.giatien FROM SanPhamChiTiet spct WHERE spct.id = :productId")
    BigDecimal findPriceByProductId(@Param("productId") Integer id);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanpham.id = :sanphamId AND spct.mausac.ten = :color AND spct.kichco.ten = :size")
    SanPhamChiTiet findBySanPhamIdAndColorAndSize(@Param("sanphamId") Integer sanphamId, @Param("color") String color, @Param("size") String size);

    Page<SanPhamChiTiet> findAllBySoluongGreaterThan(Integer soluong, Pageable p);

    //đếm số lương thuonghieunam
    @Query(value = """
                WITH AnhDaiDien AS (
                    SELECT spct.IdSanPham, anh.tenanh,
                           ROW_NUMBER() OVER (PARTITION BY spct.IdSanPham ORDER BY anh.tenanh DESC) AS row_num
                    FROM Anh anh
                    JOIN SanPhamChiTiet spct ON anh.IdSanPhamChiTiet = spct.id
                ),
                SanPhamChiTietGrouped AS (
                    SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
                    FROM SanPhamChiTiet
                    WHERE gioitinh = 1
                    GROUP BY IdSanPham
                ),
                SoLuongThuongHieu AS (
                    SELECT IdSanPham, th.ten AS tenThuongHieu, COUNT(*) AS soLuongThuongHieu
                    FROM SanPhamChiTiet spct
                    JOIN ThuongHieu th ON spct.IdThuongHieu = th.Id
                    GROUP BY IdSanPham, th.ten
                )
                SELECT slth.tenThuongHieu, SUM(slth.soLuongThuongHieu) AS tongSoLuongThuongHieu
                FROM SanPham sp
                JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
                JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
                JOIN SoLuongThuongHieu slth ON sp.id = slth.IdSanPham
                GROUP BY slth.tenThuongHieu
                ORDER BY tongSoLuongThuongHieu DESC
            """, nativeQuery = true)
    List<Object[]> findSoLuongThuongHieuGrouped();

    //đếm số lương kichconam
    @Query(value = """
                WITH AnhDaiDien AS (
                    SELECT spct.IdSanPham, anh.tenanh,
                           ROW_NUMBER() OVER (PARTITION BY spct.IdSanPham ORDER BY anh.tenanh DESC) AS row_num
                    FROM Anh anh
                    JOIN SanPhamChiTiet spct ON anh.IdSanPhamChiTiet = spct.id
                ),
                SanPhamChiTietGrouped AS (
                    SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
                    FROM SanPhamChiTiet
                    WHERE gioitinh = 1
                    GROUP BY IdSanPham
                ),
                SoLuongKichCo AS (
                    SELECT IdSanPham, kc.ten AS tenKichCo, COUNT(*) AS soLuongKichCo
                    FROM SanPhamChiTiet spct
                    JOIN KichCo kc ON spct.IdKichCo = kc.Id
                    GROUP BY IdSanPham, kc.ten
                )
                SELECT slkc.tenKichCo, SUM(slkc.soLuongKichCo) AS tongSoLuongKichCo
                FROM SanPham sp
                JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
                JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
                JOIN SoLuongKichCo slkc ON sp.id = slkc.IdSanPham
                GROUP BY slkc.tenKichCo
                ORDER BY tongSoLuongKichCo DESC
            """, nativeQuery = true)
    List<Object[]> findSoLuongKichCoGrouped();

    //đếm số lương thuonghieunu
    @Query(value = """
                WITH AnhDaiDien AS (
                    SELECT spct.IdSanPham, anh.tenanh,
                           ROW_NUMBER() OVER (PARTITION BY spct.IdSanPham ORDER BY anh.tenanh DESC) AS row_num
                    FROM Anh anh
                    JOIN SanPhamChiTiet spct ON anh.IdSanPhamChiTiet = spct.id
                ),
                SanPhamChiTietGrouped AS (
                    SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
                    FROM SanPhamChiTiet
                    WHERE gioitinh = 0
                    GROUP BY IdSanPham
                ),
                SoLuongThuongHieu AS (
                    SELECT IdSanPham, th.ten AS tenThuongHieu, COUNT(*) AS soLuongThuongHieu
                    FROM SanPhamChiTiet spct
                    JOIN ThuongHieu th ON spct.IdThuongHieu = th.Id
                    GROUP BY IdSanPham, th.ten
                )
                SELECT slth.tenThuongHieu, SUM(slth.soLuongThuongHieu) AS tongSoLuongThuongHieu
                FROM SanPham sp
                JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
                JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
                JOIN SoLuongThuongHieu slth ON sp.id = slth.IdSanPham
                GROUP BY slth.tenThuongHieu
                ORDER BY tongSoLuongThuongHieu DESC
            """, nativeQuery = true)
    List<Object[]> findSoLuongThuongHieuGroupedNu();


    //đếm số lương kichconam
    @Query(value = """
                WITH AnhDaiDien AS (
                    SELECT spct.IdSanPham, anh.tenanh,
                           ROW_NUMBER() OVER (PARTITION BY spct.IdSanPham ORDER BY anh.tenanh DESC) AS row_num
                    FROM Anh anh
                    JOIN SanPhamChiTiet spct ON anh.IdSanPhamChiTiet = spct.id
                ),
                SanPhamChiTietGrouped AS (
                    SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
                    FROM SanPhamChiTiet
                    WHERE gioitinh = 0
                    GROUP BY IdSanPham
                ),
                SoLuongKichCo AS (
                    SELECT IdSanPham, kc.ten AS tenKichCo, COUNT(*) AS soLuongKichCo
                    FROM SanPhamChiTiet spct
                    JOIN KichCo kc ON spct.IdKichCo = kc.Id
                    GROUP BY IdSanPham, kc.ten
                )
                SELECT slkc.tenKichCo, SUM(slkc.soLuongKichCo) AS tongSoLuongKichCo
                FROM SanPham sp
                JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
                JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
                JOIN SoLuongKichCo slkc ON sp.id = slkc.IdSanPham
                GROUP BY slkc.tenKichCo
                ORDER BY tongSoLuongKichCo DESC
            """, nativeQuery = true)
    List<Object[]> findSoLuongKichCoGroupedNu();


}