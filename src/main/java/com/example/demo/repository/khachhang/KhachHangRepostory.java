package com.example.demo.repository.khachhang;

import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangRepostory extends JpaRepository<KhachHang, Integer> {
}
