package com.example.demo.repository;

import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public interface NhanVienRepository extends JpaRepository<NhanVien,Integer> {
    List<NhanVien> getNhanVienByTrangthai(Boolean trangThai);

}
