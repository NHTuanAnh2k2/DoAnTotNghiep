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
    WITH AnhDaiDien AS (
        SELECT sct.IdSanPham, a.tenanh,
               ROW_NUMBER() OVER (PARTITION BY sct.IdSanPham ORDER BY a.tenanh DESC) AS row_num
        FROM Anh a
        JOIN SanPhamChiTiet sct ON a.IdSanPhamChiTiet = sct.Id
    )
    SELECT sp.id, sp.TenSanPham, sct.GiaTien, anhdd.tenanh, SUM(ct.SoLuong) as TongSoLuong
    FROM HoaDonChiTiet ct
    JOIN HoaDon hd ON ct.IdHoaDon = hd.Id
    JOIN SanPhamChiTiet sct ON ct.IdSanPhamChiTiet = sct.Id
    JOIN SanPham sp ON sct.IdSanPham = sp.Id
    JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
    WHERE MONTH(hd.lancapnhatcuoi) = MONTH(GETDATE()) 
      AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE()) 
      AND hd.trangthai = 5
    GROUP BY sp.id, sp.TenSanPham, sct.GiaTien, anhdd.tenanh
    ORDER BY TongSoLuong DESC
""")
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

    // search-trangchu
    @Query(value = """
            SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong, sp.trangthai, MAX(spct.giatien) AS maxGiaTien, MAX(anh.tenanh) AS maxTenAnh, spct.gioitinh
            FROM SanPham sp
            JOIN sp.spct spct
            JOIN spct.anh anh
            WHERE 
                 (sp.masanpham LIKE %?1% OR sp.tensanpham LIKE %?2%)
            GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, spct.gioitinh
            ORDER BY sp.ngaytao DESC, tongSoLuong DESC
                  """)
    List<Object[]>searchTrangChu(String masanpham, String tensanpham);


    @Query(nativeQuery = true, value = """
                WITH AnhDaiDien AS (
                    SELECT spct.IdSanPham, anh.tenanh,
                           ROW_NUMBER() OVER (PARTITION BY spct.IdSanPham ORDER BY anh.tenanh DESC) AS row_num
                    FROM Anh anh
                    JOIN SanPhamChiTiet spct ON anh.IdSanPhamChiTiet = spct.id
                ),
                SanPhamChiTietGrouped AS (
                    SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
                    FROM SanPhamChiTiet
                    GROUP BY IdSanPham
                )
                SELECT sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh
                FROM SanPham sp
                JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
                JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
                ORDER BY sp.ngaytao DESC, spctg.tongSoLuong DESC
            """)
    List<Object[]> searchAll();
}
