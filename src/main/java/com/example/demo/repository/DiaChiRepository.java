package com.example.demo.repository;

import com.example.demo.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {
    List<DiaChi> getDiaChiByTrangthai(Boolean trangThai);
    List<DiaChi> getAllByOrderByIdDesc();
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.id = ?1")
    DiaChi TimIdNguoiDung(Integer id);
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.hovaten = ?1 OR c.nguoidung.sodienthoai = ?2")
    List<DiaChi> TimhoTenHoacSdt(String ht, String sdt);
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.trangthai = ?1")
    List<DiaChi> TimTrangThai(Boolean tt);

}
