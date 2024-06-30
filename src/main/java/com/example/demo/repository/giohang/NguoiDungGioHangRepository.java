package com.example.demo.repository.giohang;

import com.example.demo.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguoiDungGioHangRepository extends JpaRepository<NguoiDung, Integer> {

    NguoiDung findByTaikhoan(String taiKhoan);
}
