package com.example.demo.repository;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {
    List<DiaChi> getNhanVienByTrangthai(Boolean trangThai);
}
