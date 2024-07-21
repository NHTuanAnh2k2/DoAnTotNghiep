package com.example.demo.repository;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository1 extends JpaRepository<NguoiDung, Integer> {
    @Query("SELECT c FROM NguoiDung c left join KhachHang n on n.nguoidung.id = c.id where c.sodienthoai = ?1 and  n.id = null")
    NguoiDung searchEmail(String sdt);
    @Query("SELECT c FROM NguoiDung c WHERE c.id = ?1")
    NguoiDung searchId(Integer id);
    List<NguoiDung> getAllByOrderByIdDesc();
}
