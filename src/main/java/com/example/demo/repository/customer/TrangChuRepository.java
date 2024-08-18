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
               WHERE trangthai = 1 -- Lọc sản phẩm chi tiết có trạng thái true
               GROUP BY IdSanPham
           ),
           KichCoCount AS (
               SELECT spct.IdSanPham, COUNT(DISTINCT kc.id) AS soLuongKichCo
               FROM SanPhamChiTiet spct
               JOIN KichCo kc ON spct.IdKichCo = kc.id
               GROUP BY spct.IdSanPham
           ),
           MauSacCount AS (
               SELECT spct.IdSanPham, COUNT(DISTINCT ms.id) AS soLuongMauSac
               FROM SanPhamChiTiet spct
               JOIN MauSac ms ON spct.IdMauSac = ms.id
               GROUP BY spct.IdSanPham
           ),
           GiamGia AS (
               SELECT spct.IdSanPham, MAX(dg.giatrigiam) AS giaGiam
               FROM SanPhamDotGiam spdg
               JOIN DotGiamGia dg ON spdg.iddotgiam = dg.id
               JOIN SanPhamChiTiet spct ON spdg.idchitietsanpham = spct.id
               WHERE dg.trangthai = 1 -- Giả sử bạn chỉ muốn giảm giá còn hiệu lực
               GROUP BY spct.IdSanPham
           )
           SELECT sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh, kc.soLuongKichCo, ms.soLuongMauSac, giamgia.giaGiam
           FROM SanPham sp
           JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
           JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
           JOIN KichCoCount kc ON sp.id = kc.IdSanPham
           JOIN MauSacCount ms ON sp.id = ms.IdSanPham
           LEFT JOIN GiamGia giamgia ON sp.id = giamgia.IdSanPham
           ORDER BY sp.ngaytao DESC, spctg.tongSoLuong DESC;
        """)
    List<Object[]> topspmoinhattrangchu();



    //top sp bán chạy nhất
    @Query(nativeQuery = true, value = """
           WITH AnhDaiDien AS (
               SELECT sct.IdSanPham, a.tenanh,
                      ROW_NUMBER() OVER (PARTITION BY sct.IdSanPham ORDER BY a.tenanh DESC) AS row_num
               FROM Anh a
               JOIN SanPhamChiTiet sct ON a.IdSanPhamChiTiet = sct.Id
           ),
           KichCoCount AS (
               SELECT sct.IdSanPham, COUNT(DISTINCT kc.id) AS soLuongKichCo
               FROM HoaDonChiTiet ct
               JOIN SanPhamChiTiet sct ON ct.IdSanPhamChiTiet = sct.Id
               LEFT JOIN KichCo kc ON sct.IdKichCo = kc.id
               JOIN HoaDon hd ON ct.IdHoaDon = hd.Id
               WHERE MONTH(hd.lancapnhatcuoi) = MONTH(GETDATE())
                 AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE())
                 AND hd.trangthai = 5
               GROUP BY sct.IdSanPham
           ),
           MauSacCount AS (
               SELECT sct.IdSanPham, COUNT(DISTINCT ms.id) AS soLuongMauSac
               FROM HoaDonChiTiet ct
               JOIN SanPhamChiTiet sct ON ct.IdSanPhamChiTiet = sct.Id
               LEFT JOIN MauSac ms ON sct.IdMauSac = ms.id
               JOIN HoaDon hd ON ct.IdHoaDon = hd.Id
               WHERE MONTH(hd.lancapnhatcuoi) = MONTH(GETDATE())
                 AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE())
                 AND hd.trangthai = 5
               GROUP BY sct.IdSanPham
           ),
           GiamGia AS (
               SELECT sct.IdSanPham, MAX(dg.giatrigiam) AS giaGiam
               FROM SanPhamChiTiet sct
               JOIN SanPhamDotGiam spdg ON sct.Id = spdg.idchitietsanpham
               JOIN DotGiamGia dg ON spdg.iddotgiam = dg.id
               WHERE dg.trangthai = 1
               GROUP BY sct.IdSanPham
           )
           SELECT sp.id, sp.TenSanPham, MIN(sct.GiaTien) AS GiaTien, anhdd.tenanh, 
                  SUM(ct.SoLuong) AS TongSoLuong, 
                  COALESCE(kc.soLuongKichCo, 0) AS soLuongKichCo, 
                  COALESCE(ms.soLuongMauSac, 0) AS soLuongMauSac, 
                  COALESCE(giamgia.giaGiam, 0) AS giaGiam
           FROM HoaDonChiTiet ct
           JOIN SanPhamChiTiet sct ON ct.IdSanPhamChiTiet = sct.Id
           JOIN HoaDon hd ON ct.IdHoaDon = hd.Id
           JOIN SanPham sp ON sct.IdSanPham = sp.Id
           JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
           LEFT JOIN KichCoCount kc ON sct.IdSanPham = kc.IdSanPham
           LEFT JOIN MauSacCount ms ON sct.IdSanPham = ms.IdSanPham
           LEFT JOIN GiamGia giamgia ON sp.id = giamgia.IdSanPham
           WHERE MONTH(hd.lancapnhatcuoi) = MONTH(GETDATE())
             AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE())
             AND hd.trangthai = 5
           GROUP BY sp.id, sp.TenSanPham, anhdd.tenanh, kc.soLuongKichCo, ms.soLuongMauSac, giamgia.giaGiam
           ORDER BY TongSoLuong DESC;
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
           ),
           KichCoCount AS (
               SELECT spct.IdSanPham, COUNT(DISTINCT kc.id) AS soLuongKichCo
               FROM SanPhamChiTiet spct
               JOIN KichCo kc ON spct.IdKichCo = kc.id
               GROUP BY spct.IdSanPham
           ),
           MauSacCount AS (
               SELECT spct.IdSanPham, COUNT(DISTINCT ms.id) AS soLuongMauSac
               FROM SanPhamChiTiet spct
               JOIN MauSac ms ON spct.IdMauSac = ms.id
               GROUP BY spct.IdSanPham
           ),
           GiamGia AS (
               SELECT spct.IdSanPham, MAX(dg.giatrigiam) AS giaGiam
               FROM SanPhamDotGiam spdg
               JOIN DotGiamGia dg ON spdg.iddotgiam = dg.id
               JOIN SanPhamChiTiet spct ON spdg.idchitietsanpham = spct.id
               WHERE dg.trangthai = 1 -- Giả sử bạn chỉ muốn giảm giá còn hiệu lực
               GROUP BY spct.IdSanPham
           )
           SELECT sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh, kc.soLuongKichCo, ms.soLuongMauSac, giamgia.giaGiam
           FROM SanPham sp
           JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
           JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
           JOIN KichCoCount kc ON sp.id = kc.IdSanPham
           JOIN MauSacCount ms ON sp.id = ms.IdSanPham
           LEFT JOIN GiamGia giamgia ON sp.id = giamgia.IdSanPham
           ORDER BY sp.ngaytao DESC, spctg.tongSoLuong DESC;
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
           ),
           KichCoCount AS (
               SELECT spct.IdSanPham, COUNT(DISTINCT kc.id) AS soLuongKichCo
               FROM SanPhamChiTiet spct
               JOIN KichCo kc ON spct.IdKichCo = kc.id
               GROUP BY spct.IdSanPham
           ),
           MauSacCount AS (
               SELECT spct.IdSanPham, COUNT(DISTINCT ms.id) AS soLuongMauSac
               FROM SanPhamChiTiet spct
               JOIN MauSac ms ON spct.IdMauSac = ms.id
               GROUP BY spct.IdSanPham
           ),
           GiamGia AS (
               SELECT spct.IdSanPham, MAX(dg.giatrigiam) AS giaGiam
               FROM SanPhamDotGiam spdg
               JOIN DotGiamGia dg ON spdg.iddotgiam = dg.id
               JOIN SanPhamChiTiet spct ON spdg.idchitietsanpham = spct.id
               WHERE dg.trangthai = 1 -- Giả sử bạn chỉ muốn giảm giá còn hiệu lực
               GROUP BY spct.IdSanPham
           )
           SELECT sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh, kc.soLuongKichCo, ms.soLuongMauSac, giamgia.giaGiam
           FROM SanPham sp
           JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
           JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
           JOIN KichCoCount kc ON sp.id = kc.IdSanPham
           JOIN MauSacCount ms ON sp.id = ms.IdSanPham
           LEFT JOIN GiamGia giamgia ON sp.id = giamgia.IdSanPham
           ORDER BY sp.ngaytao ASC, spctg.tongSoLuong ASC;
        """)
    List<Object[]> topspnoibatdetail();

    // search-trangchu
    @Query(nativeQuery = true, value = """
    WITH SanPhamChiTietGrouped AS (
        SELECT spct.IdSanPham, SUM(spct.soluong) AS tongSoLuong, MAX(spct.giatien) AS maxGiaTien
        FROM SanPhamChiTiet spct
        GROUP BY spct.IdSanPham
    ),
    AnhDaiDien AS (
        SELECT spct.IdSanPham, MAX(anh.tenanh) AS maxTenAnh
        FROM Anh anh
        JOIN SanPhamChiTiet spct ON anh.IdSanPhamChiTiet = spct.id
        GROUP BY spct.IdSanPham
    ),
    KichCoCount AS (
        SELECT spct.IdSanPham, COUNT(DISTINCT kc.id) AS soLuongKichCo
        FROM SanPhamChiTiet spct
        JOIN KichCo kc ON spct.IdKichCo = kc.id
        GROUP BY spct.IdSanPham
    ),
    MauSacCount AS (
        SELECT spct.IdSanPham, COUNT(DISTINCT ms.id) AS soLuongMauSac
        FROM SanPhamChiTiet spct
        JOIN MauSac ms ON spct.IdMauSac = ms.id
        GROUP BY spct.IdSanPham
    ),
    GiamGia AS (
        SELECT spct.IdSanPham, MAX(dg.giatrigiam) AS giaGiam
        FROM SanPhamChiTiet spct
        JOIN SanPhamDotGiam spdg ON spct.Id = spdg.idchitietsanpham
        JOIN DotGiamGia dg ON spdg.iddotgiam = dg.id
        WHERE dg.trangthai = 1
        GROUP BY spct.IdSanPham
    )
    SELECT sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.maxGiaTien, anhdd.maxTenAnh, kc.soLuongKichCo, ms.soLuongMauSac, COALESCE(giamgia.giaGiam, 0) AS giaGiam
    FROM SanPham sp
    JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
    JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham
    LEFT JOIN KichCoCount kc ON sp.id = kc.IdSanPham
    LEFT JOIN MauSacCount ms ON sp.id = ms.IdSanPham
    LEFT JOIN GiamGia giamgia ON sp.id = giamgia.IdSanPham
    WHERE (sp.masanpham LIKE %?1% OR sp.tensanpham LIKE %?2%)
    GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, spctg.tongSoLuong, spctg.maxGiaTien, anhdd.maxTenAnh, kc.soLuongKichCo, ms.soLuongMauSac, giamgia.giaGiam
    ORDER BY sp.ngaytao DESC, spctg.tongSoLuong DESC
""")
    List<Object[]> searchTrangChu(String masanpham, String tensanpham);



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
    ),
    KichCoCount AS (
        SELECT spct.IdSanPham, COUNT(DISTINCT kc.id) AS soLuongKichCo
        FROM SanPhamChiTiet spct
        JOIN KichCo kc ON spct.IdKichCo = kc.id
        GROUP BY spct.IdSanPham
    ),
    MauSacCount AS (
        SELECT spct.IdSanPham, COUNT(DISTINCT ms.id) AS soLuongMauSac
        FROM SanPhamChiTiet spct
        JOIN MauSac ms ON spct.IdMauSac = ms.id
        GROUP BY spct.IdSanPham
    ),
    GiamGia AS (
        SELECT spct.IdSanPham, MAX(dg.giatrigiam) AS giaGiam
        FROM SanPhamChiTiet spct
        JOIN SanPhamDotGiam spdg ON spct.Id = spdg.idchitietsanpham
        JOIN DotGiamGia dg ON spdg.iddotgiam = dg.id
        WHERE dg.trangthai = 1
        GROUP BY spct.IdSanPham
    )
    SELECT sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh, kc.soLuongKichCo, ms.soLuongMauSac, COALESCE(giamgia.giaGiam, 0) AS giaGiam
    FROM SanPham sp
    JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
    JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
    LEFT JOIN KichCoCount kc ON sp.id = kc.IdSanPham
    LEFT JOIN MauSacCount ms ON sp.id = ms.IdSanPham
    LEFT JOIN GiamGia giamgia ON sp.id = giamgia.IdSanPham
    ORDER BY sp.ngaytao DESC, spctg.tongSoLuong DESC
""")
    List<Object[]> searchAll();


}
