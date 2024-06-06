package com.example.demo.repository;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface ThongKeRepository extends JpaRepository<HoaDon, Integer> {
    @Query(value = "SELECT COUNT (*) FROM HoaDon h WHERE MONTH(h.lancapnhatcuoi) = MONTH(GETDATE()) AND YEAR(h.lancapnhatcuoi) = YEAR(GETDATE()) AND h.trangthai = 5", nativeQuery = true)
    public int thongKeTheoThang();

    @Query(value = "SELECT COALESCE(SUM(h.tongtien), 0) FROM HoaDon h WHERE MONTH(h.lancapnhatcuoi) = MONTH(GETDATE()) AND YEAR(h.lancapnhatcuoi) = YEAR(GETDATE()) AND h.trangthai = 5", nativeQuery = true)
    public int thongKeTienTheoThang();
    @Query(value = "SELECT COUNT (*) FROM HoaDon h WHERE  CAST(h.lancapnhatcuoi AS DATE) = CAST(GETDATE() AS DATE) AND h.trangthai = 5", nativeQuery = true)
    public int thongKeTheoNgay();

    @Query(value = "SELECT COALESCE(SUM(h.tongtien), 0) FROM HoaDon h WHERE CAST(h.lancapnhatcuoi AS DATE) = CAST(GETDATE() AS DATE) AND h.trangthai = 5", nativeQuery = true)
    public int thongKeTienTheoNgay();
    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "\tWHEN subquery.tt = 0 THEN N'Chờ xác nhận'\n" +
            "        WHEN subquery.tt = 1 THEN N'Đã xác nhận'\n" +
            "        WHEN subquery.tt = 2 THEN N'Chờ giao hàng'\n" +
            "\t\tWHEN subquery.tt = 3 THEN N'Đang giao hàng'\n" +
            "\t\tWHEN subquery.tt = 4 THEN N'Đã thanh toán'\n" +
            "\t\tWHEN subquery.tt = 5 THEN N'Hoàn thành'\n" +
            "\t\tWHEN subquery.tt = 6 THEN N'Đã hủy'\n" +
            "    END AS ten_trang_thai,\n" +
            "    COALESCE(COUNT(HoaDon.TrangThai), 0) AS so_luong\n" +
            "FROM \n" +
            "    (\n" +
            "        SELECT 0 AS tt UNION ALL\n" +
            "\t\tSELECT 1 UNION ALL\n" +
            "        SELECT 2 UNION ALL\n" +
            "        SELECT 3 UNION ALL\n" +
            "        SELECT 4 UNION ALL\n" +
            "        SELECT 5 UNION ALL\n" +
            "        SELECT 6\n" +
            "    ) AS subquery\n" +
            "\n" +
            "LEFT JOIN \n" +
            "    HoaDon ON subquery.tt = HoaDon.TrangThai\n" +
            "\tAND MONTH(HoaDon.lancapnhatcuoi) = MONTH(GETDATE()) AND YEAR(HoaDon.lancapnhatcuoi) = YEAR(GETDATE())\n" +
            "GROUP BY \n" +
            "subquery.tt",nativeQuery = true)
    List<Object[]> bdTron();
    @Query(value = "SELECT COALESCE(SUM(hd.TongTien), 0)\n" +
            "FROM HoaDon hd\n" +
            "Where DAY(hd.lanCapNhatCuoi) = DAY(GETDATE()) AND hd.TrangThai = 5", nativeQuery = true)
    BigDecimal ttdsn();
    @Query(value = "SELECT COALESCE(SUM(hd.TongTien), 0)\n" +
            "FROM HoaDon hd\n" +
            "Where MONTH (hd.lancapnhatcuoi) = MONTH(GETDATE()) AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE())  AND hd.TrangThai = 5", nativeQuery = true)
    BigDecimal ttdst();
    @Query(value = "SELECT COALESCE(SUM(ct.SoLuong), 0)\n" +
            "FROM HoaDon hd join HoaDonChiTiet ct on hd.Id = ct.IdHoaDon\n" +
            "Where DAY(hd.lanCapNhatCuoi) = DAY(GETDATE()) AND hd.TrangThai = 5", nativeQuery = true)
    int ttspn();
    @Query(value = "SELECT COALESCE(SUM(ct.SoLuong), 0)\n" +
            "FROM HoaDon hd join HoaDonChiTiet ct on hd.Id = ct.IdHoaDon\n" +
            "Where MONTH (hd.lancapnhatcuoi) = MONTH(GETDATE()) AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE())  AND hd.TrangThai = 5", nativeQuery = true)
    int ttspt();
    @Query(value = "SELECT Count(*)\n" +
            "FROM HoaDon hd\n" +
            "Where DAY(hd.lanCapNhatCuoi) = DAY(GETDATE()) AND hd.TrangThai = 5", nativeQuery = true)
    int tthdn();
    @Query(value = "SELECT Count(*)\n" +
            "FROM HoaDon hd\n" +
            "Where MONTH (hd.lancapnhatcuoi) = MONTH(GETDATE()) AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE())  AND hd.TrangThai = 5", nativeQuery = true)
    int tthdt();

    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.lancapnhatcuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN hd.TongTien ELSE 0 END), 0) = 0 \n" +
            "            THEN 100\n" +
            "        ELSE\n" +
            "            CAST(ROUND(((SUM(CASE WHEN MONTH(hd.lancapnhatcuoi) = MONTH(GETDATE()) THEN hd.TongTien ELSE 0 END) /\n" +
            "            SUM(CASE WHEN MONTH(hd.lancapnhatcuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN hd.TongTien ELSE 0 END)) - 1) * 100, 0) AS INT)\n" +
            "    END AS percentage_change\n" +
            "FROM \n" +
            "    HoaDon hd\n" +
            "WHERE \n" +
            "    (MONTH(hd.lancapnhatcuoi) = MONTH(GETDATE()) OR MONTH(hd.lancapnhatcuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())))\n" +
            "    AND YEAR(hd.lancapnhatcuoi) = YEAR(GETDATE())\n" +
            "    AND hd.TrangThai = 5;\n",nativeQuery = true)
    int ptdtt();
    @Query(value = "\tSELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.lancapnhatcuoi AS DATE) = CAST(GETDATE() AS DATE) THEN hd.TongTien ELSE 0 END), 0) = 0 \n" +
            "            THEN 100\n" +
            "        ELSE\n" +
            "            CAST(ROUND(((SUM(CASE WHEN CAST(hd.lancapnhatcuoi AS DATE) = CAST(GETDATE() AS DATE) THEN hd.TongTien ELSE 0 END) /\n" +
            "            SUM(CASE WHEN CAST(hd.lancapnhatcuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN hd.TongTien ELSE 0 END)) - 1) * 100, 0) AS INT)\n" +
            "    END AS percentage_change\n" +
            "FROM \n" +
            "    HoaDon hd\n" +
            "WHERE \n" +
            "    (CAST(hd.lancapnhatcuoi AS DATE) = CAST(GETDATE() AS DATE) OR CAST(hd.lancapnhatcuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE))\n" +
            "    AND hd.TrangThai = 5;", nativeQuery = true)
    int ptdtn();
    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) THEN ct.SoLuong ELSE 0 END), 0) = 0 \n" +
            "            THEN 100\n" +
            "        ELSE\n" +
            "            CAST(ROUND(((SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) THEN ct.SoLuong ELSE 0 END) /\n" +
            "            SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN ct.SoLuong ELSE 0 END)) - 1) * 100, 0) AS INT)\n" +
            "    END AS percentage_change\n" +
            "FROM \n" +
            "    HoaDon hd\n" +
            "JOIN \n" +
            "    HoaDonChiTiet ct ON hd.Id = ct.IdHoaDon\n" +
            "WHERE \n" +
            "    (CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) OR CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE))\n" +
            "    AND hd.TrangThai = 5;",nativeQuery = true)
    int ptspn();
    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) THEN ct.SoLuong ELSE 0 END), 0) = 0 \n" +
            "            THEN 100\n" +
            "        ELSE\n" +
            "            CAST(ROUND(((SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) THEN ct.SoLuong ELSE 0 END) /\n" +
            "            SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN ct.SoLuong ELSE 0 END)) - 1) * 100, 0) AS INT)\n" +
            "    END AS percentage_change\n" +
            "FROM \n" +
            "    HoaDon hd\n" +
            "JOIN \n" +
            "    HoaDonChiTiet ct ON hd.Id = ct.IdHoaDon\n" +
            "WHERE \n" +
            "    (MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) OR MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())))\n" +
            "    AND YEAR(hd.LanCapNhatCuoi) = YEAR(GETDATE())\n" +
            "    AND hd.TrangThai = 5;",nativeQuery = true)
    int ptspt();
    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END), 0) = 0 \n" +
            "            THEN 100\n" +
            "        ELSE\n" +
            "            CAST(ROUND(((SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END) /\n" +
            "            SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN 1 ELSE 0 END)) - 1) * 100, 0) AS INT)\n" +
            "    END AS percentage_change\n" +
            "FROM \n" +
            "    HoaDon hd\n" +
            "WHERE \n" +
            "    (CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) OR CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE))\n" +
            "    AND hd.TrangThai = 5;",nativeQuery = true)
    int pthdn();
    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) THEN 1 ELSE 0 END), 0) = 0 \n" +
            "            THEN 100\n" +
            "        ELSE\n" +
            "            CAST(ROUND(((SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) THEN 1 ELSE 0 END) /\n" +
            "            SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN 1 ELSE 0 END)) - 1) * 100, 0) AS INT)\n" +
            "    END AS percentage_change\n" +
            "FROM \n" +
            "    HoaDon hd\n" +
            "WHERE \n" +
            "    (MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) OR MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())))\n" +
            "    AND YEAR(hd.LanCapNhatCuoi) = YEAR(GETDATE())\n" +
            "    AND hd.TrangThai = 5;",nativeQuery = true)
    int pthdt();
    @Query(value = "SELECT COALESCE(SUM(ct.SoLuong), 0) FROM HoaDonChiTiet ct  Join HoaDon h on ct.IdHoaDon = h.Id WHERE MONTH(h.lancapnhatcuoi) = MONTH(GETDATE()) AND YEAR(h.lancapnhatcuoi) = YEAR(GETDATE()) AND h.trangthai = 5",nativeQuery = true)
    int soLuongsp();

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
    List<Object[]> soLuongDaBan();
    @Query(value = "SELECT ct.sanphamchitiet.masanphamchitiet, ct.sanphamchitiet.sanpham.tensanpham || ' [' || ct.sanphamchitiet.kichco.ten || ' - ' || ct.sanphamchitiet.mausac.ten|| ']', ct.sanphamchitiet.giatien,ct.sanphamchitiet.soluong \n" +
            "FROM HoaDonChiTiet ct \n " +
            "WHERE ct.sanphamchitiet.soluong <= 10 \n" +
            "ORDER BY ct.sanphamchitiet.soluong ASC")
    List<Object[]> soLuongTon();
}
