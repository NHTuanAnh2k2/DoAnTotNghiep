package com.example.demo.repository;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamRepositoty extends JpaRepository<SanPham, Integer> {
    Page<SanPham> findAllByTensanphamAndTrangthaiAndSpctSoluong(String tensanpham, Boolean trangthai, Integer soluong, Pageable pageable);
}
