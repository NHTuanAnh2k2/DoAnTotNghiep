package com.example.demo.repository.customer;

import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamNuRepository extends JpaRepository<SanPham, Integer> {

    //sản phẩm nữ
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
                    WHERE gioitinh = 0
                    GROUP BY IdSanPham
                )
                SELECT sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh
                FROM SanPham sp
                JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
                JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
                ORDER BY sp.ngaytao DESC, spctg.tongSoLuong DESC
            """)
    List<Object[]> findProductsGioiTinh0();

    // sắp xếp tăng dần theo giá của sp nữ
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
                    WHERE gioitinh = 0
                    GROUP BY IdSanPham
                )
                SELECT sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh
                FROM SanPham sp
                JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
                JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
                ORDER BY spctg.giatien ASC , spctg.tongSoLuong DESC
            """)
    List<Object[]> loctangdannu();

    //sắp xếp giảm dần theo giá của sp nữ
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
                    WHERE gioitinh = 0
                    GROUP BY IdSanPham
                )
                SELECT sp.id, sp.tensanpham, sp.ngaytao, spctg.tongSoLuong, sp.trangthai, spctg.giatien, anhdd.tenanh
                FROM SanPham sp
                JOIN SanPhamChiTietGrouped spctg ON sp.id = spctg.IdSanPham
                JOIN AnhDaiDien anhdd ON sp.id = anhdd.IdSanPham AND anhdd.row_num = 1
                ORDER BY spctg.giatien DESC , spctg.tongSoLuong DESC
            """)
    List<Object[]> locgiamdannu();

    //lọc sản phẩm là nữ
    @Query(value = """
            SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong, sp.trangthai, MAX(spct.giatien) AS maxGiaTien, MAX(anh.tenanh) AS maxTenAnh, spct.gioitinh
            FROM SanPham sp
            JOIN sp.spct spct
            JOIN spct.anh anh
            WHERE 
                (
                    (?1 = true AND spct.giatien BETWEEN 0 AND 1000000)
                    OR (?2 = true AND spct.giatien BETWEEN 1000000 AND 2000000)
                    OR (?3 = true AND spct.giatien BETWEEN 2000000 AND 3000000)
                    OR (?4 = true AND spct.giatien BETWEEN 3000000 AND 5000000)
                    OR (?5 = true AND spct.giatien > 5000000)
                )
                OR ((?6 IS NULL OR spct.thuonghieu.id IN (?6))
                OR (?7 IS NULL OR spct.kichco.id IN (?7))
                OR (?8 IS NULL OR spct.mausac.id IN (?8))) AND spct.gioitinh = false 
                GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, spct.gioitinh
                ORDER BY sp.ngaytao DESC, tongSoLuong DESC
                  """)
    List<Object[]> loctheothkcnu(Boolean range1, Boolean range2, Boolean range3, Boolean range4, Boolean range5, List<Integer> idthuonghieu, List<Integer> idkichco, List<Integer> idmausac);

    // tìm kiếm theo mã và tên sản phẩm nữ
    @Query(value = """
            SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong, sp.trangthai, MAX(spct.giatien) AS maxGiaTien, MAX(anh.tenanh) AS maxTenAnh, spct.gioitinh
            FROM SanPham sp
            JOIN sp.spct spct
            JOIN spct.anh anh
            WHERE 
                 (sp.masanpham LIKE %?1% OR sp.tensanpham LIKE %?2%) AND spct.gioitinh = false 
            GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, spct.gioitinh
            ORDER BY sp.ngaytao DESC, tongSoLuong DESC
                  """)
    List<Object[]> searchByMaAnhTenSPNu(String masanpham, String tensanpham);
}
