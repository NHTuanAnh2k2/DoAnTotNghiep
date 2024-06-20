package com.example.demo.repository.customer;

import com.example.demo.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrangChuRepository extends JpaRepository<SanPham, Integer> {
    // top sp mới nhất của trang chủ
    @Query(nativeQuery = true, value = """
    WITH AnhDaiDien AS (
        SELECT spct.IdSanPham, anh.tenanh,
               ROW_NUMBER() OVER (PARTITION BY spct.IdSanPham ORDER BY anh.tenanh DESC ) AS row_num
        FROM Anh anh
        JOIN SanPhamChiTiet spct ON anh.IdSanPhamChiTiet = spct.id
    ),
    SanPhamChiTietGrouped AS (
        SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
        FROM SanPhamChiTiet
        GROUP BY IdSanPham
    )
    SELECT TOP 8 sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh
    FROM SanPham sp
    JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
    JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
    ORDER BY sp.ngaytao DESC, spctg.tongSoLuong DESC
""")
    List<Object[]> topspmoinhattrangchu();


    //top sp bán chạy nhất
    @Query(nativeQuery = true, value = """
                     SELECT sp.id,sp.TenSanPham, sct.GiaTien ,a.TenAnh ,SUM(ct.SoLuong) as TongSoLuong
                     FROM  HoaDonChiTiet ct
                     JOIN HoaDon hd on ct.IdHoaDon = hd.Id
                     JOIN SanPhamChiTiet sct on ct.IdSanPhamChiTiet = sct.Id
                     JOIN SanPham sp on sct.IdSanPham = sp.Id
                     JOIN Anh a on sct.Id=a.IdSanPhamChiTiet
                     WHERE MONTH (hd.lancapnhatcuoi) = MONTH(GETDATE()) AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE()) AND hd.trangthai = 5
                     GROUP BY sp.id,sp.TenSanPham, sct.GiaTien,a.TenAnh\s
                     ORDER BY TongSoLuong DESC
            """
    )
    List<Object[]> topspbanchaynhat();


    // top sp mới nhất của detail
    @Query(nativeQuery = true, value = """
    WITH AnhDaiDien AS (
        SELECT spct.IdSanPham, anh.tenanh,
               ROW_NUMBER() OVER (PARTITION BY spct.IdSanPham ORDER BY anh.tenanh DESC ) AS row_num
        FROM Anh anh
        JOIN SanPhamChiTiet spct ON anh.IdSanPhamChiTiet = spct.id
    ),
    SanPhamChiTietGrouped AS (
        SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
        FROM SanPhamChiTiet
        GROUP BY IdSanPham
    )
    SELECT TOP 8 sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh
    FROM SanPham sp
    JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
    JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
    ORDER BY sp.ngaytao DESC, spctg.tongSoLuong DESC
""")
    List<Object[]> topspmoinhatdetail();


    // top sp nổi bật của detail
    @Query(nativeQuery = true, value = """
    WITH AnhDaiDien AS (
        SELECT spct.IdSanPham, anh.tenanh,
               ROW_NUMBER() OVER (PARTITION BY spct.IdSanPham ORDER BY anh.tenanh DESC ) AS row_num
        FROM Anh anh
        JOIN SanPhamChiTiet spct ON anh.IdSanPhamChiTiet = spct.id
    ),
    SanPhamChiTietGrouped AS (
        SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
        FROM SanPhamChiTiet
        GROUP BY IdSanPham
    )
    SELECT TOP 8 sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh
    FROM SanPham sp
    JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
    JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
    ORDER BY sp.ngaytao ASC , spctg.tongSoLuong ASC
""")
    List<Object[]> topspnoibatdetail();
}
