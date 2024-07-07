package com.example.demo.repository.giohang;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangGioHangRepository extends JpaRepository<KhachHang, Integer> {
    @Query(value = """
            SELECT k FROM KhachHang k where k.nguoidung.id= :id
            """)
    KhachHang findByNguoidung(Integer id);


}
