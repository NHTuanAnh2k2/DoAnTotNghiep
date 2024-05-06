package com.example.demo.repository.khachhang;

import com.example.demo.entity.KhachHang;
import com.example.demo.info.KhachHangInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface KhachHangRepostory extends JpaRepository<KhachHang, Integer> {
    @Query("SELECT k FROM KhachHang k WHERE k.nguoidung.hovaten LIKE %?1% OR k.nguoidung.sodienthoai LIKE %?1% OR k.makhachhang LIKE %?1%")
    List<KhachHang> findByTenSdtMa(@Param("tenSdtMa") String tenSdtMa);

    //    @Query("SELECT K FROM KhachHang k where k.id=?1")
//    KhachHang findKhachHangById(Integer id);
    @Query("SELECT k FROM KhachHang k ORDER BY k.ngaytao DESC")
    List<KhachHang> findAllKhachHang();

    @Query("SELECT k FROM KhachHang k WHERE k.nguoidung.id = ?1")
    KhachHang findByNguoiDung(Integer id);
}
