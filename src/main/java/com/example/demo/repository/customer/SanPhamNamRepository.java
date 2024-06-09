package com.example.demo.repository.customer;

import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamNamRepository extends JpaRepository<SanPham, Integer> {
    // sản phẩm nam
    @Query(nativeQuery = true, value = """
            SELECT sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh, spct.GioiTinh
            FROM SanPham sp
            JOIN (
                SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien, GioiTinh
                FROM SanPhamChiTiet
                GROUP BY IdSanPham, giatien, GioiTinh
            ) spct ON sp.id = spct.IdSanPham
            JOIN Anh anh ON sp.id = anh.Id
            WHERE spct.GioiTinh = 1
            ORDER BY sp.ngaytao DESC, tongSoLuong DESC
                  """)
    Page<Object[]> findProductsGioiTinh1(Pageable pageable);


    // sắp xếp tăng dần theo giá của sp nam
    @Query(nativeQuery = true, value = """
            SELECT sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh, spct.GioiTinh
            FROM SanPham sp
            JOIN (
                SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien, GioiTinh
                FROM SanPhamChiTiet
                GROUP BY IdSanPham, giatien, GioiTinh
            ) spct ON sp.id = spct.IdSanPham
            JOIN Anh anh ON sp.id = anh.Id
            WHERE spct.GioiTinh = 1
            ORDER BY spct.giatien ASC 
                  """)
    Page<Object[]> loctangdan(Pageable pageable);

    //sắp xếp giảm dần theo giá của sp nam
    @Query(nativeQuery = true, value = """
            SELECT sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh, spct.GioiTinh
            FROM SanPham sp
            JOIN (
                SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien, GioiTinh
                FROM SanPhamChiTiet
                GROUP BY IdSanPham, giatien, GioiTinh
            ) spct ON sp.id = spct.IdSanPham
            JOIN Anh anh ON sp.id = anh.Id
            WHERE spct.GioiTinh = 1
            ORDER BY spct.giatien DESC 
                  """)
    Page<Object[]> locgiamdan(Pageable pageable);

    // lọc sản phẩm là nam
    @Query(value = """
            SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong, sp.trangthai, MAX(spct.giatien) AS maxGiaTien, MAX(anh.tenanh) AS maxTenAnh, spct.gioitinh
            FROM SanPham sp
            JOIN sp.spct spct
            JOIN spct.anh anh
            WHERE spct.gioitinh = true 
               AND (
                    (?1 = true AND spct.giatien BETWEEN 0 AND 1000000)
                    OR (?2 = true AND spct.giatien BETWEEN 1000000 AND 2000000)
                    OR (?3 = true AND spct.giatien BETWEEN 2000000 AND 3000000)
                    OR (?4 = true AND spct.giatien BETWEEN 3000000 AND 5000000)
                    OR (?5 = true AND spct.giatien > 5000000)
                )
                OR ((?6 IS NULL OR spct.thuonghieu.id IN (?6))
                OR (?7 IS NULL OR spct.kichco.id IN (?7))
                OR (?8 IS NULL OR spct.mausac.id IN (?8)))
                GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, spct.gioitinh
                ORDER BY sp.ngaytao DESC, tongSoLuong DESC
                  """)
    Page<Object[]> loctheothkc(Boolean range1, Boolean range2, Boolean range3, Boolean range4, Boolean range5, List<Integer> idthuonghieu, List<Integer> idkichco,List<Integer> idmausac, Pageable pageable);

    // tìm kiếm theo mã và tên sản phẩm nam
    @Query(value = """
            SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong, sp.trangthai, MAX(spct.giatien) AS maxGiaTien, MAX(anh.tenanh) AS maxTenAnh, spct.gioitinh
            FROM SanPham sp
            JOIN sp.spct spct
            JOIN spct.anh anh
            WHERE spct.gioitinh = true 
                AND (sp.masanpham LIKE %?1% OR sp.tensanpham LIKE %?2%)
            GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, spct.gioitinh
            ORDER BY sp.ngaytao DESC, tongSoLuong DESC
                  """)
    Page<Object[]> searchByMaAnhTenSP(String masanpham, String tensanpham, Pageable pageable);


}
