package com.example.demo.repository.giohang;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangGioHangRepository extends JpaRepository<KhachHang, Integer> {

    KhachHang findByNguoidung(NguoiDung nguoidung);
}
