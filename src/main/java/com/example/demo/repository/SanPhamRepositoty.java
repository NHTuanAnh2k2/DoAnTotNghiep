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

    @Query("SELECT sp.id, sp.tensanpham, sp.ngaytao,SUM(spct.soluong) as tongSoLuong ,sp.trangthai " +
            "FROM SanPham sp " +
            "JOIN sp.spct spct " +
            "WHERE (sp.tensanpham LIKE ?1)AND(?2 IS NULL OR sp.trangthai=?2) " +
            "GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai " +
            "ORDER BY sp.ngaytao DESC, tongSoLuong DESC")
    Page<Object[]> findByTenSanPhamAndTrangThai(String key, Boolean trangthai, Pageable pageable);


    @Query("SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong,sp.trangthai " +
            "FROM SanPham sp JOIN sp.spct spct " +
            "GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai " +
            "ORDER BY sp.ngaytao DESC, tongSoLuong DESC")
    Page<Object[]> findProductsWithTotalQuantityOrderByDateDesc(Pageable pageable);

    @Query( nativeQuery = true,value = """
           SELECT sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh\s
           FROM SanPham sp\s
           JOIN (
               SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien
               FROM SanPhamChiTiet
               GROUP BY IdSanPham, giatien
           ) spct ON sp.id = spct.IdSanPham
           JOIN Anh anh ON sp.id = anh.Id
           ORDER BY sp.ngaytao DESC, tongSoLuong DESC;
              """ )
    Page<Object[]> findProductsWithTotalQuantityOrderByDateDesc2(Pageable pageable);


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

