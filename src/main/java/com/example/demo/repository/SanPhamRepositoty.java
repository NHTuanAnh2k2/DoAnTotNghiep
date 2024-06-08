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

    @Query(value = "SELECT MAX(s.id) FROM SanPham s")
    Integer findMaxIdSP();


    SanPham findFirstByOrderByNgaytaoDesc();

    Page<Object[]> findAllByTensanphamOrTrangthai(String tensanpham, Boolean trangthai, Pageable pageable);

    Boolean existsByTensanpham(String tensanpham);

    @Query("SELECT sp.id, sp.tensanpham, sp.ngaytao,SUM(spct.soluong) as tongSoLuong ,sp.trangthai,sp.masanpham " +
            "FROM SanPham sp " +
            "JOIN sp.spct spct " +
            "WHERE (sp.masanpham LIKE?1 OR sp.tensanpham LIKE ?2)AND(?3 IS NULL OR sp.trangthai=?3) " +
            "GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, sp.masanpham " +
            "ORDER BY sp.ngaytao DESC, tongSoLuong DESC")
    Page<Object[]> findByMasanphamAndTenSanPhamAndTrangThai(String masanpham,String key, Boolean trangthai, Pageable pageable);


    @Query("SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong,sp.trangthai, sp.masanpham " +
            "FROM SanPham sp JOIN sp.spct spct " +
            "GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, sp.masanpham " +
            "ORDER BY sp.ngaytao DESC, tongSoLuong DESC")
    Page<Object[]> findProductsWithTotalQuantityOrderByDateDesc(Pageable pageable);

    //dùng cho sp client
    @Query(nativeQuery = true, value = """
            SELECT top 8 sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh\s
            FROM SanPham sp\s
            JOIN (
                SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien
                FROM SanPhamChiTiet
                GROUP BY IdSanPham, giatien
            ) spct ON sp.id = spct.IdSanPham
            JOIN Anh anh ON sp.id = anh.Id
            ORDER BY sp.ngaytao DESC, tongSoLuong DESC;
               """)
    List<Object[]> findProductsWithTotalQuantityOrderByDateDesc2();
    // dùng cho sp detail
    @Query(nativeQuery = true, value = """
            SELECT top 8 sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh\s
            FROM SanPham sp\s
            JOIN (
                SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien
                FROM SanPhamChiTiet
                GROUP BY IdSanPham, giatien
            ) spct ON sp.id = spct.IdSanPham
            JOIN Anh anh ON sp.id = anh.Id
            ORDER BY sp.ngaytao DESC, tongSoLuong DESC;
               """)
    List<Object[]> findProductsWithTotalQuantityOrderByDateDesc3();
    // dùng cho sp nổi bật detail
    @Query(nativeQuery = true, value = """
            SELECT top 8 sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh\s
            FROM SanPham sp\s
            JOIN (
                SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien
                FROM SanPhamChiTiet
                GROUP BY IdSanPham, giatien
            ) spct ON sp.id = spct.IdSanPham
            JOIN Anh anh ON sp.id = anh.Id
            ORDER BY sp.ngaytao ASC , tongSoLuong ASC;
               """)
    List<Object[]> findProductsWithTotalQuantityOrderByDateDesc4();


    @Query("SELECT sp.id, sp.tensanpham, spct.soluong FROM SanPham sp " +
            "JOIN SanPhamChiTiet spct " +
            "WHERE sp.tensanpham = :tenSanPham " +
            "AND spct.kichco.id = :idKichCo " +
            "AND spct.thuonghieu.id = :idThuongHieu " +
            "AND spct.mausac.id = :idMauSac " +
            "AND spct.chatlieu.id = :idChatLieu " +
            "AND spct.degiay.id = :idDeGiay")
    List<SanPham> findSanPhamByAttributes(@Param("tenSanPham") String tenSanPham, @Param("idKichCo") Long idKichCo, @Param("idThuongHieu") Long idThuongHieu, @Param("idMauSac") Long idMauSac, @Param("idChatLieu") Long idChatLieu, @Param("idDeGiay") Long idDeGiay);
}