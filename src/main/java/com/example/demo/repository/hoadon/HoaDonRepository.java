package com.example.demo.repository.hoadon;

import com.example.demo.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    Page<HoaDon> findAllByTrangthaiAndLoaihoadonAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(Integer trangThai, Boolean loaihd,Date tu, Date den, Pageable p);
     Page<HoaDon> findAll(Pageable p);
    Page<HoaDon> findAllByTrangthai(Integer trangThai,Pageable p);
}
