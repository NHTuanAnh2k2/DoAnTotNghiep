package com.example.demo.repository;

import com.example.demo.entity.KhachHangPhieuGiam;
import com.example.demo.entity.SanPhamDotGiam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhachHangPhieuGiamRepository extends JpaRepository<KhachHangPhieuGiam,Integer> {
    @Query("SELECT s FROM KhachHangPhieuGiam s WHERE s.phieugiamgia.id =:IdPhieu")
    List<KhachHangPhieuGiam> findKhachHangPhieuGiamByIdPhieugiamgia(Integer IdPhieu);
}
