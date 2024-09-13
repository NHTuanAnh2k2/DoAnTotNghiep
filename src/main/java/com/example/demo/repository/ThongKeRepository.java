package com.example.demo.repository;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.lancapnhatcuoi) = MONTH(GETDATE()) THEN hd.TongTien ELSE 0 END), 0) =\n" +
            "             COALESCE(SUM(CASE WHEN MONTH(hd.lancapnhatcuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN hd.TongTien ELSE 0 END), 0) THEN 100\n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.lancapnhatcuoi) = MONTH(GETDATE()) THEN hd.TongTien ELSE 0 END), 0) = 0 THEN 0\n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.lancapnhatcuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN hd.TongTien ELSE 0 END), 0) = 0 THEN 0\n" +
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
    @Query(value = "SELECT\n" +
            "    CASE\n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.lancapnhatcuoi AS DATE) = CAST(GETDATE() AS DATE) THEN hd.TongTien ELSE 0 END), 0) =\n" +
            "             COALESCE(SUM(CASE WHEN CAST(hd.lancapnhatcuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN hd.TongTien ELSE 0 END), 0) THEN 100\n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.lancapnhatcuoi AS DATE) = CAST(GETDATE() AS DATE) THEN hd.TongTien ELSE 0 END), 0) = 0 THEN 0\n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.lancapnhatcuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN hd.TongTien ELSE 0 END), 0) = 0 THEN 0\n" +
            "        ELSE\n" +
            "            CAST(ROUND(((SUM(CASE WHEN CAST(hd.lancapnhatcuoi AS DATE) = CAST(GETDATE() AS DATE) THEN hd.TongTien ELSE 0 END) /\n" +
            "            SUM(CASE WHEN CAST(hd.lancapnhatcuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN hd.TongTien ELSE 0 END)) - 1) * 100, 0) AS INT)\n" +
            "    END AS percentage_change\n" +
            "FROM \n" +
            "    HoaDon hd\n" +
            "WHERE\n" +
            "    (CAST(hd.lancapnhatcuoi AS DATE) = CAST(GETDATE() AS DATE) OR CAST(hd.lancapnhatcuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE))\n" +
            "    AND hd.TrangThai = 5;\n", nativeQuery = true)
    int ptdtn();
    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) THEN ct.SoLuong ELSE 0 END), 0) =\n" +
            "             COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN ct.SoLuong ELSE 0 END), 0) THEN 100\n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) THEN ct.SoLuong ELSE 0 END), 0) = 0 THEN 0\n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN ct.SoLuong ELSE 0 END), 0) = 0 THEN 0\n" +
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
            "    AND hd.TrangThai = 5;\n",nativeQuery = true)
    int ptspn();
    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) THEN ct.SoLuong ELSE 0 END), 0) =\n" +
            "             COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN ct.SoLuong ELSE 0 END), 0) THEN 100\n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) THEN ct.SoLuong ELSE 0 END), 0) = 0 THEN 0\n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN ct.SoLuong ELSE 0 END), 0) = 0 THEN 0\n" +
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
            "    AND hd.TrangThai = 5;\n",nativeQuery = true)
    int ptspt();
    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END), 0) =\n" +
            "             COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN 1 ELSE 0 END), 0) THEN 100\n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END), 0) = 0 THEN 0\n" +
            "        WHEN COALESCE(SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN 1 ELSE 0 END), 0) = 0 THEN 0\n" +
            "        ELSE\n" +
            "            CAST(ROUND(((SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END) /\n" +
            "            SUM(CASE WHEN CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) THEN 1 ELSE 0 END)) - 1) * 100, 0) AS INT)\n" +
            "    END AS percentage_change\n" +
            "FROM \n" +
            "    HoaDon hd\n" +
            "WHERE \n" +
            "    (CAST(hd.LanCapNhatCuoi AS DATE) = CAST(GETDATE() AS DATE) OR CAST(hd.LanCapNhatCuoi AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE))\n" +
            "    AND hd.TrangThai = 5;\n",nativeQuery = true)
    int pthdn();
    @Query(value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) THEN 1 ELSE 0 END), 0) =\n" +
            "             COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN 1 ELSE 0 END), 0) THEN 100\n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) THEN 1 ELSE 0 END), 0) = 0 THEN 0\n" +
            "        WHEN COALESCE(SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN 1 ELSE 0 END), 0) = 0 THEN 0\n" +
            "        ELSE\n" +
            "            CAST(ROUND(((SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) THEN 1 ELSE 0 END) /\n" +
            "            SUM(CASE WHEN MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN 1 ELSE 0 END)) - 1) * 100, 0) AS INT)\n" +
            "    END AS percentage_change\n" +
            "FROM \n" +
            "    HoaDon hd\n" +
            "WHERE \n" +
            "    (MONTH(hd.LanCapNhatCuoi) = MONTH(GETDATE()) OR MONTH(hd.LanCapNhatCuoi) = MONTH(DATEADD(MONTH, -1, GETDATE())))\n" +
            "    AND YEAR(hd.LanCapNhatCuoi) = YEAR(GETDATE())\n" +
            "    AND hd.TrangThai = 5;\n",nativeQuery = true)
    int pthdt();
    @Query(value = "SELECT COALESCE(SUM(ct.SoLuong), 0) \n" +
            "FROM HoaDonChiTiet ct  Join HoaDon h on ct.IdHoaDon = h.Id \n" +
            "WHERE MONTH(h.lancapnhatcuoi) = MONTH(GETDATE()) AND \n" +
            "YEAR(h.lancapnhatcuoi) = YEAR(GETDATE()) AND h.trangthai = 5",nativeQuery = true)
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
    @Query(value = "SELECT ct.masanphamchitiet, ct.sanpham.tensanpham || ' [' || ct.kichco.ten || ' - ' || ct.mausac.ten|| ']', ct.giatien, ct.soluong \n" +
            "FROM SanPhamChiTiet ct \n " +
            "WHERE ct.soluong <= 10 \n" +
            "GROUP BY ct.masanphamchitiet, ct.sanpham.tensanpham || ' [' || ct.kichco.ten || ' - ' || ct.mausac.ten|| ']', ct.giatien, ct.soluong \n"+
            "ORDER BY ct.soluong ASC")
    List<Object[]> soLuongTon();
    @Query(value = "WITH DateRange AS (\n" +
            "SELECT CAST(GETDATE() - 6 AS DATE) AS SaleDay\n" +
            "UNION ALL\n" +
            "SELECT DATEADD(DAY, 1, SaleDay)\n" +
            "FROM DateRange\n" +
            "WHERE SaleDay < CAST(GETDATE() AS DATE)\n" +
            ")\n" +
            "SELECT d.SaleDay,\n" +
            "       COALESCE(SUM(sales.SoLuong), 0) AS total_quantity_sold,\n" +
            "       COALESCE(COUNT(sales.IdHoaDon), 0) AS total_invoices\n" +
            "FROM DateRange d\n" +
            "LEFT JOIN \n" +
            "    (SELECT CAST(hd.LanCapNhatCuoi AS DATE) AS sale_day, ct.SoLuong, hd.Id AS IdHoaDon\n" +
            "     FROM HoaDon hd\n" +
            "     JOIN HoaDonChiTiet ct ON hd.Id = ct.IdHoaDon\n" +
            "     WHERE hd.LanCapNhatCuoi >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE))\n" +
            "\t\tAND hd.LanCapNhatCuoi < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))\n" +
            "\t\tAND hd.TrangThai = 5) AS sales\n" +
            "     ON d.SaleDay = sales.sale_day\n" +
            "     GROUP BY d.SaleDay\n" +
            "     ORDER BY d.SaleDay\n" +
            "     OPTION (MAXRECURSION 7);",nativeQuery = true)
    List<Object[]> dayData();
    @Query(value ="WITH MonthRange AS (\n" +
            "    SELECT FORMAT(DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) - 11, 0), 'yyyy-MM') AS SaleMonth\n" +
            "    UNION ALL\n" +
            "    SELECT FORMAT(DATEADD(MONTH, 1, CAST(SaleMonth + '-01' AS DATE)), 'yyyy-MM')\n" +
            "    FROM MonthRange\n" +
            "    WHERE DATEADD(MONTH, 1, CAST(SaleMonth + '-01' AS DATE)) < GETDATE()\n" +
            ")\n" +
            "\n" +
            "SELECT \n" +
            "    m.SaleMonth,\n" +
            "    COALESCE(SUM(sales.SoLuong), 0) AS total_quantity_sold,\n" +
            "    COALESCE(COUNT(sales.IdHoaDon), 0) AS total_invoices\n" +
            "FROM \n" +
            "    MonthRange m\n" +
            "LEFT JOIN \n" +
            "    (SELECT FORMAT(DATEADD(MONTH, DATEDIFF(MONTH, 0, hd.LanCapNhatCuoi), 0), 'yyyy-MM') AS SaleMonth, ct.SoLuong, hd.Id AS IdHoaDon\n" +
            "     FROM HoaDon hd\n" +
            "     JOIN HoaDonChiTiet ct ON hd.Id = ct.IdHoaDon\n" +
            "     WHERE hd.LanCapNhatCuoi >= DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) - 12, 0)\n" +
            "       AND hd.LanCapNhatCuoi < GETDATE()\n" +
            "       AND hd.TrangThai = 5) AS sales\n" +
            "ON m.SaleMonth = sales.SaleMonth\n" +
            "GROUP BY \n" +
            "    m.SaleMonth\n" +
            "ORDER BY \n" +
            "    m.SaleMonth\n" +
            "OPTION (MAXRECURSION 0);\n",nativeQuery = true)
    List<Object[]> thangData();
    @Query(value = "WITH YearRange AS (\n" +
            "    SELECT DATEPART(YEAR, DATEADD(YEAR, -4, GETDATE())) AS SaleYear\n" +
            "    UNION ALL\n" +
            "    SELECT SaleYear + 1\n" +
            "    FROM YearRange\n" +
            "    WHERE SaleYear < DATEPART(YEAR, GETDATE())\n" +
            ")\n" +
            "\n" +
            "SELECT \n" +
            "    y.SaleYear,\n" +
            "    COALESCE(SUM(sales.SoLuong), 0) AS total_quantity_sold,\n" +
            "    COALESCE(COUNT(sales.IdHoaDon), 0) AS total_invoices\n" +
            "FROM \n" +
            "    YearRange y\n" +
            "LEFT JOIN \n" +
            "    (SELECT DATEPART(YEAR, hd.LanCapNhatCuoi) AS SaleYear, ct.SoLuong, hd.Id AS IdHoaDon\n" +
            "     FROM HoaDon hd\n" +
            "     JOIN HoaDonChiTiet ct ON hd.Id = ct.IdHoaDon\n" +
            "     WHERE hd.LanCapNhatCuoi >= DATEADD(YEAR, -5, GETDATE())\n" +
            "       AND hd.LanCapNhatCuoi < GETDATE()\n" +
            "       AND hd.TrangThai = 5) AS sales\n" +
            "ON y.SaleYear = sales.SaleYear\n" +
            "GROUP BY \n" +
            "    y.SaleYear\n" +
            "ORDER BY \n" +
            "    y.SaleYear;\n",nativeQuery = true)
    List<Object[]> namData();
    //ok
    //ok
    @Query(value = "WITH DateRange AS (\n" +
            "    SELECT CAST(GETDATE() - 6 AS DATE) AS SaleDay\n" +
            "    UNION ALL\n" +
            "    SELECT DATEADD(DAY, 1, SaleDay)\n" +
            "    FROM DateRange\n" +
            "    WHERE SaleDay < CAST(GETDATE() AS DATE)\n" +
            "),\n" +
            "SalesData AS (\n" +
            "    SELECT CAST(hd.LanCapNhatCuoi AS DATE) AS SaleDay,\n" +
            "           SUM(hd.TongTien) AS total_money,  -- Tổng tiền tính một lần cho mỗi hóa đơn\n" +
            "           COUNT(hd.Id) AS total_invoices     -- Đếm số hóa đơn trong ngày\n" +
            "    FROM HoaDon hd\n" +
            "    WHERE hd.LanCapNhatCuoi >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE))\n" +
            "          AND hd.LanCapNhatCuoi < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))\n" +
            "          AND hd.TrangThai = 5\n" +
            "    GROUP BY CAST(hd.LanCapNhatCuoi AS DATE)\n" +
            "),\n" +
            "QuantityData AS (\n" +
            "    SELECT CAST(hd.LanCapNhatCuoi AS DATE) AS SaleDay,\n" +
            "           SUM(ct.SoLuong) AS total_quantity_sold -- Tổng số lượng bán được\n" +
            "    FROM HoaDon hd\n" +
            "    JOIN HoaDonChiTiet ct ON hd.Id = ct.IdHoaDon\n" +
            "    WHERE hd.LanCapNhatCuoi >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE))\n" +
            "          AND hd.LanCapNhatCuoi < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))\n" +
            "          AND hd.TrangThai = 5\n" +
            "    GROUP BY CAST(hd.LanCapNhatCuoi AS DATE)\n" +
            ")\n" +
            "SELECT \n" +
            "    d.SaleDay,\n" +
            "    COALESCE(qd.total_quantity_sold, 0) AS total_quantity_sold,\n" +
            "    COALESCE(sd.total_money, 0) AS total_invoices\n" +
            "FROM \n" +
            "    DateRange d\n" +
            "LEFT JOIN \n" +
            "    SalesData sd ON d.SaleDay = sd.SaleDay\n" +
            "LEFT JOIN \n" +
            "    QuantityData qd ON d.SaleDay = qd.SaleDay\n" +
            "ORDER BY \n" +
            "    d.SaleDay\n" +
            "OPTION (MAXRECURSION 7);",nativeQuery = true)
    List<Object[]> dayex();
    //ok
    //ok
    @Query(value = "WITH MonthRange AS (\n" +
            "    SELECT FORMAT(DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) - 11, 0), 'yyyy-MM') AS SaleMonth\n" +
            "    UNION ALL\n" +
            "    SELECT FORMAT(DATEADD(MONTH, 1, CAST(SaleMonth + '-01' AS DATE)), 'yyyy-MM')\n" +
            "    FROM MonthRange\n" +
            "    WHERE DATEADD(MONTH, 1, CAST(SaleMonth + '-01' AS DATE)) < GETDATE()\n" +
            "),\n" +
            "SalesData AS (\n" +
            "    SELECT FORMAT(DATEADD(MONTH, DATEDIFF(MONTH, 0, hd.LanCapNhatCuoi), 0), 'yyyy-MM') AS SaleMonth,\n" +
            "           SUM(hd.TongTien) AS total_money, -- Tổng tiền chỉ tính một lần cho mỗi hóa đơn\n" +
            "           COUNT(hd.Id) AS invoice_count     -- Đếm số hóa đơn trong tháng\n" +
            "    FROM HoaDon hd\n" +
            "    WHERE hd.LanCapNhatCuoi >= DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) - 12, 0)\n" +
            "          AND hd.LanCapNhatCuoi < GETDATE()\n" +
            "          AND hd.TrangThai = 5\n" +
            "    GROUP BY FORMAT(DATEADD(MONTH, DATEDIFF(MONTH, 0, hd.LanCapNhatCuoi), 0), 'yyyy-MM')\n" +
            "),\n" +
            "QuantityData AS (\n" +
            "    SELECT FORMAT(DATEADD(MONTH, DATEDIFF(MONTH, 0, hd.LanCapNhatCuoi), 0), 'yyyy-MM') AS SaleMonth,\n" +
            "           SUM(ct.SoLuong) AS total_quantity_sold -- Tổng số lượng bán được\n" +
            "    FROM HoaDon hd\n" +
            "    JOIN HoaDonChiTiet ct ON hd.Id = ct.IdHoaDon\n" +
            "    WHERE hd.LanCapNhatCuoi >= DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) - 12, 0)\n" +
            "          AND hd.LanCapNhatCuoi < GETDATE()\n" +
            "          AND hd.TrangThai = 5\n" +
            "    GROUP BY FORMAT(DATEADD(MONTH, DATEDIFF(MONTH, 0, hd.LanCapNhatCuoi), 0), 'yyyy-MM')\n" +
            ")\n" +
            "SELECT \n" +
            "    m.SaleMonth,\n" +
            "    COALESCE(qd.total_quantity_sold, 0) AS total_quantity_sold,\n" +
            "    COALESCE(sd.total_money, 0) AS total_money_sold\n" +
            "FROM \n" +
            "    MonthRange m\n" +
            "LEFT JOIN \n" +
            "    SalesData sd ON m.SaleMonth = sd.SaleMonth\n" +
            "LEFT JOIN \n" +
            "    QuantityData qd ON m.SaleMonth = qd.SaleMonth\n" +
            "ORDER BY \n" +
            "    m.SaleMonth\n" +
            "OPTION (MAXRECURSION 0);",nativeQuery = true)
    List<Object[]> thangex();
    @Query(value = "WITH YearRange AS (\n" +
            "    SELECT DATEPART(YEAR, DATEADD(YEAR, -4, GETDATE())) AS SaleYear\n" +
            "    UNION ALL\n" +
            "    SELECT SaleYear + 1\n" +
            "    FROM YearRange\n" +
            "    WHERE SaleYear < DATEPART(YEAR, GETDATE())\n" +
            "),\n" +
            "SalesData AS (\n" +
            "    SELECT DATEPART(YEAR, hd.LanCapNhatCuoi) AS SaleYear,\n" +
            "           SUM(hd.TongTien) AS total_money,  -- Tổng tiền tính một lần cho mỗi hóa đơn\n" +
            "           COUNT(hd.Id) AS total_invoices     -- Đếm số hóa đơn trong năm\n" +
            "    FROM HoaDon hd\n" +
            "    WHERE hd.LanCapNhatCuoi >= DATEADD(YEAR, -5, GETDATE())\n" +
            "          AND hd.LanCapNhatCuoi < GETDATE()\n" +
            "          AND hd.TrangThai = 5\n" +
            "    GROUP BY DATEPART(YEAR, hd.LanCapNhatCuoi)\n" +
            "),\n" +
            "QuantityData AS (\n" +
            "    SELECT DATEPART(YEAR, hd.LanCapNhatCuoi) AS SaleYear,\n" +
            "           SUM(ct.SoLuong) AS total_quantity_sold -- Tổng số lượng bán được\n" +
            "    FROM HoaDon hd\n" +
            "    JOIN HoaDonChiTiet ct ON hd.Id = ct.IdHoaDon\n" +
            "    WHERE hd.LanCapNhatCuoi >= DATEADD(YEAR, -5, GETDATE())\n" +
            "          AND hd.LanCapNhatCuoi < GETDATE()\n" +
            "          AND hd.TrangThai = 5\n" +
            "    GROUP BY DATEPART(YEAR, hd.LanCapNhatCuoi)\n" +
            ")\n" +
            "SELECT \n" +
            "    y.SaleYear,\n" +
            "    COALESCE(qd.total_quantity_sold, 0) AS total_quantity_sold,\n" +
            "    COALESCE(sd.total_money, 0) AS total_invoices\n" +
            "FROM \n" +
            "    YearRange y\n" +
            "LEFT JOIN \n" +
            "    SalesData sd ON y.SaleYear = sd.SaleYear\n" +
            "LEFT JOIN \n" +
            "    QuantityData qd ON y.SaleYear = qd.SaleYear\n" +
            "ORDER BY \n" +
            "    y.SaleYear;",nativeQuery = true)
    List<Object[]> namex();

    @Query(value = "WITH DateRange AS (" +
            "    SELECT CAST(:startDate AS DATE) AS SaleDay" +
            "    UNION ALL" +
            "    SELECT DATEADD(DAY, 1, SaleDay)" +
            "    FROM DateRange" +
            "    WHERE SaleDay < CAST(:endDate AS DATE)" +
            ")" +
            "SELECT " +
            "    d.SaleDay," +
            "    COALESCE(SUM(sales.SoLuong), 0) AS total_quantity_sold," +
            "    COALESCE(COUNT(sales.IdHoaDon), 0) AS total_invoices " +
            "FROM " +
            "    DateRange d " +
            "LEFT JOIN " +
            "    (SELECT CAST(hd.LanCapNhatCuoi AS DATE) AS sale_day, ct.SoLuong, hd.Id AS IdHoaDon " +
            "     FROM HoaDon hd " +
            "     JOIN HoaDonChiTiet ct ON hd.Id = ct.IdHoaDon " +
            "     WHERE hd.LanCapNhatCuoi >= :startDate " +
            "       AND hd.LanCapNhatCuoi < DATEADD(DAY, 1, :endDate) " +
            "       AND hd.TrangThai = 5) AS sales " +
            "ON d.SaleDay = sales.sale_day " +
            "GROUP BY " +
            "    d.SaleDay " +
            "ORDER BY " +
            "    d.SaleDay " +
            "OPTION (MAXRECURSION 0);", nativeQuery = true)
    List<Object[]> khoangNgay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
