package com.example.demo.repository;

import com.example.demo.entity.SanPhamDotGiam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamDotGiamRepository extends JpaRepository<SanPhamDotGiam,Integer> {

}
