package com.example.demo.repository;

import com.example.demo.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguoiDungRepository1 extends JpaRepository<NguoiDung, Integer> {
    @Query("SELECT c FROM NguoiDung c WHERE c.email = ?1")
    NguoiDung searchEmail(String email);
List<NguoiDung> getNhanVienByTrangthai(Boolean trangThai);
}
