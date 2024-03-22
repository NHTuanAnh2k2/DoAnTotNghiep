package com.example.demo.repository;

import com.example.demo.entity.LoaiGiay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThuongHieuRepository extends JpaRepository<LoaiGiay,Integer> {
}
