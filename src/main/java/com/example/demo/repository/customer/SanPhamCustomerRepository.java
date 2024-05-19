package com.example.demo.repository.customer;

import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamCustomerRepository extends JpaRepository<SanPham,Integer> {
    //dùng cho sp client có giới tính là 0
    @Query(nativeQuery = true, value = """
         SELECT sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh, spct.GioiTinh
         FROM SanPham sp
         JOIN (
             SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien, GioiTinh
             FROM SanPhamChiTiet
             GROUP BY IdSanPham, giatien, GioiTinh
         ) spct ON sp.id = spct.IdSanPham
         JOIN Anh anh ON sp.id = anh.Id
         WHERE spct.GioiTinh = 0
         ORDER BY sp.ngaytao DESC, tongSoLuong DESC
               """)
    List<Object[]> findProductsGioiTinh0();


    //dùng để lọc sp client có giới tính là 0 và theo thương hiệu, kích cỡ
    @Query(value = """
         SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh, spct.gioitinh
         FROM SanPham sp
         JOIN sp.spct spct
         JOIN spct.anh anh
         WHERE spct.gioitinh = false 
             AND (?1 IS NULL OR spct.thuonghieu.id IN (?1))
             OR (?2 IS NULL OR spct.kichco.id IN (?2))
         GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, spct.giatien, anh.tenanh, spct.gioitinh
         ORDER BY sp.ngaytao DESC, tongSoLuong DESC
               """)
    List<Object[]> searchByGender0(List<Integer> idthuonghieu, List<Integer> idkichco);


    //dùng cho sp client có giới tính là 1
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


    //tăng dần nam theo giá tiền
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

    //giảm dần nam theo giá tiền
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

    //dùng để lọc sp client có giới tính là 1 và theo thương hiệu, kích cỡ
    @Query(value = """
         SELECT sp.id, sp.tensanpham, sp.ngaytao, SUM(spct.soluong) AS tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh, spct.gioitinh
         FROM SanPham sp
         JOIN sp.spct spct
         JOIN spct.anh anh
         WHERE spct.gioitinh = true 
             AND (?1 IS NULL OR spct.thuonghieu.id IN (?1))
             OR (?2 IS NULL OR spct.kichco.id IN (?2))
         GROUP BY sp.id, sp.tensanpham, sp.ngaytao, sp.trangthai, spct.giatien, anh.tenanh, spct.gioitinh
         ORDER BY sp.ngaytao DESC, tongSoLuong DESC
               """)
    Page<Object[]> searchByGender1(List<Integer> idthuonghieu, List<Integer> idkichco,Pageable pageable);
    // tang dan nu theo gia tien
    @Query(nativeQuery = true, value = """
         SELECT sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh, spct.GioiTinh
         FROM SanPham sp
         JOIN (
             SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien, GioiTinh
             FROM SanPhamChiTiet
             GROUP BY IdSanPham, giatien, GioiTinh
         ) spct ON sp.id = spct.IdSanPham
         JOIN Anh anh ON sp.id = anh.Id
         WHERE spct.GioiTinh = 0
         ORDER BY spct.giatien ASC 
               """)
    List<Object[]> loctangdannu();

    // tang dan nu theo gia tien
    @Query(nativeQuery = true, value = """
         SELECT sp.id, sp.tensanpham, sp.ngaytao, tongSoLuong, sp.trangthai, spct.giatien, anh.tenanh, spct.GioiTinh
         FROM SanPham sp
         JOIN (
             SELECT IdSanPham, SUM(soluong) AS tongSoLuong, giatien, GioiTinh
             FROM SanPhamChiTiet
             GROUP BY IdSanPham, giatien, GioiTinh
         ) spct ON sp.id = spct.IdSanPham
         JOIN Anh anh ON sp.id = anh.Id
         WHERE spct.GioiTinh = 0
         ORDER BY spct.giatien DESC 
               """)
    List<Object[]> locgiamdannu();
}
