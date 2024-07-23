package com.example.demo.repository;

import com.example.demo.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface SanPhamChiTietRepository1 extends JpaRepository<SanPhamChiTiet, Integer> {
   @Query("SELECT spc FROM SanPhamChiTiet spc " +
           "JOIN spc.mausac ms")
    List<SanPhamChiTiet> findSanPhamChiTietByIdHoaDon(Integer idHoaDon);


}