package com.example.demo.repository;

import com.example.demo.entity.KhachHangPhieuGiam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangPhieuGiamRepository extends JpaRepository<KhachHangPhieuGiam,Integer> {

}
