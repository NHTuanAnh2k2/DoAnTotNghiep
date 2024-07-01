package com.example.demo.repository.giohang;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GioHangRepository extends JpaRepository<GioHang,Integer> {

    @Query("SELECT gh FROM GioHang gh WHERE gh.khachhang = :khachhang AND gh.trangthai = true")
    GioHang findCurrentGioHang(@Param("khachhang") KhachHang khachhang);

    GioHang findByKhachhang(KhachHang khachHang);

    @Query(value = """
            SELECT g FROM GioHang g where g.khachhang.id= :id
            """)
    GioHang findByIdKhachHang(Integer id);

}
