package com.example.demo.repository;

import com.example.demo.entity.SanPhamDotGiam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamDotGiamRepository extends JpaRepository<SanPhamDotGiam,Integer> {
    @Query("SELECT s FROM SanPhamDotGiam s WHERE s.dotgiamgia.id=:IdDot")
    List<SanPhamDotGiam> findSanPhamDotGiamByIdDotgiamgia(Integer IdDot);
}
