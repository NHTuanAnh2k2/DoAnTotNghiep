package com.example.demo.repository.khachhang;

import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface KhachHangRepostory extends JpaRepository<KhachHang, Integer> {
    @Query("SELECT k FROM KhachHang k WHERE k.nguoidung.hovaten LIKE %?1% AND k.nguoidung.sodienthoai LIKE %?2% AND k.nguoidung.trangthai = ?3 AND k.nguoidung.ngaysinh = ?4")
    List<KhachHang> findByAll(String ten, String sdt, int trangthai, Date ngaysinh);
//    @Query("SELECT K FROM KhachHang k where k.id=?1")
//    KhachHang findKhachHangById(Integer id);
    @Query("SELECT k FROM KhachHang k ORDER BY k.ngaytao DESC")
    List<KhachHang> findAllKhachHang();

}
