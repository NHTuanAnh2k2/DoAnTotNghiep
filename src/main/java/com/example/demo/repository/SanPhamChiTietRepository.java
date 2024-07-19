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


@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
    //bên a tùng
    @Query(value = """

            SELECT s FROM SanPhamChiTiet s WHERE  s.mausac=?1 AND s.kichco=?2 AND s.thuonghieu=?3 AND 
            s.chatlieu=?4 AND  s.degiay=?5 AND  s.sanpham=?6
            """)
    SanPhamChiTiet findSPCT(MauSac mauSac, KichCo kichCo, ThuongHieu thuongHieu, ChatLieu chatLieu, DeGiay deGiay, SanPham sanPham);

    // check trùng màu sắc và kích cỡ
    boolean existsByMausacAndKichco(MauSac mauSac, KichCo kichCo);

    // tìm theo sản phẩm
    List<SanPhamChiTiet> findBySanpham(SanPham sanPham);

    //bên a tùng
    Boolean existsByMasanphamchitiet(String ma);

    // bên a tùng
    @Query("SELECT c FROM SanPhamChiTiet c WHERE c.masanphamchitiet like %?1%")
    List<SanPhamChiTiet> searchSPCTtheoMa(String masp);

    // dùng cho giỏ hàng
    @Query(value = """
            SELECT s FROM SanPhamChiTiet s WHERE  s.id=:Id
            """)
    SanPhamChiTiet findByIdSPCT(Integer Id);

    //bên a tùng
    @Query("SELECT c FROM SanPhamChiTiet c WHERE c.sanpham.tensanpham like %?1% and c.chatlieu.ten like %?2% and c.thuonghieu.ten like %?3% and c.degiay.ten like %?4% and c.kichco.ten like %?5% and c.mausac.ten like %?6% and c.gioitinh = ?7 and c.giatien <= ?8 and c.soluong >0")
    List<SanPhamChiTiet> searchSPCT(String tenSp, String chatlieu,
                                    String ThuongHieu, String De,
                                    String KichCo, String MauSac,
                                    Boolean gioitinh, BigDecimal gia);

    // dùng để lấy id cao nhất
    @Query(value = "SELECT MAX(spct.id) FROM SanPhamChiTiet spct")
    Integer findMaxIdSPCT();

    //dùng cho search các thuộc tính sản phẩm chi tiết
    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE (spct.sanpham.tensanpham LIKE ?1 OR spct.masanphamchitiet LIKE ?2) AND (?3 IS NULL OR spct.thuonghieu.id=?3) " +
            "AND (?4 IS NULL OR " + " spct.degiay.id=?4) AND (?5 IS NULL OR spct.kichco.id=?5) AND (?6 IS NULL OR spct.mausac.id=?6)" +
            "AND (?7 IS NULL OR spct.chatlieu.id=?7) AND (?8 IS NULL OR spct.gioitinh=?8) AND (?9 IS NULL OR spct.sanpham.trangthai=?9)")
    List<SanPhamChiTiet> search(String key, String maSPCT, Integer idthuonghieu, Integer iddegiay, Integer idkichco, Integer idmausac, Integer idchatlieu, Boolean gioitinh, Boolean trangthai);

    // bên a tuấn
    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanpham.id = :idSanPham")
    List<SanPhamChiTiet> findBySanPhamId(@Param("idSanPham") Integer idSanPham);

    //update số lượng và giá tiền
    @Transactional
    @Modifying
    @Query(value = "UPDATE SanPhamChiTiet SET soluong = :soluong, giatien = :giatien WHERE id = :id", nativeQuery = true)
    void updateSoLuongVaGiaTienById(@Param("id") Integer id, @Param("soluong") Integer soLuong, @Param("giatien") BigDecimal giaTien);

    // lấy id by sản phẩm
    @Query("SELECT s.sanpham.id FROM SanPhamChiTiet s WHERE s.id = :spctId")
    Integer findIdBySanpham(Integer spctId);

    // dùng để lấy giá tiền của spct
    @Query("SELECT spct.giatien FROM SanPhamChiTiet spct WHERE spct.id = :productId")
    BigDecimal findPriceByProductId(@Param("productId") Integer id);

    // tìm sản phẩm chi tiết theo kích cỡ và màu sắc
    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanpham.id = :sanphamId AND spct.mausac.ten = :color AND spct.kichco.ten = :size")
    SanPhamChiTiet findBySanPhamIdAndColorAndSize(@Param("sanphamId") Integer sanphamId, @Param("color") String color, @Param("size") String size);

    // bên a tùng
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