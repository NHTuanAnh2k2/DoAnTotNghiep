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
    SELECT TOP 8 sp.id, sp.tensanpham, sp.ngaytao, spct.tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh
    FROM SanPham sp
    JOIN (
        SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien -- Chỉ lấy MIN(giatien) để đại diện cho sản phẩm
        FROM SanPhamChiTiet
        GROUP BY IdSanPham
    ) spct ON sp.id = spct.IdSanPham
    JOIN Anh anh ON sp.id = anh.Id
    ORDER BY sp.ngaytao DESC, spct.tongSoLuong DESC
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
    SELECT TOP 8 sp.id, sp.tensanpham, sp.ngaytao, spct.tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh
    FROM SanPham sp
    JOIN (
        SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
        FROM SanPhamChiTiet
        GROUP BY IdSanPham
    ) spct ON sp.id = spct.IdSanPham
    JOIN Anh anh ON sp.id = anh.Id
    ORDER BY sp.ngaytao DESC, spct.tongSoLuong DESC
""")
    List<Object[]> topspmoinhatdetail();


    // top sp nổi bật của detail
    @Query(nativeQuery = true, value = """
    SELECT TOP 8 sp.id, sp.tensanpham, sp.ngaytao, spct.tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh
    FROM SanPham sp
    JOIN (
        SELECT IdSanPham, SUM(soluong) AS tongSoLuong, MIN(giatien) AS giatien
        FROM SanPhamChiTiet
        GROUP BY IdSanPham
    ) spct ON sp.id = spct.IdSanPham
    JOIN Anh anh ON sp.id = anh.Id
    ORDER BY sp.ngaytao ASC , spct.tongSoLuong ASC
""")
    List<Object[]> topspnoibatdetail();
}
