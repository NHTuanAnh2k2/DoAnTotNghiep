package com.example.demo.repository;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SanPhamRepositoty extends JpaRepository<SanPham, Integer> {
    Page<SanPham> findAllByTensanphamOrTrangthai(String tensanpham, Boolean trangthai, Pageable pageable);

    Page<SanPham> findAllByOrderByNgaytaoDesc(Pageable pageable);
}
