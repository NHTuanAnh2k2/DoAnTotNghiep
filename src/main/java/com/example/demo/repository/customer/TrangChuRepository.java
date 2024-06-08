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



    @Query(value ="SELECT sct.MaSanPhamChiTiet, sp.TenSanPham + ' [' + kc.Ten + ' - ' + ms.Ten + ']', sct.GiaTien , COALESCE(SUM(ct.SoLuong), 0)\n" +
            "FROM  HoaDonChiTiet ct \n" +
            "join HoaDon hd on ct.IdHoaDon = hd.Id \n" +
            "JOIN SanPhamChiTiet sct on ct.IdSanPhamChiTiet = sct.Id\n" +
            "Join SanPham sp on sct.IdSanPham = sp.Id\n" +
            "Join KichCo kc on sct.IdKichCo = kc.Id\n" +
            "Join MauSac ms on sct.IdMauSac = ms.Id\n" +
            "WHERE MONTH (hd.lancapnhatcuoi) = MONTH(GETDATE()) AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE()) AND hd.trangthai = 5 \n" +
            "GROUP BY sct.MaSanPhamChiTiet, sp.TenSanPham + ' [' + kc.Ten + ' - ' + ms.Ten + ']', sct.GiaTien\n" +
            "ORDER BY COALESCE(SUM(ct.SoLuong), 0) DESC",nativeQuery = true)
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
