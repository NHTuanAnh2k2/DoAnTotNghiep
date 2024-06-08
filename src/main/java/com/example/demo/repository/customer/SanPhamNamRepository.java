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
                AND ((?1 IS NULL OR spct.thuonghieu.id IN (?1))
                OR (?2 IS NULL OR spct.kichco.id IN (?2)))
            GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, spct.gioitinh
            ORDER BY sp.ngaytao DESC, tongSoLuong DESC
                  """)
    Page<Object[]> searchByGender1(List<Integer> idthuonghieu, List<Integer> idkichco, Pageable pageable);


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
