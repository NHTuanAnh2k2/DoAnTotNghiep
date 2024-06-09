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
    List<Object[]> topspmoinhatdetail();

    // top sp nổi bật của detail
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
    List<Object[]> topspnoibatdetail();
}